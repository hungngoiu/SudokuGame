package com.example.sudokugame.ui;

import com.example.sudokugame.core.Level;
import com.example.sudokugame.util.HelpMethods;
import com.example.sudokugame.util.LoadMethods;
import com.example.sudokugame.util.Pair;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.*;

import static com.example.sudokugame.util.Constants.SUDOKU_SIZE;
import static com.example.sudokugame.util.HelpMethods.IsValidInsert;
import static com.example.sudokugame.util.HelpMethods.SolveSudoku;

public class SolveController implements Initializable {
    private int[][] inputArray =  new int[SUDOKU_SIZE][SUDOKU_SIZE];
    private int[][] resultArray =  new int[SUDOKU_SIZE][SUDOKU_SIZE];
    private int select = -1;
    @FXML
    private Button importBtn;
    @FXML
    private GridPane insertGrid;
    @FXML
    private GridPane sudokuBoard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSudokuBoard();
        initInputBoard();
        drawSudokuBoard();
    }

    public void backToMenu(ActionEvent event) {
        LoadMethods.SwitchToScene(event, "Menu.fxml");
    }

    @FXML
    public void solve() {
        resetSelect();
        SolveSudoku(inputArray, resultArray);
        drawSudokuBoardResult();
    }

    private void resetSelect() {
        sudokuBoard.requestFocus();
        select = -1;
    }

    private void drawSudokuBoardResult() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                drawCell(row, col, resultArray[row][col]);
            }
        }
    }

    @FXML
    public void loadSudokuProblem(ActionEvent event) {
        inputArray = LoadMethods.LoadSudokuPuzzles();
        drawSudokuBoard();
    }
    public void drawSudokuBoard() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                drawCell(row, col, inputArray[row][col]);
            }
        }
    }


    private void drawCell(int row, int col, int val) {
        TextField textField = (TextField) sudokuBoard.getChildren().get(SUDOKU_SIZE * row + col + 1);
        if (val != 0) {
            textField.setText(String.valueOf(val));
        }
        else {
            textField.setText("");
        }
        if (inputArray[row][col] == 0) {
            textField.getStyleClass().removeAll("textFieldOccupied");

            textField.getStyleClass().add("textFieldUnoccupied");
            textField.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    select = SUDOKU_SIZE * row + col;
                }
            });
        } else {
            textField.getStyleClass().removeAll("textFieldUnoccupied");
            textField.getStyleClass().add("textFieldOccupied");
            textField.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    select = -1;
                }
            });
        }
    }


    private void initInputBoard() {
        insertGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (select == -1) {
                    return;
                } else {
                    System.out.println(select);
                    importBtn.setDisable(true);
                    for (Node node : insertGrid.getChildren()) {
                            if (node instanceof TextField) {
                                if (node.localToScene(node.getBoundsInLocal()).contains(e.getSceneX(), e.getSceneY())) {
                                    TextField textfield = (TextField) node;
                                    int row = select / SUDOKU_SIZE;
                                    int col = select % SUDOKU_SIZE;
                                    int value = Integer.parseInt(textfield.getText());
                                    if(IsValidInsert(inputArray, row, col, value)){
                                        inputArray[row][col] = value;
                                    }
                                }
                            }
                        }

                        select = -1;
                        drawSudokuBoard();
                }
            }
        });
    }

    private void initSudokuBoard() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                TextField textField = new TextField();
                textField.setPrefWidth(60);
                textField.setPrefHeight(60);

                textField.setStyle("-fx-text-box-border: transparent");
                textField.setEditable(false);

                textField.setAlignment(Pos.CENTER);

                textField.setFocusTraversable(false);

                sudokuBoard.add(textField, col, row);
            }
        }
    }

    // New algo using a bit manipulation
    public void solve_with_new_algo(ActionEvent actionEvent) {
        resetSelect();
        SolveSudokuWithBitManipulation(inputArray, resultArray);
        drawSudokuBoardResult();
    }

    private boolean SolveSudokuWithBitManipulation(int[][] board, int[][] result) {
        // Bit manipulation algorithm to solve Sudoku
        int[] rows = new int[9];
        int[] cols = new int[9];
        int[] blocks = new int[9];

        // Initialize the bitmask arrays
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != 0) {
                    int val = board[r][c] - 1;
                    rows[r] |= (1 << val);
                    cols[c] |= (1 << val);
                    blocks[(r / 3) * 3 + (c / 3)] |= (1 << val);
                }
            }
        }

        return solve(board, 0, 0, rows, cols, blocks, result);
    }

    private boolean solve(int[][] board, int row, int col, int[] rows, int[] cols, int[] blocks, int[][] result) {
        if (row == 9) {
            // Copy the solution to the result array
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    result[r][c] = board[r][c];
                }
            }
            return true;
        }
        if (col == 9) {
            return solve(board, row + 1, 0, rows, cols, blocks, result);
        }
        if (board[row][col] != 0) {
            return solve(board, row, col + 1, rows, cols, blocks, result);
        }

        int blockIndex = (row / 3) * 3 + (col / 3);
        for (int val = 1; val <= 9; val++) {
            int bit = 1 << (val - 1);
            if ((rows[row] & bit) == 0 && (cols[col] & bit) == 0 && (blocks[blockIndex] & bit) == 0) {
                // Place the value
                board[row][col] = val;
                rows[row] |= bit;
                cols[col] |= bit;
                blocks[blockIndex] |= bit;

                // Recurse
                if (solve(board, row, col + 1, rows, cols, blocks, result)) {
                    return true;
                }

                // Undo the value
                board[row][col] = 0;
                rows[row] &= ~bit;
                cols[col] &= ~bit;
                blocks[blockIndex] &= ~bit;
            }
        }
        return false;
    }
}
