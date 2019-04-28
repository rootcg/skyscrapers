package com.cristianroot;

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
		long time = System.currentTimeMillis();

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

		solveRecursive(0, 0, solution);
		System.out.println("Time: " + (System.currentTimeMillis() - time));

		print(solution);
	}

	private static boolean solveRecursive(int i, int j, int[][] solution) {
		if (j == solution[i].length) {
			if(i == solution.length - 1)
				return true;

			j = 0;
			i++;
		}

		boolean solved = false;
		int number = 1;
		int arrNumber;

		do {
			arrNumber = number -1;
			if((!rowUsedNumbers[i][arrNumber] && !columnUsedNumbers[j][arrNumber])) {
				solution[i][j] = number;
				rows[i][j] = number;
				columns[j][i] = number;
				rowUsedNumbers[i][arrNumber] = true;
				columnUsedNumbers[j][arrNumber] = true;

				solved = validateClues(splittedClues, i, j) && solveRecursive(i, j + 1, solution);

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
		splittedClues[2] = copyReverse(splittedClues[2]);
		splittedClues[3] = copyReverse(splittedClues[3]);
		return splittedClues;
	}

	private static boolean validateClue(int clue, int[] values, boolean reverse) {
		if(reverse)
			values = copyReverse(values);

		// Not enough values
		if(values[0] == -1)
			return true;

		int evalLength = 0;
		int max = 0;
		int count = 0;
		for (int value : values) {
			if(value != -1) {
				evalLength++;
				if (value > max) {
					count++;
					max = value;
				}
			}
		}

		if(evalLength < clue || (count < clue && max != SIZE))
			return true;

		return count == clue && max == SIZE;
	}

	private static boolean validateClues(int[][] clues, int r, int c) {
		// Top to bottom
		if (clues[0][c] != 0 && !validateClue(clues[0][c], columns[c], false))
			return false;

		// Right to left
		if (clues[1][r] != 0 && !validateClue(clues[1][r], rows[r], true))
			return false;

		// Bottom to top
		if (clues[2][c] != 0 && !validateClue(clues[2][c], columns[c], true))
			return false;

		// Left to right
		return clues[3][r] == 0 || validateClue(clues[3][r], rows[r], false);

	}

	private static int[] copyReverse(int[] arr) {
		int[] newArr = new int[arr.length];
		int j = 0;
		for (int i = arr.length - 1; i >= 0; i--) {
			newArr[j] = arr[i];
			j++;
		}
		return newArr;
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
