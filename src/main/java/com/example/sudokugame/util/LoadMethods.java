package com.example.sudokugame.util;

import com.example.sudokugame.core.Level;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

import static com.example.sudokugame.util.Constants.RESOURCE_ROOT;

public class LoadMethods {
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
}
