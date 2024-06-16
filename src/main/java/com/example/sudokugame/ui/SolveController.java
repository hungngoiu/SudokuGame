package com.example.sudokugame.ui;

import com.example.sudokugame.util.LoadMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import static com.example.sudokugame.util.LoadMethods.SwitchToScene;

public class SolveController {
    @FXML
    private void backToMenu(ActionEvent event) {
        SwitchToScene(event, "Menu.fxml");
    }
}
