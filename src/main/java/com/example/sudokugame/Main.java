package com.example.sudokugame;

import com.example.sudokugame.core.Game;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Game game = Game.getInstance();
        game.initGame(stage);
        game.startGame();

    }

    public static void main(String[] args) {
        launch();
    }
}