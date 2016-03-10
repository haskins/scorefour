/**
 * This class is called by the event dispatcher thread to display the board, 
 * it also manages the pegs.
 * 
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

package board;

import java.awt.Image;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker.StateValue;
import board.Peg;
import gui.LoadImage;

public class Board {
	private Peg[] position;
	private Image image;
	private Image beadImageW;
	private Image beadImageB;
	private final String FILE_NAME_WHITE = "bead_white.png";
	private final String FILE_NAME_BLACK = "bead_black.png";
	private int x;
	private int y;
	private int width;
	private int height;
	private String player1Name;
	private String player2Name;
	private boolean isWin;
	private boolean isDraw;
	private final int NUM_ALPHA_OFFSET = 16;
	private final int NUM_COLS = 4;
	private final String FILE_NAME = "board.png";

	/**
	 * Construct a board consisting of 16 pegs.
	 */
	public Board() {
		// Create the board and initializes all pegs.
		position = new Peg[16];
		for (int i = 0; i < 16; i++) {
			position[i] = new Peg();
		}
		this.width = 0;
		this.height = 0;
	}

	/**
	 * Construct a board consisting at a specific pair of coordinates of 16
	 * pegs.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public Board(int x, int y) {
		// creates the board and initalizes all pegs
		position = new Peg[16];
		for (int i = 0; i < 16; i++) {
			position[i] = new Peg();
		}

		this.width = 0;
		this.height = 0;
		this.x = x;
		this.y = y;
		startImageLoader();
		loadBeadImages();
	}

	/**
	 * Loads the images of the white and black beads.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public void loadBeadImages() {
		// System.out.println("Loading Bead Image...");
		startBeadImageLoaderW();
		startBeadImageLoaderB();
	}

	/**
	 * Gets the image of that the white beads have.
	 * 
	 * @return The image used for white beads.
	 */
	public Image getWhiteBeadImage() {
		return beadImageW;
	}

	/**
	 * Gets the image of that the black beads have.
	 * 
	 * @return The image used for black beads.
	 */
	public Image getBlackBeadImage() {
		return beadImageB;
	}

	/**
	 * Set the coordinate of a board.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public void setBoardCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Loads an image.
	 */
	public void loadImage() {
		startImageLoader();
	}

	/**
	 * Returns an x coordinate.
	 * 
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns a y coordinate.
	 * 
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets an image.
	 * 
	 * @return The image.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Used to load an image.
	 */
	public void startImageLoader() {
		final LoadImage imgRetriever = new LoadImage(FILE_NAME);

		// Adds a listener.
		imgRetriever.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (imgRetriever.getState() == StateValue.DONE) {
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								image = imgRetriever.get();
								width = image.getWidth(null);
								height = image.getHeight(null);
							} catch (InterruptedException e) {
								System.err
										.println("Couldnt display board - error 1 ");
								e.printStackTrace();

							} catch (ExecutionException e) {
								System.err
										.println("Couldnt display board - error 2");
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		imgRetriever.execute();
	}

	/**
	 * Used to load the image of white beads.
	 */
	public void startBeadImageLoaderW() {
		final LoadImage imgRetriever;
		imgRetriever = new LoadImage(FILE_NAME_WHITE);

		// starts the image loader (thread)
		// adds listener!
		imgRetriever.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (imgRetriever.getState() == StateValue.DONE) {
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								// loads the images
								beadImageW = imgRetriever.get();
								width = beadImageW.getWidth(null);
								height = beadImageW.getHeight(null);
							} catch (InterruptedException e) {
								System.err
										.println("Couldnt display board - error 1 ");
								e.printStackTrace();
							} catch (ExecutionException e) {
								System.err
										.println("Couldnt display board - error 2");
								e.printStackTrace();
							} catch (Exception e) {
								System.err
										.println("Couldnt display board - error 3"
												+ e.getLocalizedMessage());
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		imgRetriever.execute();
	}

	/**
	 * Used to load the image of black beads.
	 */
	public void startBeadImageLoaderB() {
		final LoadImage imgRetriever;
		imgRetriever = new LoadImage(FILE_NAME_BLACK);

		// System.out.println(
		// " WE ARE ABOUT TO START THE IMAGE LOADER (THREAD)!!!! " );

		// adds listener!
		imgRetriever.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				// System.out.println( " INSIDE PROPERTY CHANGE" );
				if (imgRetriever.getState() == StateValue.DONE) {
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								// System.out.println(
								// " THE IMAGE LOADER (THREAD) IS DONE!!!!!!!! "
								// );
								beadImageB = imgRetriever.get();
								width = beadImageB.getWidth(null);
								height = beadImageB.getHeight(null);
								// System.out.println (
								// "!!!!!!IMage was loaded, here is the proof:"
								// + image.toString() );
								// System.out.println ( "width: " + width );
								// System.out.println ( "height: " + height );

							} catch (InterruptedException e) {
								System.err
										.println("Couldnt display board - error 1 ");
								e.printStackTrace();
							} catch (ExecutionException e) {
								System.err
										.println("Couldnt display board - error 2");
								e.printStackTrace();
							} catch (Exception e) {
								System.err
										.println("Couldnt display board - error 3"
												+ e.getLocalizedMessage());
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		imgRetriever.execute();
	}

	/**
	 * Used to get the boundaries.
	 * 
	 * @return The boundaries.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Sets a bead on a peg.
	 * 
	 * @param number
	 *            The number of the peg to set the bead on.
	 * @param c
	 *            The colour of the bead.
	 * @return Whether or not the move is legal.
	 */
	public boolean setPeg(int number, char c) {
		// Turns the input into an array-friendly format.
		number = number - 1;

		// Make sure that the bead will fit on the peg
		if (position[number].beadAt(0).colour() == 'N'
				|| position[number].beadAt(1).colour() == 'N'
				|| position[number].beadAt(2).colour() == 'N'
				|| position[number].beadAt(3).colour() == 'N') {
			position[number].setBead(c);

			// System.out.println("bead placed with colour" + c);
			return true;
		}

		else
			return false;
	}

	/**
	 * Get whether or not a player has won.
	 * 
	 * @return If a player has won, return true.
	 */
	public boolean win() {
		return isWin;
	}

	/**
	 * Get whether or not there is a draw.
	 * 
	 * @return If there is a draw, return true.
	 */
	public boolean draw() {
		return isDraw;
	}

	/**
	 * Get a particular bead on a specified peg.
	 * 
	 * @param pos
	 *            The peg to get a bead from.
	 * @param numBead
	 *            The position of the bead on the peg.
	 * @return The particular bead.
	 */
	public Bead getBead(String pos, int numBead) {
		int selectedPeg = mapPosToPeg(pos);
		return position[selectedPeg].beadAt(numBead);
	}

	/**
	 * Map the positions to the pegs.
	 * 
	 * @param pos
	 *            The peg to map to.
	 */
	private int mapPosToPeg(String pos) {
		char colAlpha = pos.charAt(0);
		int row = pos.charAt(1);
		int colNum = 0;

		if (colAlpha < 65 || colAlpha > 68 || row < 1 || row > 4) {
			// trhow exceptino
			// input out of range
		}

		// converts row to number
		colNum = colAlpha - NUM_ALPHA_OFFSET;

		return row + NUM_COLS + colNum;
	}

	public void setPlayerNames(String name1, String name2) {
		player1Name = name1;
		player2Name = name2;
	}

	public String player1() {
		return player1Name;
	}

	public String player2() {
		return player2Name;
	}

	public Peg[] getPosition() {
		return position;
	}
}
