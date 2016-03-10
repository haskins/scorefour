/**
 * This program will check to see if either there is a win or a draw.
 *
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant,Thanh Minh Vo
 * @version 03-15-2012
 */
package board;

public class Line {

	/**
	 * Checks all possible lines to see if there is a win.
	 * 
	 * @return true or false depending on win or not
	 */
	public static boolean checkForWin(Peg[] peg) {

		// name: 1-peg
		// checks 1 peg to see if it is full of one colour
		// repeats 16 times, checking all peg
		for (int i = 0; i < 16; i++) {
			if (peg[i].beadAt(0).colour() != 'N'
					&& peg[i].beadAt(0).colour() == peg[i].beadAt(1).colour()
					&& peg[i].beadAt(2).colour() == peg[i].beadAt(3).colour()
					&& peg[i].beadAt(0).colour() == peg[i].beadAt(3).colour()) {
				System.out.println("case 1");
				return true;
			}
		}

		// name: verti-line
		// checks for virticle lines of one colour
		// repeats 4 times per column, a total of 16 times
		for (int a = 0; a < 4; a++) {
			for (int b = 0; b < 4; b++) {
				if (peg[a].beadAt(b).colour() != 'N'
						&& peg[a].beadAt(b).colour() == peg[a + 4].beadAt(b)
								.colour()
						&& peg[a + 8].beadAt(b).colour() == peg[a + 12].beadAt(
								b).colour()
						&& peg[a + 4].beadAt(b).colour() == peg[a + 8]
								.beadAt(b).colour()) {
					System.out.println("case 2");
					return true;
				}
			}
		}

		// name: hori-line
		// checks for horizontal lines of one colour
		// repeats 4 times per column, a total of 16 times
		for (int b = 0; b < 4; b++) {
			if ((peg[0].beadAt(b).colour() != 'N'
					&& peg[0].beadAt(b).colour() == peg[1].beadAt(b).colour()
					&& peg[2].beadAt(b).colour() == peg[3].beadAt(b).colour() && peg[1]
					.beadAt(b).colour() == peg[2].beadAt(b).colour())
					|| (peg[4].beadAt(b).colour() != 'N'
							&& peg[4].beadAt(b).colour() == peg[5].beadAt(b)
									.colour()
							&& peg[6].beadAt(b).colour() == peg[7].beadAt(b)
									.colour() && peg[5].beadAt(b).colour() == peg[6]
							.beadAt(b).colour())
					|| (peg[8].beadAt(b).colour() != 'N'
							&& peg[8].beadAt(b).colour() == peg[9].beadAt(b)
									.colour()
							&& peg[10].beadAt(b).colour() == peg[11].beadAt(b)
									.colour() && peg[9].beadAt(b).colour() == peg[10]
							.beadAt(b).colour())
					|| (peg[12].beadAt(b).colour() != 'N'
							&& peg[12].beadAt(b).colour() == peg[13].beadAt(b)
									.colour()
							&& peg[14].beadAt(b).colour() == peg[15].beadAt(b)
									.colour() && peg[13].beadAt(b).colour() == peg[14]
							.beadAt(b).colour())) {

				System.out.println("case 3");
				return true;
			}
		}

		// name: diagonal-x-top
		// checks for diagonal lines on top view
		// repeats 4 times per diagonal strip, a total of 8 times
		for (int b = 0; b < 4; b++) {
			if ((peg[0].beadAt(b).colour() != 'N'
					&& peg[0].beadAt(b).colour() == peg[5].beadAt(b).colour()
					&& peg[10].beadAt(b).colour() == peg[15].beadAt(b).colour() && peg[5]
					.beadAt(b).colour() == peg[10].beadAt(b).colour())
					|| (peg[3].beadAt(b).colour() != 'N'
							&& peg[3].beadAt(b).colour() == peg[6].beadAt(b)
									.colour()
							&& peg[9].beadAt(b).colour() == peg[12].beadAt(b)
									.colour() && peg[6].beadAt(b).colour() == peg[9]
							.beadAt(b).colour())) {
				System.out.println("case 4");
				return true;
			}
		}

		// name: diagonal-x-side
		// checks for diagonal lines on side view
		// repeats 2 times per set, a total of 8 times
		for (int a = 0; a < 13; a = a + 4) {
			if ((peg[a].beadAt(3).colour() != 'N'
					&& peg[a].beadAt(3).colour() == peg[a + 1].beadAt(2)
							.colour()
					&& peg[a + 2].beadAt(1).colour() == peg[a + 3].beadAt(0)
							.colour() && peg[a + 1].beadAt(2).colour() == peg[a + 2]
					.beadAt(1).colour())
					|| (peg[a].beadAt(0).colour() != 'N'
							&& peg[a].beadAt(0).colour() == peg[a + 1]
									.beadAt(1).colour()
							&& peg[a + 2].beadAt(2).colour() == peg[a + 3]
									.beadAt(3).colour() && peg[a + 1].beadAt(1)
							.colour() == peg[a + 2].beadAt(2).colour())) {
				System.out.println("case 5");
				return true;
			}
		}

		// name: diagonal-x-angle
		// checks for diagonal lines on angle
		// repeats 2 times per diagonal strip, a total of 4 times
		if ((peg[0].beadAt(0).colour() != 'N'
				&& peg[0].beadAt(0).colour() == peg[5].beadAt(1).colour()
				&& peg[10].beadAt(2).colour() == peg[15].beadAt(3).colour() && peg[5]
				.beadAt(1).colour() == peg[10].beadAt(2).colour())
				|| (peg[0].beadAt(3).colour() != 'N'
						&& peg[0].beadAt(3).colour() == peg[5].beadAt(2)
								.colour()
						&& peg[10].beadAt(1).colour() == peg[15].beadAt(0)
								.colour() && peg[5].beadAt(2).colour() == peg[10]
						.beadAt(1).colour())
				|| (peg[3].beadAt(0).colour() != 'N'
						&& peg[3].beadAt(0).colour() == peg[6].beadAt(1)
								.colour()
						&& peg[9].beadAt(2).colour() == peg[12].beadAt(3)
								.colour() && peg[6].beadAt(1).colour() == peg[9]
						.beadAt(2).colour())
				|| (peg[3].beadAt(3).colour() != 'N'
						&& peg[3].beadAt(3).colour() == peg[6].beadAt(2)
								.colour()
						&& peg[9].beadAt(1).colour() == peg[12].beadAt(0)
								.colour() && peg[6].beadAt(2).colour() == peg[9]
						.beadAt(1).colour())) {
			System.out.println("case 6");
			return true;
		}

		// name: diagonal-x-otherside
		// checks for diagonal lines on otherside view
		// repeats 2 times per set, a total of 8 times
		for (int a = 0; a < 4; a++) {
			if ((peg[a].beadAt(3).colour() != 'N'
					&& peg[a].beadAt(3).colour() == peg[a + 4].beadAt(2)
							.colour()
					&& peg[a + 8].beadAt(1).colour() == peg[a + 12].beadAt(0)
							.colour() && peg[a + 4].beadAt(2).colour() == peg[a + 8]
					.beadAt(1).colour())
					|| (peg[a].beadAt(0).colour() != 'N'
							&& peg[a].beadAt(0).colour() == peg[a + 4]
									.beadAt(1).colour()
							&& peg[a + 8].beadAt(2).colour() == peg[a + 12]
									.beadAt(3).colour() && peg[a + 4].beadAt(1)
							.colour() == peg[a + 8].beadAt(2).colour())) {
				System.out.println("case 7");
				return true;
			}
		}

		// if no lines, returns false
		return false;
	}

	/**
	 * Checks to see if there are any available moves to be made that could
	 * result in a win.
	 * 
	 * @return true or false depending on draw or not
	 */
	public static boolean checkForDraw(Peg peg[]) {
		// initializes the count as zero
		int count = 0;

		// checks peg to see if cannot win
		for (int a = 0; a < 16; a++) {
			if ((peg[a].beadAt(0).colour() == 'W'
					|| peg[a].beadAt(1).colour() == 'W'
					|| peg[a].beadAt(2).colour() == 'W' || peg[a].beadAt(3)
					.colour() == 'W')
					&& (peg[a].beadAt(0).colour() == 'B'
							|| peg[a].beadAt(1).colour() == 'B'
							|| peg[a].beadAt(2).colour() == 'B' || peg[a]
							.beadAt(3).colour() == 'B')) {
				count++;
			}
		}

		// checks horizonal lines
		for (int a = 0; a < 16; a = a + 4) {
			for (int b = 0; b < 4; b++) {
				if ((peg[a].beadAt(b).colour() == 'W'
						|| peg[a + 1].beadAt(b).colour() == 'W'
						|| peg[a + 2].beadAt(b).colour() == 'W' || peg[a + 3]
						.beadAt(b).colour() == 'W')
						&& (peg[a].beadAt(b).colour() == 'B'
								|| peg[a + 1].beadAt(b).colour() == 'B'
								|| peg[a + 2].beadAt(b).colour() == 'B' || peg[a + 3]
								.beadAt(b).colour() == 'B')) {
					count++;
				}
			}
		}

		// checks verticle lines
		for (int a = 0; a < 4; a++) {
			for (int b = 0; b < 4; b++) {
				if ((peg[a].beadAt(b).colour() == 'W'
						|| peg[a + 4].beadAt(b).colour() == 'W'
						|| peg[a + 8].beadAt(b).colour() == 'W' || peg[a + 12]
						.beadAt(b).colour() == 'W')
						&& (peg[a].beadAt(b).colour() == 'B'
								|| peg[a + 4].beadAt(b).colour() == 'B'
								|| peg[a + 8].beadAt(b).colour() == 'B' || peg[a + 12]
								.beadAt(b).colour() == 'B')) {
					count++;
				}
			}
		}
		// checks diagonal-x-top lines
		for (int b = 0; b < 4; b++) {
			if (((peg[0].beadAt(b).colour() == 'W'
					|| peg[5].beadAt(b).colour() == 'W'
					|| peg[10].beadAt(b).colour() == 'W' || peg[15].beadAt(b)
					.colour() == 'W') && (peg[0].beadAt(b).colour() == 'B'
					|| peg[5].beadAt(b).colour() == 'B'
					|| peg[10].beadAt(b).colour() == 'B' || peg[15].beadAt(b)
					.colour() == 'B'))) {
				count++;
			}

			if (((peg[3].beadAt(b).colour() == 'W'
					|| peg[6].beadAt(b).colour() == 'W'
					|| peg[9].beadAt(b).colour() == 'W' || peg[12].beadAt(b)
					.colour() == 'W') && (peg[3].beadAt(b).colour() == 'B'
					|| peg[6].beadAt(b).colour() == 'B'
					|| peg[9].beadAt(b).colour() == 'B' || peg[12].beadAt(b)
					.colour() == 'B'))) {
				count++;
			}
		}

		// checks diagonal-x-side lines
		for (int a = 0; a < 4; a++) {
			if (((peg[a].beadAt(3).colour() == 'W'
					|| peg[a + 1].beadAt(2).colour() == 'W'
					|| peg[a + 2].beadAt(1).colour() == 'W' || peg[a + 3]
					.beadAt(0).colour() == 'W') && (peg[a].beadAt(3).colour() == 'B'
					|| peg[a + 1].beadAt(2).colour() == 'B'
					|| peg[a + 2].beadAt(1).colour() == 'B' || peg[a + 3]
					.beadAt(0).colour() == 'B'))) {
				count++;
			}

			if (((peg[a].beadAt(0).colour() == 'W'
					|| peg[a + 1].beadAt(1).colour() == 'W'
					|| peg[a + 2].beadAt(2).colour() == 'W' || peg[a + 3]
					.beadAt(3).colour() == 'W') && (peg[a].beadAt(0).colour() == 'B'
					|| peg[a + 1].beadAt(1).colour() == 'B'
					|| peg[a + 2].beadAt(2).colour() == 'B' || peg[a + 3]
					.beadAt(3).colour() == 'B'))) {
				count++;
			}
		}

		// checks diagonal-x-otherside lines
		for (int a = 0; a < 4; a++) {
			if (((peg[a].beadAt(3).colour() == 'W'
					|| peg[a + 4].beadAt(2).colour() == 'W'
					|| peg[a + 8].beadAt(1).colour() == 'W' || peg[a + 12]
					.beadAt(0).colour() == 'W') && (peg[a].beadAt(3).colour() == 'B'
					|| peg[a + 4].beadAt(2).colour() == 'B'
					|| peg[a + 8].beadAt(1).colour() == 'B' || peg[a + 12]
					.beadAt(0).colour() == 'B'))) {
				count++;
			}

			if (((peg[a].beadAt(0).colour() == 'W'
					|| peg[a + 4].beadAt(1).colour() == 'W'
					|| peg[a + 8].beadAt(2).colour() == 'W' || peg[a + 12]
					.beadAt(3).colour() == 'W') && (peg[a].beadAt(0).colour() == 'B'
					|| peg[a + 4].beadAt(1).colour() == 'B'
					|| peg[a + 8].beadAt(2).colour() == 'B' || peg[a + 12]
					.beadAt(3).colour() == 'B'))) {
				count++;
			}

		}

		// checks diagonal-x-angle lines
		for (int a = 0; a < 4; a++) {
			if (((peg[0].beadAt(3).colour() == 'W'
					|| peg[5].beadAt(2).colour() == 'W'
					|| peg[10].beadAt(1).colour() == 'W' || peg[15].beadAt(0)
					.colour() == 'W') && (peg[0].beadAt(3).colour() == 'B'
					|| peg[5].beadAt(2).colour() == 'B'
					|| peg[10].beadAt(1).colour() == 'B' || peg[15].beadAt(0)
					.colour() == 'B'))) {
				count++;
			}

			if (((peg[0].beadAt(0).colour() == 'W'
					|| peg[5].beadAt(1).colour() == 'W'
					|| peg[10].beadAt(2).colour() == 'W' || peg[15].beadAt(3)
					.colour() == 'W') && (peg[0].beadAt(0).colour() == 'B'
					|| peg[5].beadAt(1).colour() == 'B'
					|| peg[10].beadAt(2).colour() == 'B' || peg[15].beadAt(3)
					.colour() == 'B'))) {
				count++;
			}

			if (((peg[3].beadAt(3).colour() == 'W'
					|| peg[6].beadAt(2).colour() == 'W'
					|| peg[9].beadAt(1).colour() == 'W' || peg[12].beadAt(0)
					.colour() == 'W') && (peg[3].beadAt(3).colour() == 'B'
					|| peg[6].beadAt(2).colour() == 'B'
					|| peg[9].beadAt(1).colour() == 'B' || peg[12].beadAt(0)
					.colour() == 'B'))) {
				count++;
			}

			if (((peg[3].beadAt(0).colour() == 'W'
					|| peg[6].beadAt(1).colour() == 'W'
					|| peg[9].beadAt(2).colour() == 'W' || peg[12].beadAt(3)
					.colour() == 'W') && (peg[3].beadAt(0).colour() == 'B'
					|| peg[6].beadAt(1).colour() == 'B'
					|| peg[9].beadAt(2).colour() == 'B' || peg[12].beadAt(3)
					.colour() == 'B'))) {
				count++;
			}
		}

		// if equals 76 means no moves available that could result in a win
		if (count == 76) {
			return true;
		} // else there are still moves that could result in a win
		else {
			return false;
		}
	}
}