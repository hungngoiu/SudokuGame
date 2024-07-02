package com.example.sudokugame.util;

import java.util.HashSet;
import java.util.Set;

import static com.example.sudokugame.util.Constants.SUDOKU_SIZE;

public class HelpMethods {
    private static Set<Integer>[][] candidates;
    private static void initializeCandidates(int [][] board) {
        candidates = new HashSet[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                candidates[r][c] = new HashSet<>();
                if (board[r][c] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        candidates[r][c].add(num);
                    }
                } else {
                    candidates[r][c].add(board[r][c]);
                }
            }
        }
        applyConstraints(board);
    }

    private static boolean applyConstraints(int [][] board) {
        boolean progress;
        do {
            progress = false;
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (board[r][c] == 0) {
                        Set<Integer> cellCandidates = new HashSet<>(candidates[r][c]);
                        for (int num : cellCandidates) {
                            if (!isValid(r, c, num, board)) {
                                candidates[r][c].remove(num);
                                progress = true;
                            }
                        }
                        if (candidates[r][c].size() == 1) {
                            int num = candidates[r][c].iterator().next();
                            board[r][c] = num;
                            candidates[r][c].clear();
                            applyConstraintsForCell(r, c, num);
                            progress = true;
                        }
                    }
                }
            }
            progress = progress || applyHiddenSingles(board);
        } while (progress);
        return progress;
    }
    private static boolean applyHiddenSingles(int [][] board) {
        boolean progress = false;
        for (int num = 1; num <= 9; num++) {
            for (int i = 0; i < 9; i++) {
                // Check rows
                int rowCount = 0;
                int rowIndex = -1;
                for (int j = 0; j < 9; j++) {
                    if (candidates[i][j].contains(num)) {
                        rowCount++;
                        rowIndex = j;
                    }
                }
                if (rowCount == 1) {
                    board[i][rowIndex] = num;
                    candidates[i][rowIndex].clear();
                    applyConstraintsForCell(i, rowIndex, num);
                    progress = true;
                }

                // Check columns
                int colCount = 0;
                int colIndex = -1;
                for (int j = 0; j < 9; j++) {
                    if (candidates[j][i].contains(num)) {
                        colCount++;
                        colIndex = j;
                    }
                }
                if (colCount == 1) {
                    board[colIndex][i] = num;
                    candidates[colIndex][i].clear();
                    applyConstraintsForCell(colIndex, i, num);
                    progress = true;
                }

                // Check subgrids
                int startRow = (i / 3) * 3;
                int startCol = (i % 3) * 3;
                int gridCount = 0;
                int gridRowIndex = -1;
                int gridColIndex = -1;
                for (int r = startRow; r < startRow + 3; r++) {
                    for (int c = startCol; c < startCol + 3; c++) {
                        if (candidates[r][c].contains(num)) {
                            gridCount++;
                            gridRowIndex = r;
                            gridColIndex = c;
                        }
                    }
                }
                if (gridCount == 1) {
                    board[gridRowIndex][gridColIndex] = num;
                    candidates[gridRowIndex][gridColIndex].clear();
                    applyConstraintsForCell(gridRowIndex, gridColIndex, num);
                    progress = true;
                }
            }
        }
        return progress;
    }
    private static void applyConstraintsForCell(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            candidates[row][i].remove(num);
            candidates[i][col].remove(num);
        }
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                candidates[r][c].remove(num);
            }
        }
    }
    private static boolean isValid(int row, int col, int num, int [][] board) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (board[r][c] == num) {
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean SolveSudoku(int[][] sudokuBoard, int [][] result) {
        // neu duoc thi them check size cua hai array deu la 9 * 9 trc
//        for (int row = 0; row < SUDOKU_SIZE; row++) {
//           for (int col = 0; col < SUDOKU_SIZE; col++) {
//               result[row][col] = sudokuBoard[row][col];
//           }
//       }
//        return SolveSudokuUtil(result);
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                result[row][col] = sudokuBoard[row][col];
            }
        }
        initializeCandidates(result);
        applyConstraints(result);
        return applyConstraints(result);
    }


//    private static boolean SolveSudokuUtil(int[][] sudokuBoard) {
//
//        Pair<Integer, Integer> nextEmptyCell = FindNextEmptyCell(sudokuBoard);
//        if (nextEmptyCell == null) {
//            return true;
//        }
//        int row = nextEmptyCell.getFirst();
//        int col = nextEmptyCell.getSecond();
//        for (int i = 1; i <= SUDOKU_SIZE; i++) {
//            if (IsValidInsert(sudokuBoard, row, col, i)) {
//                sudokuBoard[row][col] = i;
//                if (SolveSudokuUtil(sudokuBoard)) {
//                    return true;
//                }
//                sudokuBoard[row][col] = 0;
//            }
//        }
//        return false;
//    }

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
