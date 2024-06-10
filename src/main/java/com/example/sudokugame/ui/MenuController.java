package com.example.sudokugame.ui;

import com.example.sudokugame.core.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    private Stage stage;
    @FXML
    private void switchToPlay(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Game game = Game.getInstance();
        game.initGame(stage);
        game.startGame();
    }
    @FXML
    private void switchToSolve(ActionEvent event){

    }
}
