package org.example;

public class SudokuBoardDaoFactory {
    public static FileSudokuBoardDao getFileSudokuBoardDao(String directory) {
        return new FileSudokuBoardDao(directory);
    }

    public static JdbcSudokuBoardDao getJdbcSudokuBoardDao() {
        return new JdbcSudokuBoardDao();
    }
}
