/**
 * Holds a Score, which is a name and score value.
 * 
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

package scoreboard;

public class Score implements Comparable<Score> {

	private String name;
	private int quantity;

	public Score(String name, int quantity) {
		super();
		this.name = name;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int compareTo(Score compareScore) {
		int compareQuantity = ((Score) compareScore).getQuantity();
		// returns scores in ascending order
		return this.quantity - compareQuantity;
	}
}