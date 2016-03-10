/**
 * These are the beads the go on the pegs.
 * 
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

package board;

import gui.LoadImage;
import java.awt.Image;
import javax.swing.SwingWorker.StateValue;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import java.io.*;

public class Bead implements Serializable {

	private static final long serialVersionUID = 1;
	private LoadImage imgRetriever;
	private char colour;
	private Image image;
	private int x;
	private int y;
	private int width;
	private int height;
	private int dx;
	private int dy;
	private boolean isFirstClick;
	private boolean draggingInProgress;
	private final String FILE_NAME_WHITE = "bead_white.png";
	private final String FILE_NAME_BLACK = "bead_black.png";

	/**
	 * Construct a coloured bead.
	 * 
	 * @param c
	 *            The colour to make the bead.
	 */
	public Bead(char c) {
		imgRetriever = null;
		this.colour = c;
		isFirstClick = true;
		draggingInProgress = false;
	}

	/**
	 * Construct a coloured bead at a certain position.
	 * 
	 * @param x
	 *            The x coordinate of the new bead.
	 * @param y
	 *            The y coordinate of the new bead.
	 * @param c
	 *            The colour to make the bead.
	 */
	public Bead(int x, int y, char c) {
		imgRetriever = null;
		this.x = x;
		this.y = y;
		this.colour = c;
		isFirstClick = true;
		draggingInProgress = false;
		width = 50;
		height = 50;
	}

	/**
	 * Load an image.
	 */
	public void loadImage() {
		startImageLoader();
	}

	/**
	 * Set an image.
	 * 
	 * @param image
	 *            The image to set.
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * Gets the colour of a bead, either White, Black, or None.
	 * 
	 * @return The colour of the bead.
	 */
	public char colour() {
		if (!(colour == 'W') && !(colour == 'B'))
			return 'N';
		return colour;
	}

	/**
	 * Moves a bead.
	 */
	public void move() {
		x = x + dx;
		y = y + dy;
	}

	/**
	 * Animate a bead.
	 */
	public void animation(int animationStep) {
		y = y + animationStep;
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
	 * Sets an x coordinate.
	 * 
	 * @param x
	 *            The x coordinate to set.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets a y coordinate.
	 * 
	 * @param y
	 *            The y coordinate to set.
	 */
	public void setY(int y) {
		this.y = y;
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
	 * An event used for dragging the mouse.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	public void mouseDragged(MouseEvent e) {
		Rectangle rect = new Rectangle();
		rect.setBounds(x, y, width - 1, height - 1);

		if (rect.contains(e.getPoint()) && isFirstClick) {
			dx = e.getX() - x - width / 2;
			dy = e.getY() - y - height / 2;
			isFirstClick = false;
			draggingInProgress = true;
		}

		else if (draggingInProgress) {
			dx = e.getX() - x - width / 2;
			dy = e.getY() - y - height / 2;
		}

		else {
			dx = 0;
			dy = 0;
		}
		move();
	}

	/**
	 * An event used for releasing the mouse.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	public void mouseReleased(MouseEvent e) {
		isFirstClick = true;
		draggingInProgress = false;
	}

	/**
	 * Used to load an image.
	 */
	public void startImageLoader() {
		if (colour == 'w' || colour == 'W')
			imgRetriever = new LoadImage(FILE_NAME_WHITE);
		else
			imgRetriever = new LoadImage(FILE_NAME_BLACK);

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
								System.err.println("Couldnt display board 1 ");
								e.printStackTrace();
							} catch (ExecutionException e) {
								System.err.println("Couldnt display board 2");
								e.printStackTrace();
							} catch (Exception e) {
								System.err.println("Couldnt display board 3"
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
	 * Adds to an x coordinate.
	 * 
	 * @param x
	 *            The value to add to the x coordinate.
	 */
	public void addX(int x) {
		this.x += x;
	}

	/**
	 * Adds to a y coordinate.
	 * 
	 * @param y
	 *            The value to add to the y coordinate.
	 */
	public void addY(int y) {
		this.y += y;
	}
}