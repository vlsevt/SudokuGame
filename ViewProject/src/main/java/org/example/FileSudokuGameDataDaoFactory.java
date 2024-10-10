package org.example;

public class FileSudokuGameDataDaoFactory {
    public static Dao<SudokuGameData> getSudokuGameDataDao(String directory) {
        return new FileSudokuGameDataDao(directory);
    }
}
