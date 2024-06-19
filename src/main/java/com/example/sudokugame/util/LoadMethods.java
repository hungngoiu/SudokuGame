package com.example.sudokugame.util;

import com.example.sudokugame.Main;
import com.example.sudokugame.core.Game;
import com.example.sudokugame.core.Level;
import com.example.sudokugame.ui.PopUpController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

import static com.example.sudokugame.util.Constants.RESOURCE_ROOT;
import static com.example.sudokugame.util.Constants.SUDOKU_SIZE;

public class LoadMethods {
    private static Stage stage;
    private static Scene scene;
    public static int[][] LoadSudokuPuzzles(){
        int[][] values = new int[SUDOKU_SIZE][SUDOKU_SIZE];
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(null);
        try {
            Scanner fileReader = new Scanner(file);
            for (int y = 0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    values[y][x] = fileReader.nextInt();
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot read file");
            e.printStackTrace();
        }
        return values;
    }

    public static Level LoadLevel(int level_no) {
        Level level = null;
        URL url = LoadMethods.class.getResource(RESOURCE_ROOT + "levels/" + level_no + ".txt");
        File file = new File(url.getFile());
        try {
            Scanner fileReader = new Scanner(file);
            int[][] values = new int[9][9];
            for (int y = 0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    values[y][x] = fileReader.nextInt();
                }
            }
            level = new Level(values);
        } catch (IOException e) {
            System.out.println("Cannot read file");
            e.printStackTrace();
        }
        return level;
    }
    public static void SwitchToScene(ActionEvent event, String fileName) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(RESOURCE_ROOT + fileName));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
