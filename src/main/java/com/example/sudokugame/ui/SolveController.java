package com.example.sudokugame.ui;

import com.example.sudokugame.util.LoadMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SolveController {
    @FXML
    private void backToMenu(ActionEvent event) {
        LoadMethods.switchToScene(event, "Menu.fxml");
    }
}
