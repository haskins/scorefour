/**
 * This program will manage the Scoreboard.
 *
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant,
 * Thanh Minh Vo
 * @version 03-15-2012
 */
package scoreboard;

// Needed for Scanner class
import java.util.*;

// Needed for Exceptions.
import java.io.*;

public class Scoreboard {

	/**
	 * Sets the score.
	 * 
	 * @param score
	 *            number value of score
	 * @param name
	 *            users' name
	 * 
	 */
	public static void setScore(int score, String name) {
		try {
			// Create file
			FileWriter fstream = new FileWriter("scores.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);

			// writes name and score to file, then goes to a new line
			out.write(name + " " + score);
			out.newLine();

			// Close the output stream
			out.close();
		} catch (Exception e) {
			System.out.println("Error - Screwed up in writing to file.");
		}
	}

	/**
	 * Displays the scores on screen.
	 * 
	 */
	static void displaySB() throws FileNotFoundException, IOException,
			NoSuchElementException {
		System.out.println("The highscores are:");

		// counts the lines in the file
		BufferedReader reader = new BufferedReader(new FileReader("scores.txt"));
		int count = 0;
		while (reader.readLine() != null) {
			count++;
		}
		reader.close();

		// creates new scanner for scanning into array
		Scanner scan = new Scanner(new File("scores.txt"));
		int i = 0;
		Score[] scores = new Score[count];
		String name;
		int score;
		// scans in the lines and counts them
		while (i < count) {
			name = scan.next();
			score = scan.nextInt();
			scores[i] = new Score(name, score);
			i++;
		}

		// sorts the arrays based on scores
		Arrays.sort(scores);

		// prints all the scores on screen
		int x = 0;
		for (Score temp : scores) {
			System.out.println(++x + ": " + temp.getQuantity() + " "
					+ temp.getName());
		}
	}

	static public String getScores() {

		StringBuilder output = new StringBuilder();
		output.append("The highscores are:");
		output.append("\n");
		output.append("\n");

		try {
			// counts the lines in the file
			BufferedReader reader = new BufferedReader(new FileReader(
					"scores.txt"));
			int count = 0;
			while (reader.readLine() != null) {
				count++;
			}
			reader.close();

			// creates new scanner for scanning into array
			Scanner scan = new Scanner(new File("scores.txt"));
			int i = 0;
			Score[] scores = new Score[count];
			String name;
			int score;
			// scans in lines and counts them
			while (i < count) {
				name = scan.next();
				score = scan.nextInt();
				scores[i] = new Score(name, score);
				i++;
			}

			// sorts the arrays
			Arrays.sort(scores);

			// prints the top 25 scores on screen
			int top = count;
			// makes sure to only display 25 scores or what is available
			if (top > 25) {
				top = 25;
			}
			for (int index = 0; index < top; index++) {
				output.append(index + 1 + ": " + scores[index].getName() + " "
						+ scores[index].getQuantity());
				output.append("\n");
			}
			output.append("\n");
		} catch (Exception e) {
		}

		return output.toString();
	}
}
