package com.example.sudokugame.core;

import com.example.sudokugame.util.Pair;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        this.initialValue = new int[SUDOKU_SIZE][SUDOKU_SIZE];
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
        SolveSudoku(sudokuBoard, result);
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
}
