package com.example.sudokugame.core;

import com.example.sudokugame.ui.SudokuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.sudokugame.util.Constants.LEVELS_NUMBER;
import static com.example.sudokugame.util.Constants.RESOURCE_ROOT;
import static com.example.sudokugame.util.LoadMethods.LoadLevel;

public final class Game {
    private static final Game INSTANCE = new Game();
    private Stage stage;
    private Level level;
    private int levelIndex;

    private Game() {
        levelIndex = 0;
    }
    public static Game getInstance() {
        return INSTANCE;
    }
    public void initGame(Stage stage) {
        this.stage = stage;
        stage.setTitle("Sudoku Game");
        stage.setResizable(false);
        level = LoadLevel(levelIndex);
    }
    public void startGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_ROOT + "SudokuUI.fxml"));
        Scene scene = new Scene(loader.load());
        SudokuController controller = loader.getController();
        stage.setScene(scene);
        stage.show();
    }
    public void loadLevel() {
        level = LoadLevel(levelIndex);
    }
    public Level getLevel() {
        return level;
    }

    public void nextLevel() {
        levelIndex++;
        if (levelIndex == LEVELS_NUMBER) {
            levelIndex = 0;
        }
        loadLevel();
    }
}
