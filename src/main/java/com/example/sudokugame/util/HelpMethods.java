package com.example.sudokugame.util;

import static com.example.sudokugame.util.Constants.SUDOKU_SIZE;

public class HelpMethods {
    public static boolean SolveSudoku(int[][] sudokuBoard, int[][] result) {
        // neu duoc thi them check size cua hai array deu la 9 * 9 trc
        for (int row = 0; row < SUDOKU_SIZE; row++) {
           for (int col = 0; col < SUDOKU_SIZE; col++) {
               result[row][col] = sudokuBoard[row][col];
           }
       }
        return SolveSudokuUtil(result);
    }


    private static boolean SolveSudokuUtil(int[][] sudokuBoard) {
        Pair<Integer, Integer> nextEmptyCell = FindNextEmptyCell(sudokuBoard);
        if (nextEmptyCell == null) {
            return true;
        }
        int row = nextEmptyCell.getFirst();
        int col = nextEmptyCell.getSecond();
        for (int i = 1; i <= SUDOKU_SIZE; i++) {
            if (IsValidInsert(sudokuBoard, row, col, i)) {
                sudokuBoard[row][col] = i;
                if (SolveSudokuUtil(sudokuBoard)) {
                    return true;
                }
                sudokuBoard[row][col] = 0;
            }
        }
        return false;
    }

    public static Pair<Integer, Integer> FindNextEmptyCell(int[][] sudokuBoard) {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                if (sudokuBoard[i][j] == 0) {
                    return new Pair(i, j);
                }
            }
        }
        return null;
    }
    public static boolean IsValidInsert(int[][] sudokuBoard, int row, int col, int value) {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            if (sudokuBoard[row][i] == value) {
                return false;
            }
        }
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            if (sudokuBoard[i][col] == value) {
                return false;
            }
        }
        int xGrid = col / 3;
        int yGrid = row / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudokuBoard[3 * yGrid + j][3 * xGrid + i] == value) {
                    return false;
                }
            }
        }
        return true;
    }
}
