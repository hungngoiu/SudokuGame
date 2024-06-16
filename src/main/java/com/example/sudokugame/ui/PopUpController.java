package com.example.sudokugame.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class PopUpController {
    private SudokuController controller;

    public void setMainController(SudokuController controller) {
        this.controller = controller;
    }
    @FXML
    public void switchToMenu(ActionEvent event){
        if(controller != null){
            controller.backToMenu();
        }
        close(event);
    }

    @FXML
    public void newGame(ActionEvent event){
        if(controller != null){
            controller.newGame();
        }
        close(event);
    }

    @FXML
    private void close(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
