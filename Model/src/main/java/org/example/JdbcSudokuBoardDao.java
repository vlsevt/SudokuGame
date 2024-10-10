package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {
    private static final Logger logger = LogManager.getLogger(JdbcSudokuBoardDao.class);
    private final Connection connection;

    public JdbcSudokuBoardDao() {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "220203";
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(jdbcUrl, username, password);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            logger.error("Could not find driver", e);
            throw new RuntimeException("Could not find driver", e);
        } catch (SQLException e) {
            logger.error("Could not get connection to the database", e);
            throw new RuntimeException("Could not get connection to the database", e);
        }
    }

    @Override
    public SudokuBoard read(String name) {
        SudokuBoard sudokuBoard;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM sudoku_board_field"
                            + " WHERE sudoku_board_name = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Class<?> sudokuSolverClass = Class.forName(resultSet.getString("sudoku_solver_name"));
                Constructor<?> sudokuSolverConstructor = sudokuSolverClass.getConstructor();
                sudokuBoard = new SudokuBoard((SudokuSolver) sudokuSolverConstructor.newInstance());
                resultSet.previous();
                while (resultSet.next()) {
                    int row = Integer.parseInt(resultSet.getString("field_row"));
                    int column = Integer.parseInt(resultSet.getString("field_column"));
                    int value = Integer.parseInt(resultSet.getString("field_value"));
                    sudokuBoard.set(row, column, value);
                }
                return sudokuBoard;
            }
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException("SQL Exception", e);
        } catch (ClassNotFoundException e) {
            logger.error("Could not find class of SudokuSolver", e);
            throw new RuntimeException("Could not find class of SudokuSolver", e);
        } catch (NoSuchMethodException e) {
            logger.error("Could not find constructor for SudokuSolver", e);
            throw new RuntimeException("Could not find constructor for SudokuSolver", e);
        } catch (InvocationTargetException e) {
            logger.error("Underlying constructor threw an error", e);
            throw new RuntimeException("Underlying constructor threw an error", e);
        } catch (InstantiationException e) {
            logger.error("Class that declares the underlying constructor represents an abstract class", e);
            throw new RuntimeException("Class that declares the underlying constructor represents an abstract class",
                    e);
        } catch (IllegalAccessException e) {
            logger.error("Constructor object is enforcing Java language access control and the underlying"
                    + " constructor is inaccessible", e);
            throw new RuntimeException("Constructor object is enforcing Java language access control and the underlying"
                    + " constructor is inaccessible", e);
        }
        return null;
    }

    @Override
    public void write(String name, SudokuBoard obj) {
        SudokuField[][] sudokuFields = obj.board;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                writeField(name, obj, row, column, sudokuFields[row][column].getValue());
            }
        }
        //TODO: commit here
    }

    public void writeField(String name, SudokuBoard obj, int row, int column, int value) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO sudoku_board_field (sudoku_board_name,"
                    + " field_row, field_column, field_value, sudoku_solver_name) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, row);
            preparedStatement.setInt(3, column);
            preparedStatement.setInt(4, value);
            preparedStatement.setString(5, obj.getSudokuSolver().getClass().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException("SQL Exception", e);
        }
    }

    @Override
    public List<String> names() {
        List<String> names = new ArrayList<>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT DISTINCT sudoku_board_name"
                    + " FROM sudoku_board_field");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                names.add(resultSet.getString("sudoku_board_name"));
            }
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException("SQL Exception", e);
        }
        return names;
    }

    public void delete(String name) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM sudoku_board_field"
                    + " WHERE sudoku_board_name = ?");
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL Exception", e);
            throw new RuntimeException("SQL Exception", e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.commit();
        connection.close();
    }

    public static void main(String[] args) {
        try (JdbcSudokuBoardDao jdbcSudokuBoardDao = new JdbcSudokuBoardDao()) {
            System.out.println(jdbcSudokuBoardDao.names());
            jdbcSudokuBoardDao.write("123", new SudokuBoard(new BacktrackingSudokuSolver()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
