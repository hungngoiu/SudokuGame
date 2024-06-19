package com.example.sudokugame.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.sudokugame.util.Constants.SUDOKU_SIZE;

public class HelpMethods {
    public static boolean SolveSudoku(int[][] sudokuBoard, int[][] result) {
        // neu duoc thi them check size cua hai array deu la 9 * 9 trc
        for (int row = 0; row < SUDOKU_SIZE; row++) {
           for (int col = 0; col < SUDOKU_SIZE; col++) {
               result[row][col] = sudokuBoard[row][col];
           }
       }
        ArrayList<Pair<Integer, ArrayList<Integer>>> blank_cell_valid_input_list = FindBlankCellsList(result); // an array list to store pairs of integer represent a blank cell and a list of valid input from 1 to 9
        Collections.sort(blank_cell_valid_input_list, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                if (o1 instanceof Pair && o2 instanceof Pair) {
                    Pair p1 = (Pair) o1;
                    Pair p2 = (Pair) o2;
                    if (p1.getSecond() instanceof ArrayList<?> && p2.getSecond() instanceof ArrayList<?>) {
                        return compare(((ArrayList<?>) p1.getSecond()).size(), ((ArrayList<?>) p2.getSecond()).size());
                    }
                }
                return 0;
            }
        });
        return SolveSudokuUtil(result, blank_cell_valid_input_list);
    }
    private static ArrayList<Pair<Integer, ArrayList<Integer>>> FindBlankCellsList(int[][] sudokuBoard) {
        ArrayList<Pair<Integer, ArrayList<Integer>>> blank_cell_valid_input_list = new ArrayList<>();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                if (sudokuBoard[row][col] == 0) {
                    ArrayList<Integer> valid_inputs = new ArrayList<>();
                    for (int i = 1; i <= SUDOKU_SIZE; i++) {
                        if (IsValidInsert(sudokuBoard, row, col, i)) {
                            valid_inputs.add(i);
                        }
                    }
                    blank_cell_valid_input_list.add(new Pair<>(SUDOKU_SIZE * row + col, valid_inputs));
                }
            }
        }
        return blank_cell_valid_input_list;
    }

    private static boolean SolveSudokuUtil(int[][] sudokuBoard, ArrayList<Pair<Integer, ArrayList<Integer>>> blank_cell_valid_input_list) {
        if (blank_cell_valid_input_list.isEmpty()) {
            return true;
        }
        Pair<Integer, ArrayList<Integer>> blank_cell = blank_cell_valid_input_list.removeFirst();
        int row = blank_cell.getFirst() / SUDOKU_SIZE;
        int col = blank_cell.getFirst() % SUDOKU_SIZE;
        ArrayList<Integer> valid_inputs = blank_cell.getSecond();
        for (int i : valid_inputs) {
            if (IsValidInsert(sudokuBoard, row, col, i)) {
                sudokuBoard[row][col] = i;
                if (SolveSudokuUtil(sudokuBoard, blank_cell_valid_input_list)) {
                    return true;
                }
                sudokuBoard[row][col] = 0;
            }
        }
        blank_cell_valid_input_list.addFirst(blank_cell);
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
