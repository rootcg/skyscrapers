package com.cristianroot;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

	private static int[] clues = {
			3, 2, 2, 3, 2, 1,
			1, 2, 3, 3, 2, 2,
			5, 1, 2, 2, 4, 3,
			3, 2, 1, 2, 2, 4
	};

	private static int[][] splittedClues;
	private static final int SIZE = 6;

	/**
	 * Main method, equivalent to 'solvePuzzle' method of the kata
	 */
	public static void main(String[] args) {
		splittedClues = splitClues(clues);

		int[][] solution = new int[SIZE][SIZE];
		for (int i = 0; i < solution.length; i++) {
			for (int j = 0; j < solution[i].length; j++) {
				solution[i][j] = -1;
			}
		}

		long time = System.currentTimeMillis();
		solveRecursive(0, 0, solution);
		print(solution);
		System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000);
	}

	private static boolean solveRecursive(int i, int j, int[][] solution) {
		if (i == solution.length - 1 && j == solution[i].length)
			return true;

		if (j == solution[i].length) {
			j = 0;
			i++;
		}

		List<Integer> possibilities = IntStream.range(1, SIZE + 1).boxed().collect(Collectors.toList());
		boolean solved;

		do {
			solution[i][j] = possibilities.remove(0);
			solved = validate(splittedClues, solution) && solveRecursive(i, j + 1, solution);
		} while (!solved && !possibilities.isEmpty());

		// Clean the number bc it's wrong
		if (!solved)
			solution[i][j] = -1;

		return solved;
	}

	private static int[][] splitClues(int[] clues) {
		int[][] splittedClues = new int[4][SIZE];
		for (int i = 0; i < clues.length; i++) {
			splittedClues[i / SIZE][i % SIZE] = clues[i];
		}
		reverse(splittedClues[2]);
		reverse(splittedClues[3]);
		return splittedClues;
	}

	private static boolean validate(int[][] clues, int[][] solution) {
		return validateRows(solution) && validateColumns(solution) && validateClues(clues, solution);
	}

	private static boolean validateColumns(int[][] solution) {
		for (int i = 0; i < solution.length; i++) {
			int[] column = new int[SIZE];
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

			int[] evaluate = new int[SIZE];
			for (int j = 0; j < solution.length; j++) {
				evaluate[j] = solution[j][i];
			}

			if (!validateClue(cluePortion[i], evaluate))
				return false;
		}

		// Right to left
		cluePortion = clues[1];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			int[] evaluate = new int[SIZE];
			for (int j = solution.length - 1; j >= 0; j--) {
				evaluate[solution.length - 1 - j] = solution[i][j];
			}

			if (!validateClue(cluePortion[i], evaluate))
				return false;
		}

		// Bottom to top
		cluePortion = clues[2];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			int[] evaluate = new int[SIZE];
			for (int j = solution.length - 1; j >= 0; j--) {
				evaluate[solution.length - 1 - j] = solution[j][i];
			}

			if (!validateClue(cluePortion[i], evaluate))
				return false;
		}

		// Left to right
		cluePortion = clues[3];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			int[] evaluate = new int[SIZE];
			for (int j = 0; j < solution.length; j++) {
				evaluate[j] = solution[i][j];
			}

			if (!validateClue(cluePortion[i], evaluate))
				return false;
		}

		return true;
	}

	private static void print(int[][] arr) {
		for (int j = 0; j < arr.length; j++) {
			System.out.print(" ");
			for (int k = 0; k < 3; k++) {
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
