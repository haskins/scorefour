/**
 * This is the referee class for Supreme Score 4. Some of the responsibilities
 * of the referee are: keeping track of whose turn it is, keeping track of how
 * many turns have passed by since the start of the game, reading and writing to
 * a buffer to allow the computer to act, and telling the computer player when
 * it is okay to quit the game.
 * 
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */
import gui.MainGUI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import board.*;
import scoreboard.*;

public class Referee {
	// Used to communicate with the GUI.
	private static LinkedBlockingQueue<String> sendQueue;
	private static LinkedBlockingQueue<String> recvQueue;

	// Whose turn it is, with false being player 1 and true being player 2.
	public static boolean turn;

	// The state of the GUI.
	public boolean guiOn;

	// The number of turns that have passed, used for high scores.
	public static int turnCount;

	// board is used in conjunction with the line class to determine when a
	// win/draw has occurred.
	static Board board;

	// The two players.
	static Player player1;
	static Player player2;

	// Used for restarting the game.
	static boolean wantsNewGame;

	public static void main(String[] args) {
		// Initialize the Queues.
		sendQueue = new LinkedBlockingQueue<String>();
		recvQueue = new LinkedBlockingQueue<String>();

		wantsNewGame = false;

		// Start the GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public synchronized void run() {
				MainGUI gui;
				try {
					// Player 1 is white and Player 2 is black.
					gui = new MainGUI('W', 'B', sendQueue, recvQueue);
					gui.createAndShowGUI();

				} catch (InterruptedException e) {
					System.err.println("Couldn\'t start game");
					e.printStackTrace();
					System.exit(1);
				} catch (ExecutionException e) {
					System.err.println("Couldn\'t start the game");
					e.printStackTrace();
					System.exit(1);
				}

			}
		});

		// At this point, the user can interact with the GUI.
		try {
			// get type of game
			String tempName = receiveFromGUI();
			String typeOfGame = tempName;
			// System.out.println("I got type of game:" + tempName);

			// Get the names of the players.
			tempName = receiveFromGUI();
			player1 = new Player(tempName,
					typeOfGame.equals("Human vs Gertrudis") ? true : false);
			// System.out.println("I got a name:" + tempName);

			tempName = receiveFromGUI();
			player2 = new Player(tempName, false);
			// System.out.println("I got a name:" + tempName);

			do {
				startTheLoop(typeOfGame);
			} while (wantsNewGame);
		} catch (InterruptedException e) {
			System.out.println("Couldn\'t get the names");
		}
	}

	/**
	 * This is the central method of the Referee. It is responsible for getting
	 * input from the two players and determining if the game has has been won
	 * or if there is a draw.
	 * 
	 * @param typeOfGame
	 *            The type of game that is to be played (i.e. PvC, CvC, etc.)
	 */
	public static void startTheLoop(String typeOfGame)
			throws InterruptedException {
		// Create the board.
		wantsNewGame = false;
		board = new Board();
		turnCount = 0;

		// Declare a String to contain the input from the GUI.
		String fromGUI;

		// Used for the computer player's moves.
		boolean answer;
		int move;

		// Run the game as long as the players have beads.
		while (turnCount <= 32) {
			turnCount++;

			// Get the first player's move.
			turn = true;

			// Player vs. Computer.
			if (typeOfGame.equals("Human vs Gertrudis")) {
				// Wait for input from player 1 in GUI.
				fromGUI = receiveFromGUI();
				// System.out.println("Referee, I got: " + fromGUI);

				// Restart the game if the user wishes so.
				if (fromGUI.equals("restart game")) {
					wantsNewGame = true;
					break;
				}

				// Set a bead on a peg with the input.
				board.setPeg(Integer.parseInt(fromGUI), 'W');

				// If this move results in either a win or a draw, end the game.
				if (Line.checkForDraw(board.getPosition())
						|| Line.checkForWin(board.getPosition())) {
					// System.out.println("p1 made the final move");
					break;
				}

				turn = false;
				// Get input from player 2.
				do {
					move = player2.getInput(board.getPosition());
					answer = board.setPeg(move, 'B');
					// System.out.println("LOOPING");
				} while (answer == false);

				// Inform the GUI of player 2's move.
				sendToGUI(Integer.toString(move));
				// System.out.println("Referee, I sent " + move);
			}

			// Computer vs. Computer.
			else {
				// Get player 1's move.
				do {
					move = player1.getInput(board.getPosition());
					answer = board.setPeg(move, 'W');
					// System.out.println("LOOPING");
				} while (answer == false);

				// Inform the GUI of player 1's move.
				sendToGUI(Integer.toString(move));
				// System.out.println("Referee, I sent " + move);

				// If this move results in either a win or a draw, end the game.
				if (Line.checkForDraw(board.getPosition())
						|| Line.checkForWin(board.getPosition())) {
					// System.out.println("p1 made the final move");
					break;
				}

				turn = false;
				// Get player 2's move.
				do {
					move = player2.getInput(board.getPosition());
					answer = board.setPeg(move, 'B');
					// System.out.println("LOOPING");
				} while (answer == false);

				// Inform the GUI of player 1's move.
				sendToGUI(Integer.toString(move));
				// System.out.println("Referee, I sent " + move);
			}

			// If this move results in either a win or a draw, end the game.
			if (Line.checkForDraw(board.getPosition())
					|| Line.checkForWin(board.getPosition())) {
				// System.out.println("p1 made the final move");
				break;
			}
		}

		// If there is a draw, inform the user of it and ask if they would like
		// to restart.
		if (Line.checkForDraw(board.getPosition())) {
			// System.out.println("Referee, sent to GUI: draw");
			sendToGUI("draw");
		}

		// If there is a win, inform the user of who won and ask if they would
		// like to restart.
		else if (Line.checkForWin(board.getPosition())) {
			// If player 1 wins, display an appropriate answer.
			if (turn) {
				// System.out.println("Referee, sent to GUI: win 1");
				sendToGUI("win 1");
				Scoreboard.setScore(turnCount, player1.getName());
			}

			// If player 2 wins, display an appropriate answer.
			else if (turn == false) {
				// System.out.println("Referee, sent to GUI: win 2");
				sendToGUI("win 2");
				Scoreboard.setScore(turnCount, player2.getName());
			}

			// The only way this can occur is if something went wrong.
			else
				System.out.println("error in loop");
		}

		// Determine if the user wants to player another game.
		String nada = receiveFromGUI();
		// System.out.println("Refreee, I got " + nada);

		if (nada.equals("restart game"))
			wantsNewGame = true;
	}

	/**
	 * This method recieves input from the GUI.
	 * 
	 * @return A message from the GUI.
	 */
	public static String receiveFromGUI() throws InterruptedException {
		return recvQueue.take();
	}

	/**
	 * This method writes output to the GUI.
	 * 
	 * @param position
	 *            The peg the user wants to play on.
	 */
	public static void sendToGUI(String position) throws InterruptedException {
		sendQueue.put(position);
	}

	/**
	 * This method gets the turn count, used to make the high score board.
	 * 
	 * @return The number of turns that have passed.
	 */
	public int getTurnCount() {
		return turnCount;
	}

	public Board giveBoard() {
		return board;
	}
}
