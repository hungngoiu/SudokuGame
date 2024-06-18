package com.example.sudokugame.ui;

import com.example.sudokugame.core.Level;
import com.example.sudokugame.util.LoadMethods;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.sudokugame.util.Constants.SUDOKU_SIZE;

public class SolveController implements Initializable {
    private int select = -1;
    Level level;
    @FXML
    private GridPane insertGrid;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button newGameButton;

    @FXML
    private GridPane sudokuBoard;

    public void backToMenu(ActionEvent event) {
        LoadMethods.SwitchToScene(event, "Menu.fxml");
    }

    public void solve(ActionEvent event) {
        resetSelect();
        drawSudokuBoardResult();

    }

    private void resetSelect() {
        sudokuBoard.requestFocus();
        select = -1;
    }


    private void drawSudokuBoardResult() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                drawCell(row, col, level.getResult(row, col));
            }
        }
    }

    public void take(ActionEvent event) {
        level =LoadMethods.LoadLevelFromFile();
        drawSudokuBoard(level);
    }
    public void drawSudokuBoard(Level level) {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                drawCell(row, col, level.getElement(row, col));
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
        if (!level.isOccupied(row, col)) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSudokuBoard();
        //initInputBoard();
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
