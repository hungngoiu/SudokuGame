package com.example.sudokugame.ui;

import com.example.sudokugame.core.Game;
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
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.sudokugame.util.Constants.SUDOKU_SIZE;

public class SudokuController implements Initializable{
    @FXML
    private GridPane sudokuBoard;
    @FXML
    private GridPane insertGrid;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    private Game game = Game.getInstance();
    private int select = -1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSudokuBoard();
        initInputBoard();
        drawSudokuBoard();
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
    private void initInputBoard() {
        insertGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (select == -1) {
                    return;
                }
                for( Node node : insertGrid.getChildren()) {
                    if( node instanceof TextField) {
                        if(node.localToScene(node.getBoundsInLocal()).contains(e.getSceneX(), e.getSceneY())) {
                            TextField textfield = (TextField) node;
                            int row = select / SUDOKU_SIZE;
                            int col = select % SUDOKU_SIZE;
                            int value = Integer.parseInt(textfield.getText());
                            ArrayList<Pair<Integer, Integer>> row_col_pairs = new ArrayList<>();
                            game.getLevel().insert(row, col, value, row_col_pairs);
                            clearAllMistake();
                            markAsMistake(row_col_pairs);
                        }
                    }
                }
                select = -1;
                drawSudokuBoard();
            }
        });
    }

    private void markAsMistake(ArrayList<Pair<Integer, Integer>> rowColPairs) {
        List<Node> textFields = sudokuBoard.getChildren();
        for (Pair<Integer, Integer> pair : rowColPairs) {
            TextField textField = (TextField) textFields.get(SUDOKU_SIZE * pair.getFirst() + pair.getSecond() + 1);
            textField.getStyleClass().add("mistakeCell");
        }
    }
    private void clearAllMistake() {
        List<Node> textFields = sudokuBoard.getChildren();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                TextField textField = (TextField) textFields.get(SUDOKU_SIZE * row + col + 1);
                textField.getStyleClass().removeAll("mistakeCell");
            }
        }
    }
    public void drawSudokuBoard() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int col = 0; col < SUDOKU_SIZE; col++) {
                drawCell(row, col, game.getLevel().getElement(row, col));
            }
        }}
    private void drawCell(int row, int col, int val) {
        TextField textField = (TextField) sudokuBoard.getChildren().get(SUDOKU_SIZE * row + col + 1);
        if (val != 0) {
            textField.setText(String.valueOf(val));
        }
        else {
            textField.setText("");
        }
        if (!game.getLevel().isOccupied(row, col)) {
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
    public void resetSelect() { // method for reset focus and select value when click outside of the board
        sudokuBoard.requestFocus();
        select = -1;
    }
    @FXML
    private void undo() {
        clearAllMistake();
        select = -1;
        if (game.getLevel().undo()) {
            drawSudokuBoard();
        }
        undoButton.requestFocus();
    }
    @FXML
    private void redo() {
        select = -1;
        if (game.getLevel().redo()){
            drawSudokuBoard();
        }
        redoButton.requestFocus();
    }
    @FXML
    private void restart() {
        clearAllMistake();
        resetSelect();
        game.loadLevel();
        drawSudokuBoard();
    }
    @FXML
    private void newGame() {
        clearAllMistake();
        resetSelect();
        game.nextLevel();
        drawSudokuBoard();
    }

    @FXML
    private void backToMenu(ActionEvent event) {
        LoadMethods.switchToScene(event, "Menu.fxml");
    }



}
