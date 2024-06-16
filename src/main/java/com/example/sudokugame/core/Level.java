package com.example.sudokugame.core;

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
    Stack<Move> redoStack;
    private boolean isValidInsert = true;
    private int mistakeCount;

    public Level(int[][] sudokuBoard) {
        this.moveStack = new Stack<>();
        this.redoStack = new Stack<>();
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


    public List<Cell> checkViolation(int row, int col, int value) {
        List<Cell> cells = new ArrayList<>();
        isValidInsert = true;
        int xCell;
        for(xCell = 0; xCell < SUDOKU_SIZE; ++xCell) {
            if (xCell != col && this.sudokuBoard[row][xCell] == value) {
                cells.add(new Cell(row, xCell));
                isValidInsert = false;
            }
        }

        for(xCell = 0; xCell < SUDOKU_SIZE; ++xCell) {
            if (xCell != row && this.sudokuBoard[xCell][col] == value) {
                cells.add(new Cell(xCell, col));
                isValidInsert = false;

            }
        }

        int xGrid = col / 3;
        int yGrid = row / 3;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                if (this.sudokuBoard[3 * yGrid + j][3 * xGrid + i] == value) {
                    cells.add(new Cell(3 * yGrid + j, 3 * xGrid + i));
                    isValidInsert = false;
                }
            }
        }

        return cells;
    }

    private boolean isValidInsert(int row, int col, int value){
        checkViolation(row, col, value);
        return isValidInsert;
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
