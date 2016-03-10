/**
 * This is the Computer AI for Double Double's Supreme Four Score.
 *
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

import java.util.Scanner;
import java.util.Random;
import board.*;
import java.io.*;

public class Gertrude {

	private static FileInputStream fis;
	private static ObjectInputStream ois;
	private static ObjectInputStream input;

	/**
	 * Generates a random number as the computer's move.
	 * 
	 * @return The computer's move that was randomly generated.
	 */
	public static int randomNumber() {
		return (new Random().nextInt(15) + 1);
	}

	/**
	 * Counts number of given chars in a string.
	 * 
	 * @return The amount of chars in the string.
	 */
	public static int countChars(String line, char c) {
		int count = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Not the smartest AI.
	 * 
	 * @return The computer's move that was randomly generated.
	 */
	public static int dumbo(Peg[] p) {
		return randomNumber();
	}

	/**
	 * Uses thinker(), but only looks for moves that will make 4 in a row.
	 * 
	 * @return The computer's move.
	 */
	public static int average(Peg[] p) throws java.io.IOException {
		return thinker(p, 3);
	}

	/**
	 * Uses thinker(), but only looks for moves that will make 3 or 4 in a row.
	 * 
	 * @return The computer's move.
	 */
	public static int smarter(Peg[] p) throws java.io.IOException {
		return thinker(p, 2);
	}

	/**
	 * Uses thinker(), but only looks for moves that will make 2, 3, or 4 in a
	 * row.
	 * 
	 * @return The computer's move.
	 */
	public static int gertrude(Peg[] p) throws java.io.IOException {
		return thinker(p, 1);
	}

	/**
	 * Analysis the board, looks for the best move. If cannot find a good one,
	 * generates a random number.
	 * 
	 * @return The computer's move.
	 */
	public static int thinker(Peg[] peg, int smartLevel)
			throws java.io.IOException {
		// looks for 3 in a row, then 2, then finally one(to either block
		// opponent or add to existing line)
		for (int z = 3; z >= smartLevel; z--) {

			// checks pegs
			for (int i = 0; i < 16; i++) {
				if (((countChars(peg[i].beadAt(0).colour() + ""
						+ peg[i].beadAt(1).colour() + ""
						+ peg[i].beadAt(2).colour() + ""
						+ peg[i].beadAt(3).colour(), 'W') >= z) && (countChars(
						peg[i].beadAt(0).colour() + ""
								+ peg[i].beadAt(1).colour() + ""
								+ peg[i].beadAt(2).colour() + ""
								+ peg[i].beadAt(3).colour(), 'B') == 0))
						|| ((countChars(peg[i].beadAt(0).colour() + ""
								+ peg[i].beadAt(1).colour() + ""
								+ peg[i].beadAt(2).colour() + ""
								+ peg[i].beadAt(3).colour(), 'B') >= z) && (countChars(
								peg[i].beadAt(0).colour() + ""
										+ peg[i].beadAt(1).colour() + ""
										+ peg[i].beadAt(2).colour() + ""
										+ peg[i].beadAt(3).colour(), 'W') == 0))) {
					return i;
				}
			}

			// check across
			for (int i = 0; i < 16; i = i + 4) {
				for (int x = 0; x < 4; x++) {
					if (((countChars(peg[i].beadAt(x).colour() + ""
							+ peg[i + 1].beadAt(x).colour() + ""
							+ peg[i + 2].beadAt(x).colour() + ""
							+ peg[i + 3].beadAt(x).colour(), 'W') >= z) && (countChars(
							peg[i].beadAt(x).colour() + ""
									+ peg[i + 1].beadAt(x).colour() + ""
									+ peg[i + 2].beadAt(x).colour() + ""
									+ peg[i + 3].beadAt(x).colour(), 'B') == 0))
							|| ((countChars(peg[i].beadAt(x).colour() + ""
									+ peg[i + 1].beadAt(x).colour() + ""
									+ peg[i + 2].beadAt(x).colour() + ""
									+ peg[i + 3].beadAt(x).colour(), 'B') >= z) && (countChars(
									peg[i].beadAt(x).colour() + ""
											+ peg[i + 1].beadAt(x).colour()
											+ ""
											+ peg[i + 2].beadAt(x).colour()
											+ ""
											+ peg[i + 3].beadAt(x).colour(),
									'W') == 0))) {
						if (x == 0) {
							if (peg[i].beadAt(x).colour() == 'N') {

								return i;
							} else if (peg[i + 1].beadAt(x).colour() == 'N') {

								return i + 1;
							} else if (peg[i + 2].beadAt(x).colour() == 'N') {

								return i + 2;
							} else if (peg[i + 3].beadAt(x).colour() == 'N') {

								return i + 3;
							}
						} else {
							if (peg[i].beadAt(x).colour() == 'N'
									&& peg[i].beadAt(x - 1).colour() != 'N') {

								return i;
							} else if (peg[i + 1].beadAt(x).colour() == 'N'
									&& peg[i + 1].beadAt(x - 1).colour() != 'N') {

								return i + 1;
							} else if (peg[i + 2].beadAt(x).colour() == 'N'
									&& peg[i + 2].beadAt(x - 1).colour() != 'N') {

								return i + 2;
							} else if (peg[i + 3].beadAt(x).colour() == 'N'
									&& peg[i + 3].beadAt(x - 1).colour() != 'N') {

								return i + 3;
							}

						}
					}
				}
			}

			// check down
			for (int i = 0; i < 4; i++) {
				for (int x = 0; x < 4; x++) {
					if (((countChars(peg[i].beadAt(x).colour() + ""
							+ peg[i + 4].beadAt(x).colour() + ""
							+ peg[i + 8].beadAt(x).colour() + ""
							+ peg[i + 12].beadAt(x).colour(), 'W') >= z) && (countChars(
							peg[i].beadAt(x).colour() + ""
									+ peg[i + 4].beadAt(x).colour() + ""
									+ peg[i + 8].beadAt(x).colour() + ""
									+ peg[i + 12].beadAt(x).colour(), 'B') == 0))
							|| ((countChars(peg[i].beadAt(x).colour() + ""
									+ peg[i + 4].beadAt(x).colour() + ""
									+ peg[i + 8].beadAt(x).colour() + ""
									+ peg[i + 12].beadAt(x).colour(), 'B') >= z) && (countChars(
									peg[i].beadAt(x).colour() + ""
											+ peg[i + 4].beadAt(x).colour()
											+ ""
											+ peg[i + 8].beadAt(x).colour()
											+ ""
											+ peg[i + 12].beadAt(x).colour(),
									'W') == 0))) {
						if (x == 0) {
							if (peg[i].beadAt(x).colour() == 'N') {
								return i;
							} else if (peg[i + 4].beadAt(x).colour() == 'N') {

								return i + 4;
							} else if (peg[i + 8].beadAt(x).colour() == 'N') {

								return i + 8;
							} else if (peg[i + 12].beadAt(x).colour() == 'N') {

								return i + 12;
							}
						} else {
							if (peg[i].beadAt(x).colour() == 'N'
									&& peg[i].beadAt(x - 1).colour() != 'N') {
								return i;
							} else if (peg[i + 4].beadAt(x).colour() == 'N'
									&& peg[i + 4].beadAt(x - 1).colour() != 'N') {

								return i + 4;
							} else if (peg[i + 8].beadAt(x).colour() == 'N'
									&& peg[i + 8].beadAt(x - 1).colour() != 'N') {

								return i + 8;
							} else if (peg[i + 12].beadAt(x).colour() == 'N'
									&& peg[i + 12].beadAt(x - 1).colour() != 'N') {

								return i + 12;
							}

						}
					}
				}
			}

			// checks diagonal-x-top
			for (int i = 0; i < 4; i++) {
				if (((countChars(peg[0].beadAt(i).colour() + ""
						+ peg[5].beadAt(i).colour() + ""
						+ peg[10].beadAt(i).colour() + ""
						+ peg[15].beadAt(i).colour(), 'W') >= z) && (countChars(
						peg[0].beadAt(i).colour() + ""
								+ peg[5].beadAt(i).colour() + ""
								+ peg[10].beadAt(i).colour() + ""
								+ peg[15].beadAt(i).colour(), 'B') == 0))
						|| ((countChars(peg[0].beadAt(i).colour() + ""
								+ peg[5].beadAt(i).colour() + ""
								+ peg[10].beadAt(i).colour() + ""
								+ peg[15].beadAt(i).colour(), 'B') > z) && (countChars(
								peg[0].beadAt(i).colour() + ""
										+ peg[5].beadAt(i).colour() + ""
										+ peg[10].beadAt(i).colour() + ""
										+ peg[15].beadAt(i).colour(), 'W') == 0))) {
					for (int x = 0; x < 16; x = x + 5) {
						if (peg[x].beadAt(i).colour() == 'N') {

							return x;
						}
					}
				}
				if (((countChars(peg[3].beadAt(i).colour() + ""
						+ peg[6].beadAt(i).colour() + ""
						+ peg[9].beadAt(i).colour() + ""
						+ peg[12].beadAt(i).colour(), 'W') >= z) && (countChars(
						peg[3].beadAt(i).colour() + ""
								+ peg[6].beadAt(i).colour() + ""
								+ peg[9].beadAt(i).colour() + ""
								+ peg[12].beadAt(i).colour(), 'B') == 0))
						|| ((countChars(peg[3].beadAt(i).colour() + ""
								+ peg[6].beadAt(i).colour() + ""
								+ peg[9].beadAt(i).colour() + ""
								+ peg[12].beadAt(i).colour(), 'B') > z) && (countChars(
								peg[3].beadAt(i).colour() + ""
										+ peg[6].beadAt(i).colour() + ""
										+ peg[9].beadAt(i).colour() + ""
										+ peg[12].beadAt(i).colour(), 'W') == 0))) {
					for (int x = 3; x < 13; x = x + 3) {
						if (peg[x].beadAt(i).colour() == 'N') {

							return x;
						}
					}
				}
			}

			// checks diagonal-x-side
			for (int a = 0; a < 13; a = a + 4) {
				if (((countChars(peg[a].beadAt(3).colour() + ""
						+ peg[a + 1].beadAt(2).colour() + ""
						+ peg[a + 2].beadAt(1).colour() + ""
						+ peg[a + 3].beadAt(0).colour(), 'W') >= z) && (countChars(
						peg[a].beadAt(3).colour() + ""
								+ peg[a + 1].beadAt(2).colour() + ""
								+ peg[a + 2].beadAt(1).colour() + ""
								+ peg[a + 3].beadAt(0).colour(), 'B') == 0))
						|| ((countChars(peg[a].beadAt(3).colour() + ""
								+ peg[a + 1].beadAt(2).colour() + ""
								+ peg[a + 2].beadAt(1).colour() + ""
								+ peg[a + 3].beadAt(0).colour(), 'B') > z) && (countChars(
								peg[a].beadAt(3).colour() + ""
										+ peg[a + 1].beadAt(2).colour() + ""
										+ peg[a + 2].beadAt(1).colour() + ""
										+ peg[a + 3].beadAt(0).colour(), 'W') == 0))) {
					if (peg[a].beadAt(3).colour() == 'N'
							&& peg[a].beadAt(2).colour() != 'N') {

						return a;
					} else if (peg[a + 1].beadAt(2).colour() == 'N'
							&& peg[a + 1].beadAt(1).colour() != 'N') {

						return a + 1;
					} else if (peg[a + 2].beadAt(1).colour() == 'N'
							&& peg[a + 2].beadAt(0).colour() != 'N') {

						return a + 2;
					} else if (peg[a + 3].beadAt(0).colour() == 'N') {

						return a + 3;
					}

				}

				if (((countChars(peg[a].beadAt(0).colour() + ""
						+ peg[a + 1].beadAt(1).colour() + ""
						+ peg[a + 2].beadAt(2).colour() + ""
						+ peg[a + 3].beadAt(3).colour(), 'W') >= z) && (countChars(
						peg[a].beadAt(0).colour() + ""
								+ peg[a + 1].beadAt(1).colour() + ""
								+ peg[a + 2].beadAt(2).colour() + ""
								+ peg[a + 3].beadAt(3).colour(), 'B') == 0))
						|| ((countChars(peg[a].beadAt(0).colour() + ""
								+ peg[a + 1].beadAt(1).colour() + ""
								+ peg[a + 2].beadAt(2).colour() + ""
								+ peg[a + 3].beadAt(3).colour(), 'B') > z) && (countChars(
								peg[a].beadAt(0).colour() + ""
										+ peg[a + 1].beadAt(1).colour() + ""
										+ peg[a + 2].beadAt(2).colour() + ""
										+ peg[a + 3].beadAt(3).colour(), 'W') == 0))) {
					if (peg[a].beadAt(0).colour() == 'N') {

						return a;
					} else if (peg[a + 1].beadAt(1).colour() == 'N'
							&& peg[a + 1].beadAt(0).colour() != 'N') {

						return a + 1;
					} else if (peg[a + 2].beadAt(2).colour() == 'N'
							&& peg[a + 2].beadAt(1).colour() != 'N') {

						return a + 2;
					} else if (peg[a + 3].beadAt(3).colour() == 'N'
							&& peg[a + 3].beadAt(2).colour() != 'N') {

						return a + 3;
					}

				}
			}

			// checks diagonal-x-otherside
			for (int i = 0; i < 4; i++) {
				if (((countChars(peg[i].beadAt(0).colour() + ""
						+ peg[i + 4].beadAt(1).colour() + ""
						+ peg[i + 8].beadAt(2).colour() + ""
						+ peg[i + 12].beadAt(3).colour(), 'W') >= z) && (countChars(
						peg[i].beadAt(0).colour() + ""
								+ peg[i + 4].beadAt(1).colour() + ""
								+ peg[i + 8].beadAt(2).colour() + ""
								+ peg[i + 12].beadAt(3).colour(), 'B') == 0))
						|| ((countChars(peg[i].beadAt(0).colour() + ""
								+ peg[i + 4].beadAt(1).colour() + ""
								+ peg[i + 8].beadAt(2).colour() + ""
								+ peg[i + 12].beadAt(3).colour(), 'B') >= z) && (countChars(
								peg[i].beadAt(0).colour() + ""
										+ peg[i + 4].beadAt(1).colour() + ""
										+ peg[i + 8].beadAt(2).colour() + ""
										+ peg[i + 12].beadAt(3).colour(), 'W') == 0))) {
					if (peg[i].beadAt(0).colour() == 'N') {

						return i;
					} else if (peg[i + 4].beadAt(1).colour() == 'N'
							&& peg[i + 4].beadAt(0).colour() != 'N') {

						return i + 4;
					} else if (peg[i + 8].beadAt(2).colour() == 'N'
							&& peg[i + 8].beadAt(1).colour() != 'N') {

						return i + 8;
					} else if (peg[i + 12].beadAt(3).colour() == 'N'
							&& peg[i + 12].beadAt(2).colour() != 'N') {

						return i + 12;
					}
				}

				if (((countChars(peg[i].beadAt(3).colour() + ""
						+ peg[i + 4].beadAt(2).colour() + ""
						+ peg[i + 8].beadAt(2).colour() + ""
						+ peg[i + 12].beadAt(0).colour(), 'W') >= z) && (countChars(
						peg[i].beadAt(3).colour() + ""
								+ peg[i + 4].beadAt(2).colour() + ""
								+ peg[i + 8].beadAt(1).colour() + ""
								+ peg[i + 12].beadAt(0).colour(), 'B') == 0))
						|| ((countChars(peg[i].beadAt(3).colour() + ""
								+ peg[i + 4].beadAt(2).colour() + ""
								+ peg[i + 8].beadAt(1).colour() + ""
								+ peg[i + 12].beadAt(0).colour(), 'B') >= z) && (countChars(
								peg[i].beadAt(3).colour() + ""
										+ peg[i + 4].beadAt(2).colour() + ""
										+ peg[i + 8].beadAt(1).colour() + ""
										+ peg[i + 12].beadAt(0).colour(), 'W') == 0))) {
					if (peg[i].beadAt(3).colour() == 'N'
							&& peg[i].beadAt(2).colour() != 'N') {

						return i;
					} else if (peg[i + 4].beadAt(2).colour() == 'N'
							&& peg[i + 4].beadAt(1).colour() != 'N') {

						return i + 4;
					} else if (peg[i + 8].beadAt(1).colour() == 'N'
							&& peg[i + 8].beadAt(0).colour() != 'N') {

						return i + 8;
					} else if (peg[i + 12].beadAt(0).colour() == 'N') {

						return i + 12;
					}
				}
			}

			// checks diagonal-x-angle
			if (((countChars(peg[0].beadAt(0).colour() + ""
					+ peg[5].beadAt(1).colour() + ""
					+ peg[10].beadAt(2).colour() + ""
					+ peg[15].beadAt(3).colour(), 'W') >= z) && (countChars(
					peg[0].beadAt(0).colour() + "" + peg[5].beadAt(1).colour()
							+ "" + peg[10].beadAt(2).colour() + ""
							+ peg[15].beadAt(3).colour(), 'B') == 0))
					|| ((countChars(
							peg[0].beadAt(0).colour() + ""
									+ peg[5].beadAt(1).colour() + ""
									+ peg[10].beadAt(2).colour() + ""
									+ peg[15].beadAt(3).colour(), 'B') >= z) && (countChars(
							peg[0].beadAt(0).colour() + ""
									+ peg[5].beadAt(1).colour() + ""
									+ peg[10].beadAt(2).colour() + ""
									+ peg[15].beadAt(3).colour(), 'W') == 0))) {

				if (peg[0].beadAt(0).colour() == 'N') {
					return 0;
				} else if (peg[5].beadAt(1).colour() == 'N'
						&& peg[5].beadAt(0).colour() != 'N') {
					return 5;
				} else if (peg[10].beadAt(2).colour() == 'N'
						&& peg[10].beadAt(1).colour() != 'N') {
					return 10;
				} else if (peg[15].beadAt(3).colour() == 'N'
						&& peg[15].beadAt(2).colour() != 'N') {
					return 15;
				}
			} else if (((countChars(peg[0].beadAt(3).colour() + ""
					+ peg[5].beadAt(2).colour() + ""
					+ peg[10].beadAt(1).colour() + ""
					+ peg[15].beadAt(0).colour(), 'W') >= z) && (countChars(
					peg[0].beadAt(3).colour() + "" + peg[5].beadAt(2).colour()
							+ "" + peg[10].beadAt(1).colour() + ""
							+ peg[15].beadAt(0).colour(), 'B') == 0))
					|| ((countChars(
							peg[0].beadAt(3).colour() + ""
									+ peg[5].beadAt(2).colour() + ""
									+ peg[10].beadAt(1).colour() + ""
									+ peg[15].beadAt(0).colour(), 'B') >= z) && (countChars(
							peg[0].beadAt(3).colour() + ""
									+ peg[5].beadAt(2).colour() + ""
									+ peg[10].beadAt(1).colour() + ""
									+ peg[15].beadAt(0).colour(), 'W') == 0))) {

				if (peg[0].beadAt(3).colour() == 'N'
						&& peg[0].beadAt(2).colour() != 'N') {
					return 0;
				} else if (peg[5].beadAt(2).colour() == 'N'
						&& peg[5].beadAt(1).colour() != 'N') {
					return 5;
				} else if (peg[10].beadAt(1).colour() == 'N'
						&& peg[10].beadAt(0).colour() != 'N') {
					return 10;
				} else if (peg[15].beadAt(0).colour() == 'N') {
					return 15;
				}
			} else if (((countChars(peg[3].beadAt(0).colour() + ""
					+ peg[6].beadAt(1).colour() + ""
					+ peg[9].beadAt(2).colour() + ""
					+ peg[12].beadAt(3).colour(), 'W') >= z) && (countChars(
					peg[3].beadAt(0).colour() + "" + peg[6].beadAt(1).colour()
							+ "" + peg[9].beadAt(2).colour() + ""
							+ peg[12].beadAt(3).colour(), 'B') == 0))
					|| ((countChars(peg[3].beadAt(0).colour() + ""
							+ peg[6].beadAt(1).colour() + ""
							+ peg[9].beadAt(2).colour() + ""
							+ peg[12].beadAt(3).colour(), 'B') >= z) && (countChars(
							peg[3].beadAt(0).colour() + ""
									+ peg[6].beadAt(1).colour() + ""
									+ peg[9].beadAt(2).colour() + ""
									+ peg[12].beadAt(3).colour(), 'W') == 0))) {

				if (peg[3].beadAt(0).colour() == 'N') {
					return 3;
				} else if (peg[6].beadAt(1).colour() == 'N'
						&& peg[6].beadAt(0).colour() != 'N') {
					return 6;
				} else if (peg[9].beadAt(2).colour() == 'N'
						&& peg[9].beadAt(1).colour() != 'N') {
					return 9;
				} else if (peg[12].beadAt(3).colour() == 'N'
						&& peg[12].beadAt(2).colour() != 'N') {
					return 12;
				}
			} else if (((countChars(peg[3].beadAt(3).colour() + ""
					+ peg[6].beadAt(2).colour() + ""
					+ peg[9].beadAt(1).colour() + ""
					+ peg[12].beadAt(0).colour(), 'W') >= z) && (countChars(
					peg[3].beadAt(3).colour() + "" + peg[6].beadAt(2).colour()
							+ "" + peg[9].beadAt(1).colour() + ""
							+ peg[12].beadAt(0).colour(), 'B') == 0))
					|| ((countChars(peg[3].beadAt(3).colour() + ""
							+ peg[6].beadAt(2).colour() + ""
							+ peg[9].beadAt(1).colour() + ""
							+ peg[12].beadAt(0).colour(), 'B') >= z) && (countChars(
							peg[3].beadAt(3).colour() + ""
									+ peg[6].beadAt(2).colour() + ""
									+ peg[9].beadAt(1).colour() + ""
									+ peg[12].beadAt(0).colour(), 'W') == 0))) {

				if (peg[3].beadAt(3).colour() == 'N'
						&& peg[3].beadAt(2).colour() != 'N') {
					return 3;
				} else if (peg[6].beadAt(2).colour() == 'N'
						&& peg[6].beadAt(1).colour() != 'N') {
					return 6;
				} else if (peg[9].beadAt(1).colour() == 'N'
						&& peg[9].beadAt(0).colour() != 'N') {
					return 9;
				} else if (peg[12].beadAt(0).colour() == 'N') {
					return 12;
				}
			}

		}

		//System.out.println("random computer move");
		return randomNumber();
	}

	public static void openFile() {
		try // open file
		{
			input = new ObjectInputStream(new FileInputStream("clients.ser"));
		} // end try
		catch (IOException ioException) {
			System.err.println("Error opening file.");
		} // end catch
	} // end method openFile

	public static void closeFile() {
		try // close file and exit
		{
			if (input != null)
				input.close();
		} // end try
		catch (IOException ioException) {
			System.err.println("Error closing file.");
			System.exit(1);
		} // end catch
	} // end method closeFile


	public static void main(String[] args) {
		try {
			Scanner inputScan = new Scanner(System.in);
			String word;
			Peg[] pegA;
			//makes it so AI is always waiting for command to move
			while (true) {
				// Gets the message from scanner.
				word = inputScan.nextLine();
				if (word.equals("move;")) {
					//opens file, had to put it here otherwise would not reimport the board
					openFile();
					//basicilly gets peg array from file and copies it into Gertrudes board
					pegA = (Peg[]) input.readObject();
					//prints out the AI's move, Pipe will take this
					System.out.println(gertrude(pegA) + 1);
					//System.out.println(randomNumber()); //simply just calls random number generator
				}
			}
		} catch (EOFException endOfFileException) {
			System.err.println("end of file.");

		} catch (ClassNotFoundException classNotFoundException) {
			System.err.println("Unable to create object.");

		} catch (IOException ioException) {
			System.err.println("Error during read from file.");

		}

	}
}
