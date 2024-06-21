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
        ArrayList<Pair<Integer, ArrayList<Integer>>> blankCellList = FindBlankCellsList(result);
        Stack<Pair<Integer, ArrayList<Integer>>> blankCellStack = new Stack<>();
        int[] trialsIndexofEachBlankCell = new int[81];
        int test = 0;
        while (!blankCellList.isEmpty()) {
            test++;
            Pair<Integer, ArrayList<Integer>> blankCell = blankCellList.getFirst();
            int row = blankCell.getFirst() / SUDOKU_SIZE;
            int col = blankCell.getFirst() % SUDOKU_SIZE;
            ArrayList<Integer> validInputs = blankCell.getSecond();
            if (trialsIndexofEachBlankCell[blankCell.getFirst()] < validInputs.size()) {
                int value = validInputs.get(trialsIndexofEachBlankCell[blankCell.getFirst()]);
                result[row][col] = value;
                // move the trial index to next
                trialsIndexofEachBlankCell[row * SUDOKU_SIZE + col] = trialsIndexofEachBlankCell[blankCell.getFirst()]  + 1;
                // push the cell just input into the stack
                blankCellStack.push(blankCell);
            }
            else {
                // return false if there is no last cell in the stack
                if (blankCellStack.isEmpty()) {
                    return false;
                }
                // go back the last cell input to try another number
                Pair<Integer, ArrayList<Integer>> lastCell = blankCellStack.pop();
                int lastCellRow = lastCell.getFirst() / SUDOKU_SIZE;
                int lastCellCol = lastCell.getFirst() % SUDOKU_SIZE;
                // reset the value inserted in the array
                result[lastCellRow][lastCellCol] = 0;
                // reset the value in the array that store the trial index
                trialsIndexofEachBlankCell[row * SUDOKU_SIZE + col] = 0;
            }
            blankCellList = FindBlankCellsList(result);
        }
        return true;
    }

    private static ArrayList<Pair<Integer, ArrayList<Integer>>> FindBlankCellsList(int[][] sudokuBoard) {
        ArrayList<Pair<Integer, ArrayList<Integer>>> blankCellList = new ArrayList<>();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                if (sudokuBoard[row][col] == 0) {
                    ArrayList<Integer> validInputs = new ArrayList<>();
                    List<Integer> numbers = new ArrayList<>();
                    for (int i = 1; i <= 9; i++) {
                        numbers.add(i);
                    }
                    Collections.shuffle(numbers);
                    for (int i : numbers) {
                        if (IsValidInsert(sudokuBoard, row, col, i)) {
                            validInputs.add(i);
                        }
                    }
                    blankCellList.add(new Pair<>(SUDOKU_SIZE * row + col, validInputs));
                }
            }
        }
        Collections.sort(blankCellList, new Comparator<>() {
                    @Override
                    public int compare(Pair<Integer, ArrayList<Integer>> o1, Pair<Integer, ArrayList<Integer>> o2) {
                        if (o1.getSecond().size() != o2.getSecond().size()) {
                            return o1.getSecond().size() - o2.getSecond().size();
                        } else {
                            return o1.getFirst() - o2.getFirst();
                        }
                    }
                }
        );
        return blankCellList;
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


