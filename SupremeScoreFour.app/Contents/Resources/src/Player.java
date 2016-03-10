/**
 * This is the class that handles the individual players (i.e. player 1
 * and player 2).
 * 
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

import java.util.Scanner;
import java.io.*;
import ca.unbc.cpsc101.JavaPipe;
import board.*;

public class Player {
	private String playerName;
	private JavaPipe computer;
	public Scanner pipeScanner;

	// Used for computer players. computerCreated is used to
	// pervent multiple (needless) pipe from being set up.
	private boolean isHuman;
	private boolean computerCreated;

	private static ObjectOutputStream output; // outputs data to file

	// allow user to specify file name
	public static void openFile() {
		try // open file
		{
			output = new ObjectOutputStream(new FileOutputStream("clients.ser"));
		} // end try
		catch (IOException ioException) {
			System.err.println("Error opening file.");
		} // end catch
	} // end method openFile

	public static void closeFile() {
		try // close file
		{
			if (output != null)
				output.close();
		} // end try
		catch (IOException ioException) {
			System.err.println("Error closing file.");
			System.exit(1);
		} // end catch
	} // end method closeFile

	/**
	 * Creates a named player object that can either be a human or a computer.
	 * 
	 * @param name
	 *            The name of the Player.
	 * @param human
	 *            Whether or not the player is a human
	 */

	public void killAll() {
		computer.destroy();
		// oos.close();
	}

	public Player(String name, boolean human) {
		playerName = name;
		isHuman = human;

		if (isHuman == false) {
			try {
				computer = new JavaPipe("Gertrude");
				computer.start();
				pipeScanner = new Scanner(computer.getInputReader());

			} catch (Exception e1) {
				System.out.println("error on creating comp");
			}
		}
	}

	/**
	 * Used to get input, either from a user via the GUI or from the computer
	 * with pipes.
	 * 
	 * @return The user's move.
	 */
	public int getInput(Peg[] peg) {
		int answer;
		if (isHuman) {
			// Ask the user for input.
			// System.out.println("Make a move " + getName() + " : ");

			Scanner s = new Scanner(System.in);
			answer = s.nextInt();

			// Used when playing from the Terminal!
			while (answer > 16) {
				System.out.println("Please enter a number 16 and under");
				answer = s.nextInt();
			}
			return answer;
		}

		else {

			openFile();

			try {
				System.out.println("sending object");
				// output.writeObject(board.getPosition());
				output.writeObject(peg);
				System.out.println("object sent");

			} catch (IOException ioException) {
				System.err.println("Error writing to file.");
			} // end catch

			computer.getOutputWriter().println("move;");

			return pipeScanner.nextInt();

		}
	}

	/**
	 * Get a a player's name, used with the high score board.
	 * 
	 * @return The player's name.
	 */
	public String getName() {
		return playerName;
	}
}
