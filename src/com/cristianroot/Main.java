package com.cristianroot;

import java.util.stream.IntStream;

public class Main {

//	private static int[] clues = {
//			3, 2, 2, 3, 2, 1,
//			1, 2, 3, 3, 2, 2,
//			5, 1, 2, 2, 4, 3,
//			3, 2, 1, 2, 2, 4
//	};
	private static int[] clues = {
		0, 0, 0, 2, 2, 0,
		0, 0, 0, 6, 3, 0,
		0, 4, 0, 0, 0, 0,
		4, 4, 0, 3, 0, 0
	};

	private static boolean[][] columnUsedNumbers;
	private static boolean[][] rowUsedNumbers;
	private static int[][] rows;
	private static int[][] columns;
	private static int[][] splittedClues;
	private static final int SIZE = 6;

	/**
	 * Main method, equivalent to 'solvePuzzle' method of the kata
	 */
	public static void main(String[] args) {
		columnUsedNumbers = new boolean[SIZE][SIZE];
		rowUsedNumbers = new boolean[SIZE][SIZE];
		rows = new int[SIZE][SIZE];
		columns = new int[SIZE][SIZE];
		splittedClues = splitClues(clues);

		int[][] solution = new int[SIZE][SIZE];
		for (int i = 0; i < solution.length; i++) {
			for (int j = 0; j < solution[i].length; j++) {
				solution[i][j] = -1;
				rows[i][j] = -1;
				columns[i][j] = -1;
			}
		}

		long time = System.currentTimeMillis();
		solveRecursive(0, 0, solution);
		print(solution);
		System.out.println("Time: " + (System.currentTimeMillis() - time));
	}

	private static boolean solveRecursive(int i, int j, int[][] solution) {
		if (i == solution.length - 1 && j == solution[i].length)
			return true;

		if (j == solution[i].length) {
			j = 0;
			i++;
		}

		boolean solved = false;
		int number = 1;

		do {
			if((!rowUsedNumbers[i][number - 1] && !columnUsedNumbers[j][number - 1])) {
				solution[i][j] = number;
				rows[i][j] = number;
				columns[j][i] = number;
				rowUsedNumbers[i][number - 1] = true;
				columnUsedNumbers[j][number - 1] = true;

				solved = validateClues(splittedClues, solution) && solveRecursive(i, j + 1, solution);

				if (!solved) {
					solution[i][j] = -1;
					rows[i][j] = -1;
					columns[j][i] = -1;
					rowUsedNumbers[i][number - 1] = false;
					columnUsedNumbers[j][number - 1] = false;
				}
			}
			number++;
		} while (!solved && number <= SIZE);

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

	private static boolean validateClue(int clue, int[] values, boolean reverse) {
		int max = 0;
		int count = 0;
		int[] eval = IntStream.of(values).filter(n -> n != -1).toArray();

		if(reverse)
			reverse(eval);

		// Not enough values
		if(eval.length < clue || (reverse ? values[values.length - 1] : values[0]) == -1)
			return true;

		for (int value : eval) {
			if (value > max) {
				count++;

				// I see more buildings than the clue
				if (count > clue)
					return false;

				max = value;
			}
		}

		if(count < clue && max != SIZE)
			return true;

		return count == clue && max == SIZE;
	}

	private static boolean validateClues(int[][] clues, int[][] solution) {
		int[] cluePortion;

		// Top to bottom
		cluePortion = clues[0];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			if (!validateClue(cluePortion[i], columns[i], false))
				return false;
		}

		// Right to left
		cluePortion = clues[1];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			if (!validateClue(cluePortion[i], rows[i], true))
				return false;
		}

		// Bottom to top
		cluePortion = clues[2];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			if (!validateClue(cluePortion[i], columns[i], true))
				return false;
		}

		// Left to right
		cluePortion = clues[3];
		for (int i = 0; i < cluePortion.length; i++) {
			if (cluePortion[i] == 0)
				continue;

			if (!validateClue(cluePortion[i], rows[i], false))
				return false;
		}

		return true;
	}

	private static void reverse(int[] arr) {
		for (int i = 0; i < arr.length / 2; i++) {
			int temp = arr[i];
			arr[i] = arr[arr.length - i - 1];
			arr[arr.length - i - 1] = temp;
		}
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

}
