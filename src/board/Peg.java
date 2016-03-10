/**
 * This program manages the beads.
 *
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

package board;

import gui.LoadImage;
import java.awt.Image;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker.StateValue;
import java.io.*;

public class Peg implements Serializable {

	private Bead[] position;
	private Image image;
	private int x;
	private int y;
	private int width;
	private int height;
	private final int DEFINED_HEIGHT_FOR_COLLISION = 10;
	private final String FILE_NAME = "peg.png"; // sets the image for a peg
	private int numBeads;

	public Peg() {
		position = new Bead[4];
		for (int i = 0; i < 4; i++) {
			position[i] = new Bead('N');
		}
	}

	public Peg(int x, int y) {
		position = new Bead[4];
		for (int i = 0; i < 4; i++) {
			position[i] = new Bead('N');
		}

		this.x = x;
		this.y = y;
		loadImage();
	}

	public void setCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void loadImage() {
		startImageLoader();
	}

	public Bead beadAt(int pos) {
		return position[pos];
	}

	public int amountOfBeads() {

		int number = 0;

		for (int i = 0; i < 4; i++) {
			if (position[i].colour() != 'N') {
				number = number + 1;
			}
		}
		return number;
	}

	public int amountOfBeads2() {
		return numBeads;
	}

	public void setBead(char c) {
		if (position[0].colour() == 'N') {
			position[0] = new Bead(c);
		} else if (position[1].colour() == 'N') {
			position[1] = new Bead(c);
		} else if (position[2].colour() == 'N') {
			position[2] = new Bead(c);
		} else if (position[3].colour() == 'N') {
			position[3] = new Bead(c);
		} else {
			System.out.println("setBead failed");
		}
	}

	public void addBead(Bead bead) {
		if (amountOfBeads() < 4) {
			int newPos = numBeads;
			position[newPos] = bead;
			numBeads++;
		} else {
			// throw exception
			// pegs can only have 4 beads
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Image getImage() {
		return image;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public Rectangle getBoundsForCollision() {
		return new Rectangle(x, y, width, DEFINED_HEIGHT_FOR_COLLISION);
	}

	public void startImageLoader() {
		final LoadImage imgRetriever = new LoadImage(FILE_NAME);

		// adds listener!
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
							} catch (ExecutionException e) {
								System.err.println("Couldnt display board 2");
							}
						}
					});
				}
			}
		});

		// executes
		imgRetriever.execute();
	}

	public boolean isHit(Rectangle otherObject) {
		Rectangle myself = getBoundsForCollision();

		if (myself.intersects(otherObject)) {
			return true;
		} else {
			return false;
		}
	}
}
