/**
 * What is displayed on screen.
 * 
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;
import javax.swing.SwingWorker.StateValue;
import board.*;

public class Panel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -7044872653228764154L;
	
	//the queues (for communication purposes), we get this queues from class MainGUI
	private LinkedBlockingQueue<String> sendQueue;
	private LinkedBlockingQueue<String> recvQueue;

	//vars
	private Board board;
	private Bead[] availableBeads;
	private Peg[] pegs;
	private Timer animationTimer;
	private Timer typeOfGameTimer;
	private Timer isWinLoseTimer;
	private Timer remoteBeadAnimTimer;
	private Timer updateWhenLoaded;
	private Image imgToShow;
	private JMenuBar menuBar;
	private MouseListener mouseListener;
	private int winner;
	private String typeOfGame;
	private boolean isFirstTime;
	private boolean beadMustAnimate;
	private int insertedInPegNum;
	private int currentBead;
	private boolean isFirstTransaction;

	// constants
	private static final char NUM_ASCII_OFFSET = 48;
	public final int MAX_BEADS_PER_PEG = 4;
	public final int MAX_NUM_PEGS = 16;
	public final int MAX_NUM_BEADS = 65;
	public final int BOARD_INITIAL_X = 5;
	public final int BOARD_INITIAL_Y = 200;
	public final int BEAD_INITIAL_X = 50;
	public final int BEAD_INITIAL_Y = 450;
	public final int[] PEG_INITIAL_X = { 
		305, 408, 511, 614, 
		223, 329, 435, 541, 
		141, 250, 359, 468, 
		60, 171, 283, 395 };
	public final int[] PEG_INITIAL_Y = { 
		10, 43, 76, 100, 
		56, 95, 132, 171, 
		103, 150, 196, 243, 
		150, 205, 260, 315, };
	public final int BEAD_TEXT_INITIAL_X = 5;// percetages (0-100)
	public final int BEAD_TEXT_INITIAL_Y = 80; // percetages (0-100)
	public final int TEXT_P1_INITIAL_X = 10; // percentages (0-100)
	public final int TEXT_P1_INITIAL_Y = 90; // percetages (0-100)
	public final int TEXT_P2_INITIAL_X = 70; // percetages (0-100)
	public final int TEXT_P2_INITIAL_Y = 90; // percetages (0-100)
	public final int ANIMATION_REFRESH_RATE = 15; // rate at which panel is repainted in ms
	
	private static final int LOSE_WIN_TIE_IMAGE_X = 114;
    private static final int LOSE_WIN_TIE_IMAGE_Y = 80;
    private static final int LOSE_WIN_TIE_TEXT_X = 30;
    private static final int LOSE_WIN_TIE_TEXT_Y = 90;
    
	//FILE NAMES OF IMAGES TO BE DISPLAYED WHEN WINNIN/LOSING/TYING
	public final int IMAGE_LOADERS_TIMER = 1500;
	public final String TIE_IMAGE_FILE = "tie.jpeg";
	public final String LOSE_IMAGE_FILE = "computer_wins.png";  
	public final String WIN_IMAGE_FILE = "player_wins.png";
	public final String BACKGROUND = "background.jpg";
	public final int NO_WINNER = -1;


	////////////////////////////////////////////////////////////
    	// 			CONSTRUCTOR
    	////////////////////////////////////////////////////////////
	public Panel(String p1, String p2, LinkedBlockingQueue<String> sendQueue,
			LinkedBlockingQueue<String> recvQueue, JMenuBar menuBar ) throws InterruptedException,
			ExecutionException {
		winner = NO_WINNER;
		typeOfGame = "";
		this.menuBar = menuBar;
		this.sendQueue = sendQueue;  //receives queues
		this.recvQueue = recvQueue;
		board = new Board(BOARD_INITIAL_X, BOARD_INITIAL_Y); // creates board
		board.setPlayerNames(p1, p2);
		pegs = new Peg[MAX_NUM_PEGS];
		beadMustAnimate = false;
		isFirstTransaction = true;
		insertedInPegNum = 0;
		currentBead = 0;
		// its referree's responsibility to stop the game when no more bead are avasilable
		availableBeads = new Bead[MAX_NUM_BEADS*2]; 

		//sets first free bead
		availableBeads[0] = new Bead(BEAD_INITIAL_X, BEAD_INITIAL_Y, 'w');
		availableBeads[0].setImage(board.getWhiteBeadImage());
		for (int i = 0; i < 16; i++) {
			pegs[i] = new Peg(PEG_INITIAL_X[i], PEG_INITIAL_Y[i]);
		}

		setFocusable(true); // makes panel focusable

		//adds listeners
		mouseListener = new MouseListener();
		addMouseMotionListener(mouseListener);
		addMouseListener(mouseListener);

		//when this timer fires, images will be displayed
		updateWhenLoaded = new Timer(IMAGE_LOADERS_TIMER, this);
		updateWhenLoaded.setActionCommand("images are loaded");
		updateWhenLoaded.setRepeats(false);// occurs only one time
		updateWhenLoaded.start();
	}

	////////////////////////////////////////////////////////////
    // 			OVERRIDEN
    ////////////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent e) {
		// System.out.println( "timer event (1) !");
		if (e.getActionCommand().equals("remote bead animator")) {

			availableBeads[currentBead - 1].animation(5);

			int tolerance = 5;
			if (availableBeads[currentBead - 1].getY() > (int) (pegs[insertedInPegNum - 1]
					.getBounds().getMaxY()
					- availableBeads[currentBead - 1].getBounds().getHeight() - (availableBeads[currentBead - 1]
					.getBounds().getHeight() + tolerance)
					* (pegs[insertedInPegNum - 1].amountOfBeads2() - 1))) {

				// here the remote player finished moving
				remoteBeadAnimTimer.stop();

				// keep reading from refereee!!!!!!
				try {
					receiveFromReferee();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}// end if
			repaint(); // happens every time the animationTimer triggers an event

		} else if (e.getActionCommand().equals("images are loaded")) {
			// images are loaded, then just paint them
			// this only happens one time after user
			// clicked in new game menu
			repaint();
		} else if (e.getActionCommand().equals("is win_lose")) {
			// images are loaded, then just paint them
			// this only happens one time after user
			// clicked in new game menu
			repaint();
			startImageLoader( LOSE_IMAGE_FILE );
			updateWhenLoaded.stop();
		}
		else if( e.getActionCommand().equals("Gertrude vs Computer" ) ){

			// starts the worker that receives from refereee
			try {
				receiveFromReferee();// this is a worker
			} catch (InterruptedException e1) {
				System.out.println("COuldnt receive from referee");
			}
		
		} else if (e.getActionCommand().equals("animation")) {
			availableBeads[currentBead - 1].animation(5);

			int tolerance = 5;
			if (availableBeads[currentBead - 1].getY() > pegs[insertedInPegNum - 1].getBounds().getMaxY()
					- availableBeads[currentBead - 1].getBounds().getHeight()
					- (availableBeads[currentBead - 1].getBounds().getWidth() + tolerance)
					* (pegs[insertedInPegNum - 1].amountOfBeads2() - 1)) {

				animationTimer.stop();
				//send to the referee the user's move
				 System.out.println("GUI, I sent " + insertedInPegNum);
				sendToReferee( Integer.toString(insertedInPegNum ) );

				// here the local player finished moving a piece,
				// now its turn of the remote player to go!
				// starts the worker that receives from refereee
				try {
					receiveFromReferee();// this is a worker
				} catch (InterruptedException e1) {
					System.out.println("Couldnt receive from referee");
				}

			}// end if
			repaint(); 

		}// end if-else

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		displayNames(g);
		displayWinner(g);
		g2d.drawImage(board.getImage(), board.getX(), board.getY(), null, null);

		for (int i = 0; i < 16; i++) {
			g2d.drawImage(pegs[i].getImage(), pegs[i].getX(), pegs[i].getY(),
					null, null);
			for (int j = 0; j < pegs[i].amountOfBeads2(); j++) {
				g2d.drawImage(pegs[i].beadAt(j).getImage(), pegs[i].beadAt(j)
						.getX(), pegs[i].beadAt(j).getY(), null, null);
			}
		}
		
		if( availableBeads[currentBead].getImage() == null ){
			availableBeads[currentBead].loadImage();
		}
		
		g2d.drawImage(availableBeads[currentBead].getImage(),
				availableBeads[currentBead].getX(),
				availableBeads[currentBead].getY(), null, null);

		if (beadMustAnimate) {
			animationTimer = new Timer(ANIMATION_REFRESH_RATE, this); // every
																		// REFRESH_RATE
																		// ms
			animationTimer.setActionCommand("animation");
			animationTimer.start();
			beadMustAnimate = false;
			currentBead++;
			// creates next new bead
			availableBeads[currentBead] = new Bead(BEAD_INITIAL_X,
					BEAD_INITIAL_Y, 'b');
			availableBeads[currentBead].setImage(board.getBlackBeadImage());
		}
	}

	////////////////////////////////////////////////////////////
    // 			PRIVATE METHODS
    ////////////////////////////////////////////////////////////

	//-----------draws names on screen-----------
	private void displayNames(Graphics g) {

		// draw players names
		drawText(g, "Player 1", TEXT_P1_INITIAL_X, TEXT_P1_INITIAL_Y + 4,
				Color.green, 22);
		drawText(g, board.player1(), TEXT_P1_INITIAL_X, TEXT_P1_INITIAL_Y,
				Color.blue, 22);
		drawText(g, "Player 2", TEXT_P2_INITIAL_X, TEXT_P2_INITIAL_Y + 4,
				Color.green, 22);
		drawText(g, board.player2(), TEXT_P2_INITIAL_X, TEXT_P2_INITIAL_Y,
				Color.blue, 22);
		drawText(g, "___________", BEAD_TEXT_INITIAL_X,
				BEAD_TEXT_INITIAL_Y - 4, Color.BLACK, 17);
		drawText(g, " Free Bead", BEAD_TEXT_INITIAL_X, BEAD_TEXT_INITIAL_Y,
				Color.BLACK, 17);
	}
	
	//---------draws winner on screen--------------
	private void displayWinner( Graphics g) {
		if( winner == 1 ){
			//System.out.println( "Printing winner "  + 1 );
			drawText(g, board.player1() + " wins!", LOSE_WIN_TIE_TEXT_X, LOSE_WIN_TIE_TEXT_Y,
				Color.red, 38);
		}
		else if( winner == 2 ){
			//System.out.println( "Printing winner "  + 2 );
			drawText(g, board.player2() + " wins!", LOSE_WIN_TIE_TEXT_X, LOSE_WIN_TIE_TEXT_Y,
					Color.red, 38);
		}
		else if( winner == 0 ){
			//System.out.println( "Printing tie" );
			drawText(g, "DRAW", LOSE_WIN_TIE_TEXT_X, LOSE_WIN_TIE_TEXT_Y,
					Color.red, 38);
		}
		
	}

	//-----------draws texts on screen-----------
	private void drawText(Graphics g, String text, int absoluteX, int absoluteY,
			Color textColor, int textsize) {

		Graphics2D g2 = (Graphics2D) g;
		Dimension size = getSize();

		FontRenderContext frc = ((Graphics2D) g).getFontRenderContext();

		// set font
		Font font = new Font("Arial", Font.BOLD, textsize);
		g.setFont(font);

		// gets width and height of text
		Rectangle2D bounds = font.getStringBounds(text, frc);
		int wText = (int) bounds.getWidth();
		int hText = (int) bounds.getHeight();

		// changes absolute to relative values
		int x = (int) (((double) (size.width) / 100.0) * absoluteX); // takes
		int y = (int) (((double) (size.height) / 100.0) * absoluteY);

		g.setColor(getBackground());
		g2.fillRect(x, y, wText, hText);

		// draws string
		g.setColor(textColor);
		int xText = x - (int) bounds.getX();
		int yText = y - (int) bounds.getY();
		g.drawString(text, xText, yText);

	}

	//----------- mouse listener ------------
	private class MouseListener extends MouseAdapter {

		public void mouseDragged(MouseEvent e) {
			availableBeads[currentBead].mouseDragged(e);
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			availableBeads[currentBead].mouseReleased(e);
			repaint();

			// cehack for inserted bead (by user)
			for (int i = 0; i < 16; i++) {
				if (pegs[i].isHit(availableBeads[currentBead].getBounds())) {

					if (pegs[i].amountOfBeads2() < MAX_BEADS_PER_PEG) {

						availableBeads[currentBead].setX(pegs[i].getX()
								- (int) (availableBeads[currentBead]
										.getBounds().getWidth() / 2));
						beadMustAnimate = true;
						insertedInPegNum = i + 1;
						pegs[i].addBead(availableBeads[currentBead]);
					} else {
						// put free bead in its oinsertedInPegNumriginal palce
						availableBeads[currentBead].setX(BEAD_INITIAL_X);
						availableBeads[currentBead].setY(BEAD_INITIAL_Y);
					}
				}// end if

			}// end for

		}// end mouseReleased

	}// end class MouseListener


	//---------makes the animation for the remote player's (computer) bead---------
	private void startRemoteMoving(String position) {
		

		System.out.println( "procesing " + position ); 
		if( isWin(position) ){
		
			winner = parseWinner(position);
			if( winner ==  1 )
				startImageLoader( WIN_IMAGE_FILE );
			else{

				isWinLoseTimer = new Timer(IMAGE_LOADERS_TIMER, this);
				isWinLoseTimer.setActionCommand("is win_lose");
				isWinLoseTimer.setRepeats(false);// occurs only one time
				isWinLoseTimer.start();
				
			}
			repaint();
			return;
		}
		if( isDraw(position) ){
			winner = 0;
			startImageLoader(TIE_IMAGE_FILE);
			repaint();
			return;
		}
		
		insertedInPegNum = mapPosToPeg(position);

		availableBeads[currentBead].setX(PEG_INITIAL_X[insertedInPegNum - 1] - (int)(availableBeads[currentBead].getBounds().getWidth()/2) );
		availableBeads[currentBead].setY(PEG_INITIAL_Y[insertedInPegNum - 1]);

		pegs[insertedInPegNum - 1].addBead(availableBeads[currentBead]);

		remoteBeadAnimTimer = new Timer(ANIMATION_REFRESH_RATE, this);
		remoteBeadAnimTimer.setActionCommand("remote bead animator");

		// sets the peg
		remoteBeadAnimTimer.setRepeats(true);
		remoteBeadAnimTimer.start();
		// creates and inserts new bead
		currentBead++;

		//System.out.println( "Type of game: " + typeOfGame );
		if( typeOfGame.equals("Gertrudis vs Computer" ) ){

			if( currentBead % 2 != 0 ){
				availableBeads[currentBead] = new Bead(BEAD_INITIAL_X, BEAD_INITIAL_Y,'b');
				availableBeads[currentBead].setImage(board.getBlackBeadImage());
			}
			else{
				availableBeads[currentBead] = new Bead(BEAD_INITIAL_X, BEAD_INITIAL_Y,'w');
				availableBeads[currentBead].setImage(board.getWhiteBeadImage());
			}

		}
		else{
			availableBeads[currentBead] = new Bead(BEAD_INITIAL_X, BEAD_INITIAL_Y,'w');
			availableBeads[currentBead].setImage(board.getWhiteBeadImage());

		}
	}

	//----------- determines if a string received by referee states a tie
	private boolean isDraw(String msg) {
		
		return msg.equals("draw");
	
	}

	//----------- determines if a string received by referee states a winner
	private boolean isWin(String msg) {
		return (msg.equals("win 1") || msg.equals("win 2"));
	}
	
	//-----------parses the winner from  a string received from refereee -----------
	private int parseWinner( String msg ){
		return msg.charAt(4) - NUM_ASCII_OFFSET;
	}

	//----------- puts mesg in the ouput queue ------------
        private void sendToReferee(String msg) {
		sendQueue.add(msg);
	}

	//----------- starts worker that receives fom referee -----
	private void receiveFromReferee() throws InterruptedException {

		// the dispatch server thread cannot block until getting a response from
		// the referee, so it creates a temporal thread
		// to wait for it, and do wahtever is necessary when the msg is
		// received

		final ReceiveFromReferee msgReceiver = new ReceiveFromReferee(recvQueue);

		isFirstTime = true;

		// adds listener!
		msgReceiver.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {

				if (msgReceiver.getState() == StateValue.DONE) {

					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								// this occurs when we get response from the
								// referee
								String position = msgReceiver.get();
								 System.out.println("GUI, I got__: " + position);
								// starts the worker that moves the bead from
								// the remote player to
								// wherever the remote player decided to move

								if (isFirstTime || position.equals("win 2") || position.equals("win 1")) {
									System.out.println( "start remote moving" );
									isFirstTime = false;
									startRemoteMoving(position);

								}

								// now die
							} catch (InterruptedException e) {
								System.err.println("Couldnt display board 1 ");
							} catch (ExecutionException e) {
								System.err.println("Couldnt display board 2");
							}

						}
					});

				}

			}
		});

		// executes
		msgReceiver.execute();

	}// end receive from referee


	//--------maps coordinates into peg positions-----------
	private int mapPosToPeg(String pos) {

		return Integer.parseInt(pos);
	}
	


	//---------starts the image loader worker-----------
	//(displays the picture when its done)
	private void startImageLoader( String img_file ) {
        final LoadImage imgRetriever = new LoadImage(img_file);


        //adds listener!
        imgRetriever.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {

                if (imgRetriever.getState() == StateValue.DONE) {

                    javax.swing.SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                        	
                            try {
                                imgToShow = imgRetriever.get();
				
				System.out.println("shutting down everything" );
				shutDownEverything();

                                //SHOWS IMAGE              
                                getGraphics().drawImage(imgToShow, LOSE_WIN_TIE_IMAGE_X, LOSE_WIN_TIE_IMAGE_Y, null, null);

                            } catch (InterruptedException e) {
                                System.err.println("Couldnt display image 1 ");
                            } catch (ExecutionException e) {
                                System.err.println("Couldnt display image 2");
                            }
                        }						
                    });
                }
            }
        });

        //executes
        imgRetriever.execute();

    }//end of startIMageLoader


	//-----------stops listeners and reestablish blocked menues-----------
       private void shutDownEverything() {
          this.removeMouseListener(mouseListener);
          this.removeMouseMotionListener(mouseListener);
        
          menuBar.getMenu(0).getMenuComponent(0).setEnabled(false);// keep everything disable (for now)
          menuBar.getMenu(0).getMenuComponent(1).setEnabled(false);
        }


	////////////////////////////////////////////////////////////
    // 			INTERFACE
    ////////////////////////////////////////////////////////////
	public void restart(){
		beadMustAnimate = false;
		isFirstTransaction = true;
		insertedInPegNum = 0;
		currentBead = 0;
		for (int i = 0; i < 16; i++) {
			pegs[i] = new Peg(PEG_INITIAL_X[i], PEG_INITIAL_Y[i]);
		}
	}

	public void setGertrudeVsComputer(){
		typeOfGame = "Gertrudis vs Computer" ;
		typeOfGameTimer = new Timer(IMAGE_LOADERS_TIMER, this);
		typeOfGameTimer.setActionCommand("Gertrude vs Computer");
		typeOfGameTimer.setRepeats(false);// occurs only one time
		typeOfGameTimer.start();
	}

}
