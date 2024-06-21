package com.example.sudokugame.ui;

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

}
