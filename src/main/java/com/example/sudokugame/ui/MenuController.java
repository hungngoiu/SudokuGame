package com.example.sudokugame.ui;

import com.example.sudokugame.core.Game;
import com.example.sudokugame.util.LoadMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.sudokugame.util.LoadMethods.SwitchToScene;

public class MenuController {
    private Stage stage;
    @FXML
    private Label instruction;
    @FXML
    private void switchToPlay(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Game game = Game.getInstance();
        game.initGame(stage);
        game.startGame();
    }
    @FXML
    private void switchToSolve(ActionEvent event){
        SwitchToScene(event, "Solve.fxml");
    }
}
