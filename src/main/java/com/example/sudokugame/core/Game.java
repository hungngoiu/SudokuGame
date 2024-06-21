package com.example.sudokugame.core;

import com.example.sudokugame.Main;
import com.example.sudokugame.ui.SudokuController;
import com.example.sudokugame.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.example.sudokugame.util.Constants.LEVELS.EASY;
import static com.example.sudokugame.util.Constants.LEVELS_NUMBER;
import static com.example.sudokugame.util.Constants.RESOURCE_ROOT;
import static com.example.sudokugame.util.LoadMethods.*;

public final class Game {
    private static final Game INSTANCE = new Game();
    private Stage stage;

    private Level level;


    private Game() {
    }
    public static Game getInstance() {
        return INSTANCE;
    }
    public void initGame(Stage stage) {
        this.stage = stage;
        stage.setTitle("Sudoku Game");
        stage.setResizable(false);
        level = LoadLevel(EASY);
    }
    public void startGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_ROOT + "SudokuUI.fxml"));
        Scene scene = new Scene(loader.load());
        SudokuController controller = loader.getController();
        stage.setScene(scene);
        stage.show();

    }

    public void loadLevel(int[][] sudokuBoard) {
        level = new Level(sudokuBoard);
    }

    public void loadLevel(Constants.LEVELS levels) {
        level = LoadLevel(levels);

    }

    public Level getLevel() {
        return level;
    }

    public Stage getStage() {
        return stage;
    }
}
