package com.example.sudokugame.core;

import java.util.Stack;

import static com.example.sudokugame.util.Constants.SUDOKU_SIZE;
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
    Stack<Move> moveStack;
    Stack<Move> undoStack;


    public Level(int[][] sudokuBoard) {
        this.moveStack = new Stack<>();
        this.undoStack = new Stack<>();
        this.sudokuBoard = sudokuBoard;
        this.initialValue = new int[SUDOKU_SIZE][SUDOKU_SIZE];

        for(int row = 0; row < SUDOKU_SIZE; ++row) {
            for(int col = 0; col < SUDOKU_SIZE; ++col) {
                if (sudokuBoard[row][col] == 0) {
                    this.initialValue[row][col] = 0;
                } else {
                    this.initialValue[row][col] = 1;
                }
            }
        }

    }
    public int getElement(int row, int col) {
        return this.sudokuBoard[row][col];
    }

    public void setElement(int row, int col, int value) {
        this.sudokuBoard[row][col] = value;
    }

    public boolean insert(int row, int col, int value) {
        if (!this.isOccupied(row, col) && this.isValidInsert(row, col, value)) {
            Move move = new Move(row, col, sudokuBoard[row][col], value);
            this.moveStack.push(move);

            this.sudokuBoard[row][col] = value;
            return true;
        } else {
            return false;
        }
    }
    public boolean undo() {
        if (!moveStack.isEmpty()) {
            this.undoStack.push(moveStack.peek());
            Move lastMove = moveStack.pop();
            sudokuBoard[lastMove.getRow()][lastMove.getCol()] = lastMove.getOldValue();
            return true;
        }
        return false;
    }
    public boolean redo() {
        if (!undoStack.isEmpty()) {
            this.moveStack.push(undoStack.peek());
            Move lastMove = undoStack.pop();
            sudokuBoard[lastMove.getRow()][lastMove.getCol()] = lastMove.getNewValue();
            return true;
        }
        return false;
    }
    public boolean isOccupied(int row, int col) {
        return (this.initialValue[row][col] == 1);
    }

    public boolean isValidInsert(int row, int col, int value) {
        int xCell;
        for(xCell = 0; xCell < SUDOKU_SIZE; ++xCell) {
            if (xCell != col && this.sudokuBoard[row][xCell] == value) {
                return false;
            }
        }

        for(xCell = 0; xCell < SUDOKU_SIZE; ++xCell) {
            if (xCell != row && this.sudokuBoard[xCell][col] == value) {
                return false;
            }
        }

        int xGrid = col / 3;
        int yGrid = row / 3;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                if (this.sudokuBoard[3 * yGrid + j][3 * xGrid + i] == value) {
                    return false;
                }
            }
        }
        return true;
    }
}
