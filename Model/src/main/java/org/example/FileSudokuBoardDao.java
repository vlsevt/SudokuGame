package org.example;

public class FileSudokuBoardDao extends FileDao<SudokuBoard> implements Dao<SudokuBoard> {

    public FileSudokuBoardDao(String directory) {
        super(directory);
    }
}
