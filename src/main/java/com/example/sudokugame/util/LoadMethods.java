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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.example.sudokugame.util.Constants.*;
import static com.example.sudokugame.util.Constants.LEVELS.*;
import static com.example.sudokugame.util.HelpMethods.SolveSudoku;

public class LoadMethods {
    private static Stage stage;
    private static Scene scene;
    public static Level LoadLevel(Constants.LEVELS levels){
        Level level = null;
        if(levels == EASY){
            level = new Level(generateSudokuPuzzles(30));
        } else if (levels == MEDIUM){
            level = new Level(generateSudokuPuzzles(25));
        } else if (levels == HARD){
            level = new Level(generateSudokuPuzzles(15));
        } else if (levels == EVIL){
            level = new Level(generateSudokuPuzzles(5));
        }
        return level;
    }
    public static int[][] generateSudokuPuzzles(int numCells){
        int[][] puzzles = new int[SUDOKU_SIZE][SUDOKU_SIZE];
        int[][] result = new int[SUDOKU_SIZE][SUDOKU_SIZE];
        SolveSudoku(puzzles, result);
        fillPuzzleFromResult(puzzles, result, numCells);
        return puzzles;
    }

    private static void fillPuzzleFromResult(int[][] puzzles, int[][] result, int num) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < SUDOKU_SIZE * SUDOKU_SIZE; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        for (int i = 0; i < num; i++) {
            int pos = positions.get(i);
            int row = pos / SUDOKU_SIZE;
            int col = pos % SUDOKU_SIZE;
            puzzles[row][col] = result[row][col];
        }
    }
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
