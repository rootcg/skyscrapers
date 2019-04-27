package com.cristianroot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {

	private static int[] clues = {
			2, 2, 1, 3,
			2, 2, 3, 1,
			1, 2, 2, 3,
			3, 2, 1, 3
	};

	private static int[][] splittedClues;

	/**
	 * Main method, equivalent to 'solvePuzzle' method of the kata
	 */
	public static void main(String[] args) {
		splittedClues = splitClues(clues);

		int[][] solution = new int[4][4];
		for (int i = 0; i < solution.length; i++) {
			for (int j = 0; j < solution[i].length; j++) {
				solution[i][j] = -1;
			}
		}

		solveRecursive(0, 0, solution);

		print(solution);
		System.out.println("Valid: " + validate(splittedClues, solution));
	}

	private static int[][] splitClues(int[] clues) {
		int[][] splittedClues = new int[4][4];
		for (int i = 0; i < clues.length; i++) {
			splittedClues[i / 4][i % 4] = clues[i];
		}
		reverse(splittedClues[2]);
		reverse(splittedClues[3]);
		return splittedClues;
	}

	private static boolean solveRecursive(int i, int j, int[][] solution) {
		List<Integer> possibilities = new ArrayList<>();
		possibilities.add(1);
		possibilities.add(2);
		possibilities.add(3);
		possibilities.add(4);
		boolean solved;

		if (i == 3 && j == 4)
			return true;

		if (i == 4)
			return false;

		if (j == 4) {
			j = 0;
			i++;
		}

		do {
			solution[i][j] = possibilities.remove(0);
			solved = validate(splittedClues, solution) && solveRecursive(i, j + 1, solution);
		} while (!solved && !possibilities.isEmpty());

		if (!solved)
			solution[i][j] = -1;

		return solved;
	}

	private static boolean validate(int[][] clues, int[][] solution) {
		return validateRows(solution) && validateColumns(solution) && validateClues(clues, solution);
	}

	private static boolean validateColumns(int[][] solution) {
		for (int i = 0; i < solution.length; i++) {
			int[] column = new int[4];
			for (int j = 0; j < solution[i].length; j++) {
				column[j] = solution[j][i];
			}
			for (int k = 0; k < column.length - 1; k++) {
				if (column[k] == -1)
					continue;

				for (int j = k + 1; j < column.length; j++) {
					if (column[k] == column[j])
						return false;
				}
			}
		}
		return true;
	}

	private static boolean validateRows(int[][] solution) {
		for (int[] ints : solution) {
			for (int i = 0; i < ints.length - 1; i++) {
				if (ints[i] == -1)
					continue;

				for (int j = i + 1; j < ints.length; j++) {
					if (ints[i] == ints[j])
						return false;
				}
			}
		}

		return true;
	}

	private static boolean validateClue(int clue, int[] values) {
		int aux = 0;
		int count = 0;

		// Only can validate it if all the values are in
		if (IntStream.of(values).anyMatch(n -> n == -1))
			return true;

		for (int value : values) {
			if (value > aux) {
				count++;

				// I see more buildings than the clue
				if (count > clue)
					return false;

				aux = value;
			}
		}

		return count == clue;
	}

	private static boolean validateClues(int[][] clues, int[][] solution) {
		int[] cluePortion;

		// Top to bottom
		cluePortion = clues[0];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			int[] evaluate = new int[4];
			for (int j = 0; j < solution.length; j++) {
				evaluate[j] = solution[j][i];
			}

			if(!validateClue(cluePortion[i], evaluate))
				return false;
		}

		// Right to left
		cluePortion = clues[1];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			int[] evaluate = new int[4];
			for (int j = solution.length - 1; j >= 0; j--) {
				evaluate[solution.length - 1 - j] = solution[i][j];
			}

			if(!validateClue(cluePortion[i], evaluate))
				return false;
		}

		// Bottom to top
		cluePortion = clues[2];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			int[] evaluate = new int[4];
			for (int j = solution.length - 1; j >= 0; j--) {
				evaluate[solution.length - 1 - j] = solution[j][i];
			}

			if(!validateClue(cluePortion[i], evaluate))
				return false;
		}

		// Left to right
		cluePortion = clues[3];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			int[] evaluate = new int[4];
			for (int j = 0; j < solution.length; j++) {
				evaluate[j] = solution[i][j];
			}

			if(!validateClue(cluePortion[i], evaluate))
				return false;
		}

		return true;
	}

	private static void print(int[][] arr) {
		for (int j = 0; j < arr.length; j++) {
			System.out.print(" ");
			for (int k = 0; k < arr.length - 1; k++) {
				System.out.print("_");
			}
		}
		System.out.println();

		for (int[] ints : arr) {
			System.out.print("|_");
			for (int j = 0; j < arr.length; j++) {
				System.out.print(ints[j] + "_|" + (j == arr.length - 1 ? "" : "_"));
			}
			System.out.println();
		}

		System.out.println();
	}

	private static void reverse(int[] arr) {
		for (int i = 0; i < arr.length / 2; i++) {
			int temp = arr[i];
			arr[i] = arr[arr.length - i - 1];
			arr[arr.length - i - 1] = temp;
		}
	}

}
