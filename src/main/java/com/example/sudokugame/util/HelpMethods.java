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
        // Generate the blankCellList, blankCellStack and the trialListArray
        ArrayList<Pair<Integer, ArrayList<Integer>>> blankCellList = FindBlankCellsList(result); // store the blank cell left
        Stack<Pair<Integer, ArrayList<Integer>>> blankCellStack = new Stack<>(); // store the blank cell that have been filled
        ArrayList<Integer>[] trialListArray = new ArrayList[SUDOKU_SIZE * SUDOKU_SIZE]; // store the numbers we tried for each cell
        Stack<ArrayList<Pair<Integer, ArrayList<Integer>>>> affectedCellStack = new Stack<>(); // store the list of cells that have been affected by a trial input
        for (int i = 0; i < trialListArray.length; i++) {
            trialListArray[i] = new ArrayList<>();
        }

        while (!blankCellList.isEmpty()) {
            Pair<Integer, ArrayList<Integer>> blankCell = blankCellList.getLast(); // get the last cell in the list (which is the cell with the least candidates left
            int row = blankCell.getFirst() / SUDOKU_SIZE;
            int col = blankCell.getFirst() % SUDOKU_SIZE;
            ArrayList<Integer> validInputs = new ArrayList<>(blankCell.getSecond());
            validInputs.removeAll(trialListArray[blankCell.getFirst()]); // get the list of candidates for this cell, after remove the numbers we tried in previous loops
            if (!validInputs.isEmpty()) { // if the list is empty => no candidates left
                // get the candidate and input it into the result[][]
                int input = validInputs.getFirst();
                result[row][col] = input;
                // remove the cell from the list and push it into the stack
                blankCellList.removeLast();
                blankCellStack.push(blankCell);
                // update the blank cell list dynamically
                ArrayList<Pair<Integer, ArrayList<Integer>>> affectedCells = new ArrayList<>();
                for (Pair<Integer, ArrayList<Integer>> cell : blankCellList) {
                    int cellRow = cell.getFirst() / SUDOKU_SIZE;
                    int cellCol = cell.getFirst() % SUDOKU_SIZE;
                    if (cellRow == row || cellCol == col || (cellRow / 3 == row / 3 && cellCol / 3 == col / 3)) {
                        if(cell.getSecond().remove(Integer.valueOf(input))) {
                            affectedCells.add(cell);
                        }
                    }
                }
                // push the affectedCells list into the stack for restoring value in the future backtracking process
                affectedCellStack.push(affectedCells);
            }
            else {
                if (blankCellStack.isEmpty()) { // if the stack is empty, return false because there is no cell to backtrack left
                    return false;
                }
                // backtracking
                Pair<Integer, ArrayList<Integer>> lastCell = blankCellStack.pop(); // pop the last cell out of the stack
                ArrayList<Pair<Integer, ArrayList<Integer>>> affectedCells = affectedCellStack.pop(); // pop the list of affected cells out of the stack
                int lastCellRow = lastCell.getFirst() / SUDOKU_SIZE;
                int lastCellCol = lastCell.getFirst() % SUDOKU_SIZE;
                // add the curren value of result[lastCellRow][lastCellCol] into the trialList of that cell, and clear the current cell trial list
                int old_value = result[lastCellRow][lastCellCol];
                trialListArray[lastCell.getFirst()].add(old_value);
                trialListArray[blankCell.getFirst()].clear();
                // retore the value in the array result[][]
                result[lastCellRow][lastCellCol] = 0;
                // restore the value affected by the insertion of last cell
                for (Pair<Integer, ArrayList<Integer>> cell : affectedCells) {
                    cell.getSecond().add(old_value);
                }
                blankCellList.add(lastCell);
            }
            // sort the list again
            SortBlankCellList(blankCellList);
        }
        return true;
    }

    private static void SortBlankCellList(ArrayList<Pair<Integer, ArrayList<Integer>>> blankCellList) {
        Collections.sort(blankCellList, new Comparator<>() {
                    @Override
                    public int compare(Pair<Integer, ArrayList<Integer>> o1, Pair<Integer, ArrayList<Integer>> o2) {
                        if (o1.getSecond().size() != o2.getSecond().size()) {
                            return o2.getSecond().size() - o1.getSecond().size();
                        } else {
                            return o2.getFirst() - o1.getFirst();
                        }
                    }
                }
        );
    }

    private static ArrayList<Pair<Integer, ArrayList<Integer>>> FindBlankCellsList(int[][] sudokuBoard) {
        ArrayList<Pair<Integer, ArrayList<Integer>>> blankCellList = new ArrayList<>();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                if (sudokuBoard[row][col] == 0) {
                    ArrayList<Integer> validInputs = new ArrayList<>();
                    List<Integer> nums = new ArrayList<>();
                    for (int i = 1; i <= SUDOKU_SIZE; i++) {
                        nums.add(i);
                    }
                    Collections.shuffle(nums);
                    for (int i : nums) {
                        if (IsValidInsert(sudokuBoard, row, col, i)) {
                            validInputs.add(i);
                        }
                    }
                    blankCellList.add(new Pair<>(SUDOKU_SIZE * row + col, validInputs));
                }
            }
        }
        SortBlankCellList(blankCellList);
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


