package org.example;

public class FileSudokuGameDataDao extends FileDao<SudokuGameData> implements Dao<SudokuGameData> {

    public FileSudokuGameDataDao(String directory) {
        super(directory);
    }
}
