package org.example;

import java.io.Serializable;
import java.util.ResourceBundle;

public class SudokuGameData implements Serializable {
    private static final long serialVersionUID = 1L;

    private SudokuBoard sudokuBoard;
    private Difficulty difficulty;
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public SudokuGameData(SudokuBoard sudokuBoard, Difficulty difficulty, String language) {
        this.sudokuBoard = sudokuBoard;
        this.difficulty = difficulty;
        this.language = language;
    }

    // Getters and setters
    public SudokuBoard getSudokuBoard() {
        return sudokuBoard;
    }

    public void setSudokuBoard(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
