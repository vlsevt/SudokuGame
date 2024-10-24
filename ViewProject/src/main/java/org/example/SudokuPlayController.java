package org.example;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class SudokuPlayController implements Serializable {
    private static final Logger logger = LogManager.getLogger(SudokuPlayController.class);

    @FXML
    private GridPane sudokuGrid;
    private SudokuGameData sudokuGameData;
    private SudokuBoard sudokuBoard;
    private String language;
    private Difficulty difficulty;

    public SudokuPlayController() {}

    public void initialize() {
        if (this.sudokuBoard == null) {
            this.sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
            sudokuBoard.initializeFields();
            sudokuBoard.solveGame();
            sudokuBoard.removeFields(difficulty);
            initializer();
        } else {
            initializer();
        }
        this.sudokuGameData = new SudokuGameData(this.sudokuBoard, difficulty, language);
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

    public void initializer() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = getTextFromSudokuGrid(sudokuGrid, row, col);
                SudokuField value = sudokuBoard.getField(row, col);
                if (cell != null) {
                    bindBidirectional(value, cell);
                    addTextLimiter(cell, row, col);
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
            IntegerProperty valueProperty = new SimpleIntegerProperty(sudokuField.getValue());

            StringConverter<Number> converter = new StringConverter<>() {
                @Override
                public String toString(Number value) {
                    return value == null || value.intValue() == 0 ? "" : value.toString();
                }

                @Override
                public Number fromString(String text) {
                    return (text == null || text.isEmpty()) ? 0 : Integer.parseInt(text);
                }
            };

            textField.textProperty().bindBidirectional(valueProperty, converter);
        }


        public void addTextLimiter(final TextField tf, final int row, final int col) {
            UnaryOperator<TextFormatter.Change> filter = change -> {
                if (!change.isContentChange()) {
                    return change;
                }

                String newText = change.getControlNewText();

                if (newText.equals("0")) {
                    tf.setStyle("-fx-background-color: lightcoral;");
                    sudokuBoard.set(row, col, 0);
                    return null;
                }

                // if is empty - ok
                if (newText.trim().isEmpty()) {
                    sudokuBoard.set(row, col, 0);
                    return change;
                }


                // if not a number - not ok
                if (!newText.chars().allMatch(Character::isDigit)) {
                    tf.setStyle("-fx-background-color: lightcoral;");
                    sudokuBoard.set(row, col, 0);
                    return null;
                }

                // if not in accepted range or not valid move - not ok
                int newValue = Integer.parseInt(newText);
                if (newValue == 0) {
                    change.setText("");
                } else if (newValue < 0 || newValue >= 10 || !sudokuBoard.isValidMove(row, col, newValue)) {
                    tf.setStyle("-fx-background-color: lightcoral;");
                    sudokuBoard.set(row, col, 0);
                    return null;
                }

                sudokuBoard.set(row, col, Integer.parseInt(change.getText()));
                if (!sudokuBoard.solveable()) {
                    tf.setStyle("-fx-background-color: lightcoral;");
                    sudokuBoard.set(row, col, 0);
                    return null;
                }
                tf.setStyle("-fx-background-color: transparent;");
                tf.setEditable(false);
                return change;
            };
            tf.setTextFormatter(new TextFormatter<>(filter));
        }


        @FXML
        protected void handleSaveGameAction() {
            this.sudokuGameData.setSudokuBoard(sudokuBoard);
            this.sudokuGameData.setDifficulty(difficulty);
            this.sudokuGameData.setLanguage(language);
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


