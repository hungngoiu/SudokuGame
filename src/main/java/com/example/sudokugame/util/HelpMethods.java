package com.example.sudokugame.util;

import java.util.*;

import static com.example.sudokugame.util.Constants.SUDOKU_SIZE;

public class HelpMethods {
    public static boolean SolveSudoku(int[][] sudokuBoard, int[][] result) {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                result[row][col] = sudokuBoard[row][col];
            }
        }

        PriorityQueue<Pair<Integer, ArrayList<Integer>>> blankCellQueue = FindBlankCellsQueue(result);
        return SolveSudokuUtil(result, blankCellQueue);
    }

    private static PriorityQueue<Pair<Integer, ArrayList<Integer>>> FindBlankCellsQueue(int[][] sudokuBoard) {
        PriorityQueue<Pair<Integer, ArrayList<Integer>>> blankCellQueue = new PriorityQueue<>(
                Comparator.comparingInt(p -> p.getSecond().size())
        );
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                if (sudokuBoard[row][col] == 0) {
                    ArrayList<Integer> validInputs = new ArrayList<>();
                    for (int i = 1; i <= SUDOKU_SIZE; i++) {
                        if (IsValidInsert(sudokuBoard, row, col, i)) {
                            validInputs.add(i);
                        }
                    }
                    blankCellQueue.add(new Pair<>(SUDOKU_SIZE * row + col, validInputs));
                }
            }
        }
        return blankCellQueue;
    }

    private static boolean SolveSudokuUtil(int[][] sudokuBoard, PriorityQueue<Pair<Integer, ArrayList<Integer>>> blankCellQueue) {
        if (blankCellQueue.isEmpty()) {
            return true;
        }
        Pair<Integer, ArrayList<Integer>> blankCell = blankCellQueue.poll();
        int row = blankCell.getFirst() / SUDOKU_SIZE;
        int col = blankCell.getFirst() % SUDOKU_SIZE;
        ArrayList<Integer> validInputs = blankCell.getSecond();

        for (int i : validInputs) {
            if (IsValidInsert(sudokuBoard, row, col, i)) {
                sudokuBoard[row][col] = i;
                if (SolveSudokuUtil(sudokuBoard, blankCellQueue)) {
                    return true;
                }
                sudokuBoard[row][col] = 0;
            }
        }
        blankCellQueue.add(blankCell);
        return false;
    }

    public static boolean IsValidInsert(int[][] sudokuBoard, int row, int col, int value) {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            if (sudokuBoard[row][i] == value || sudokuBoard[i][col] == value) {
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
}


