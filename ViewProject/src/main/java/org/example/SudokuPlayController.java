package org.example;

import javafx.beans.binding.Bindings;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class SudokuPlayController implements Serializable {
    private static final Logger logger = LogManager.getLogger(SudokuPlayController.class);

    @FXML
    private GridPane sudokuGrid; // Ensure this fx:id matches the GridPane in your FXML
    private SudokuGameData sudokuGameData;
    private SudokuBoard sudokuBoard;
    private String language;
    private Difficulty difficulty;

    public SudokuPlayController() {}

    public void initialize() {
        if (this.sudokuBoard == null) {
            this.sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    TextField cell = getTextFromSudokuGrid(sudokuGrid, row, col);
                    SudokuField value = sudokuBoard.getField(row, col);
                    if (cell != null) {
                        bindBidirectional(value, cell);
                        addTextLimiter(cell, 1, row, col);
                    }
                }
            }
            sudokuBoard.initializeFields();
            sudokuBoard.solveGame();
            sudokuBoard.removeFields(difficulty);
        }
        this.sudokuGameData = new SudokuGameData(this.sudokuBoard, difficulty, language);
        updateBoard();
    }

    public void setResourceBundle(String resourceBundle) {
        this.language = resourceBundle;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setSudokuBoard(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
    }

    private void updateBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = getTextFromSudokuGrid(sudokuGrid, row, col);
                if (cell != null) {
                    int value = sudokuBoard.get(row, col);
                    if (value == 0) {
                        cell.setText("");
                        cell.setEditable(true);
                    } else {
                        cell.setText(String.valueOf(value));
                        cell.setEditable(false);
                    }
                }
            }
        }
    }

    private TextField getTextFromSudokuGrid(GridPane mainGridPane, int row, int col) {
        int sectionRow = row / 3;
        int sectionCol = col / 3;
        int innerRow = row % 3;
        int innerCol = col % 3;

        for (Node section : mainGridPane.getChildren()) {
            if (GridPane.getRowIndex(section) != null && GridPane.getColumnIndex(section) != null &&
                    GridPane.getRowIndex(section) == sectionRow && GridPane.getColumnIndex(section) == sectionCol &&
                    section instanceof GridPane innerGrid) {

                for (Node cell : innerGrid.getChildren()) {
                    if (GridPane.getRowIndex(cell) != null && GridPane.getColumnIndex(cell) != null &&
                            GridPane.getRowIndex(cell) == innerRow && GridPane.getColumnIndex(cell) == innerCol &&
                            cell instanceof TextField) {
                        return (TextField) cell;
                    }
                }
            }
        }
        return null;
    }

    public void bindBidirectional(SudokuField sudokuField, TextField textField) {
        try {
            StringConverter<Number> converter = new NumberStringConverter() {
                @Override
                public String toString(Number object) {
                    return (object.intValue() == 0 ? "" : object.toString());
                }

                @Override
                public Number fromString(String string) {
                    return (string.isEmpty() ? 0 : Integer.parseInt(string));
                }
            };

            JavaBeanIntegerProperty beanProperty = JavaBeanIntegerPropertyBuilder.create()
                    .bean(sudokuField)
                    .name("value")
                    .build();
            Bindings.bindBidirectional(textField.textProperty(), beanProperty, converter);
        } catch (NoSuchMethodException e) {
            logger.error("Sudoku field does not have getter method.", e);
        }
    }

    public void addTextLimiter(final TextField tf, final int maxLength, final int row, final int col) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*") && newText.length() <= maxLength) {
                try {
                    int newValue = newText.isEmpty() ? 0 : Integer.parseInt(newText);
                    if (sudokuBoard.isValidMove(row, col, newValue)) {
                        return change;
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        };
        tf.setTextFormatter(new TextFormatter<>(filter));
    }


    @FXML
    protected void handleSaveGameAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory to Save Game");
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            TextInputDialog dialog = new TextInputDialog("defaultFileName");
            dialog.setTitle("File Name");
            dialog.setHeaderText("Enter file name for the saved game:");
            dialog.setContentText("Name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                try (Dao<SudokuGameData> dao = FileSudokuGameDataDaoFactory.getSudokuGameDataDao(selectedDirectory.getAbsolutePath())) {
                    dao.write(name + ".sudoku", this.sudokuGameData);
                } catch (Exception e) {
                    logger.error("Failed to save the file in " + selectedDirectory.getAbsolutePath(), e);
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
