package com.example.sudokugame.core;

import com.example.sudokugame.util.Pair;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;

import static com.example.sudokugame.util.Constants.*;
import static com.example.sudokugame.util.HelpMethods.FindNextEmptyCell;
import static com.example.sudokugame.util.HelpMethods.SolveSudoku;

class Move {
    private int row;
    private int col;
    private int newValue;
    private int oldValue; // Store the old value of that cell when we insert

    Move(int row, int col, int oldValue, int newValue) {
        this.row = row;
        this.col = col;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public int getOldValue() {
        return oldValue;
    }

    public int getNewValue() {return newValue;}
}

public class Level {
    private int[][] board;
    private Set<Integer>[][] candidates;
    private int[][] sudokuBoard;
    private int[][] initialValue;
    private int[][] result;
    private int mistakeCount;

    Stack<Move> moveStack;
    Stack<Move> redoStack;

    public Level(int[][] sudokuBoard) {
        this.moveStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.sudokuBoard = sudokuBoard;
        this.board = sudokuBoard;
        this.initialValue = new int[SUDOKU_SIZE][SUDOKU_SIZE];
        this.candidates = new HashSet[9][9];
        this.result = new int[SUDOKU_SIZE][SUDOKU_SIZE];

        for(int row = 0; row < SUDOKU_SIZE; ++row) {
            for(int col = 0; col < SUDOKU_SIZE; ++col) {
                if (sudokuBoard[row][col] == 0) {
                    this.initialValue[row][col] = 0;
                } else {
                    this.initialValue[row][col] = 1;
                }
            }
        }
        initializeCandidates();
        solve();
        result = board;
    }

    private void initializeCandidates() {
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
        applyConstraints();
    }

    private void applyConstraints() {
        boolean progress;
        do {
            progress = false;
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (board[r][c] == 0) {
                        Set<Integer> cellCandidates = new HashSet<>(candidates[r][c]);
                        for (int num : cellCandidates) {
                            if (!isValid(r, c, num)) {
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
            progress = progress || applyHiddenSingles();
        } while (progress);
    }
    private boolean applyHiddenSingles() {
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
    private void applyConstraintsForCell(int row, int col, int num) {
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
    private boolean isValid(int row, int col, int num) {
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
    public void solve() {
        applyConstraints();
    }



    public int[][] getInitialValue(){
        return initialValue;
    }
    public int getResult(int row, int col){
        return result[row][col];
    }
    public int getElement(int row, int col) {
        return this.sudokuBoard[row][col];
    }

    public void hint(int row, int col) {
        int value = result[row][col];
        sudokuBoard[row][col] = value;
        initialValue[row][col] = 1;
        for (int i = 0; i < SUDOKU_SIZE; ++i) {
            if (sudokuBoard[row][i] == value && i != col) {
                sudokuBoard[row][i] = 0;
            }
        }
        for (int i = 0; i < SUDOKU_SIZE; ++i) {
            if (sudokuBoard[i][col] == value && i != row) {
                sudokuBoard[i][col] = 0;
            }
        }
        int xGrid = col / 3;
        int yGrid = row / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudokuBoard[yGrid * 3 + j][xGrid * 3 + i] == value && !(yGrid * 3 + j == row && xGrid * 3 + i == col)) {
                    sudokuBoard[yGrid * 3 + j][xGrid * 3 + i] = 0;
                }
            }
        }
        moveStack.clear();
        redoStack.clear();
    }
    public void setElement(int row, int col, int value) {
        this.sudokuBoard[row][col] = value;
    }

    public boolean insert(int row, int col, int value) {
        if(!this.isOccupied(row, col) && isValidInsert(row, col, value)) {
            Move move = new Move(row, col, sudokuBoard[row][col], value);
            this.moveStack.push(move);
            this.redoStack.clear();
            this.sudokuBoard[row][col] = value;
            return true;
        } else {
            return false;
        }
    }
    public boolean insert(int row, int col, int value, ArrayList<Pair<Integer, Integer>> row_col_violate_cells) {
        if (insert(row, col, value)) {
            return true;
        }
        else {
            for(int xCell = 0; xCell < SUDOKU_SIZE; ++xCell) {
                if (xCell != col && this.sudokuBoard[row][xCell] == value) {
                    row_col_violate_cells.add(new Pair<>(row, xCell));
                }
            }

            for(int yCell = 0; yCell < SUDOKU_SIZE; ++yCell) {
                if (yCell != row && this.sudokuBoard[yCell][col] == value) {
                    row_col_violate_cells.add(new Pair<>(yCell, col));
                }
            }

            int xGrid = col / 3;
            int yGrid = row / 3;

            for(int i = 0; i < 3; ++i) {
                for(int j = 0; j < 3; ++j) {
                    if (3 * yGrid + j == row && 3 * xGrid + i == col) {
                        continue;
                    }
                    if (this.sudokuBoard[3 * yGrid + j][3 * xGrid + i] == value) {
                        row_col_violate_cells.add(new Pair<>(3 * yGrid + j, 3 * xGrid + i));
                    }
                }
            }
            mistakeCount++;
        }
        return false;
    }
    public boolean undo() {
        if (!moveStack.isEmpty()) {
            this.redoStack.push(moveStack.peek());
            Move lastMove = moveStack.pop();
            sudokuBoard[lastMove.getRow()][lastMove.getCol()] = lastMove.getOldValue();
            return true;
        }
        return false;
    }
    public boolean redo() {
        if (!redoStack.isEmpty()) {
            this.moveStack.push(redoStack.peek());
            Move lastMove = redoStack.pop();
            sudokuBoard[lastMove.getRow()][lastMove.getCol()] = lastMove.getNewValue();
            return true;
        }
        return false;
    }
    public boolean isOccupied(int row, int col) {
        return (this.initialValue[row][col] == 1);
    }


    public boolean isValidInsert(int row, int col, int value){
        for(int xCell = 0; xCell < SUDOKU_SIZE; ++xCell) {
            if (xCell != col && this.sudokuBoard[row][xCell] == value) {
                return false;
            }
        }

        for(int yCell = 0; yCell < SUDOKU_SIZE; ++yCell) {
            if (yCell != row && this.sudokuBoard[yCell][col] == value) {
                return false;
            }
        }

        int xGrid = col / 3;
        int yGrid = row / 3;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                if (3 * yGrid + j == row && 3 * xGrid + i == col) {
                    continue;
                }
                if (this.sudokuBoard[3 * yGrid + j][3 * xGrid + i] == value) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return mistakeCount >= MAX_MISTAKES;
    }

    public boolean isFinished() {
        if (FindNextEmptyCell(sudokuBoard) == null) {
            return true;
        }
        return false;
    }

    public boolean erase(int row, int col) {
        if (!isOccupied(row, col)) {
            Move move = new Move(row, col, sudokuBoard[row][col], 0);
            this.moveStack.push(move);
            this.redoStack.clear();
            this.sudokuBoard[row][col] = 0;
            return true;
        }
        return false;
    }

    public int[][] getResultA() {
        return result;
    }
}
