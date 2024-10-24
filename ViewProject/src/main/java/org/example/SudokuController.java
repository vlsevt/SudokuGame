package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class SudokuController implements Initializable {

    @FXML
    private ResourceBundle resourceBundle;
    @FXML
    private ListView<String> listView;
    @FXML
    private Label errorText;
    @FXML
    private ComboBox<Difficulty> difficultyComboBox;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Button startButton;
    private static final Logger logger = LogManager.getLogger(SudokuController.class);

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        this.resourceBundle = resources;
        if (listView == null) {
            logger.error("ListView is not initialized!");
        } else {
            updateListView(resourceBundle.getLocale()); // Initially update the ListView
        }
        setupComboBoxes();
    }

    private void setupComboBoxes() {
        difficultyComboBox.setItems(FXCollections.observableArrayList(Difficulty.values()));
        difficultyComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Difficulty difficulty) {
                if (difficulty == null) return "";
                return resourceBundle.getString(difficulty.name().toLowerCase());
            }
            @Override
            public Difficulty fromString(String string) {
                return Difficulty.valueOf(string.toUpperCase());
            }
        });
        languageComboBox.setItems(FXCollections.observableArrayList(resourceBundle.getString("en"), resourceBundle.getString("ee")));
        languageComboBox.setValue(resourceBundle.getString(resourceBundle.getLocale().toString()));  // Default value
        languageComboBox.setOnAction(this::changeLanguage);
    }

    private void changeLanguage(ActionEvent event) {
        String selectedLanguage = languageComboBox.getValue();
        Locale locale;
        if (selectedLanguage.equals("Estonian")) {
            locale = new Locale("ee", "EST");
        } else {
            locale = new Locale("en", "US");
        }
        reloadView(locale);
    }

    private void reloadView(Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Messages", locale);
            updateListView(locale);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SudokuGame.fxml"), bundle);
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(bundle.getString("game_title"));
            stage.show();
        } catch (IOException e) {
            logger.error("Failed to reload view", e);
        }
    }

    private void updateListView(Locale locale) {
        ResourceBundle listContent = ResourceBundle.getBundle("org.example.MyResources", locale);
        listView.setItems(FXCollections.observableArrayList(
                listContent.getString("s1"),
                listContent.getString("s2"),
                listContent.getString("s3")
        ));
    }

    @FXML
    protected void handleStartGameAction() {
        Difficulty selectedDifficulty = difficultyComboBox.getValue();
        if (selectedDifficulty != null) {
            logger.info("Starting game with difficulty: " + selectedDifficulty);
            loadGame(selectedDifficulty);
        } else {
            logger.warn("No difficulty selected, displaying error message.");
            errorText.setText(resourceBundle.getString("error_text"));
        }
    }

    @FXML
    protected void handleLoadGameFromSaveAction() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Sudoku files (*.sudoku)", "*.sudoku");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String path = file.getAbsolutePath();
            Locale locale;
            try (Dao<SudokuGameData> dao = FileSudokuGameDataDaoFactory.getSudokuGameDataDao(file.getParent())) {
                SudokuGameData sudokuGameData = dao.read(file.getName());
                if (Objects.equals(sudokuGameData.getLanguage(), "ee")) {
                    locale = new Locale("ee", "EST");
                } else {
                    locale = new Locale("en", "US");
                }
                ResourceBundle passedLanguage = ResourceBundle.getBundle("Messages", locale);
                System.out.println(sudokuGameData.getSudokuBoard());
                System.out.println(sudokuGameData.getDifficulty());
                loadGame(sudokuGameData.getSudokuBoard(), sudokuGameData.getDifficulty(), passedLanguage);
                logger.info("Loaded game from save file: " + path);
            } catch (Exception e) {
                logger.error("Failed to load game from save file: " + path, e);
                throw new RuntimeException(e);
            }
        } else {
            logger.warn("No file selected for loading game.");
        }
    }

    private void loadGame(SudokuBoard sudokuBoard, Difficulty difficulty, ResourceBundle language) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SudokuPlay.fxml"), language);
            loader.setControllerFactory(clazz -> {
                if (clazz == SudokuPlayController.class) {
                    SudokuPlayController controller = new SudokuPlayController();
                    controller.setResourceBundle(resourceBundle.getLocale().toString());
                    controller.setDifficulty(difficulty);
                    controller.setSudokuBoard(sudokuBoard);
                    return controller;
                }
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    logger.error("Failed to instantiate controller", e);
                    throw new RuntimeException(e);
                }
            });

            Parent root = loader.load();
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            logger.info("Game view loaded with difficulty: " + difficulty);
        } catch (IOException e) {
            logger.error("Failed to load game view", e);
        }
    }

    private void loadGame(Difficulty difficulty) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SudokuPlay.fxml"), resourceBundle);
            loader.setControllerFactory(clazz -> {
                if (clazz == SudokuPlayController.class) {
                    SudokuPlayController controller = new SudokuPlayController();
                    controller.setResourceBundle(resourceBundle.getLocale().toString());
                    controller.setDifficulty(difficulty);
                    return controller;
                }
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    logger.error("Failed to instantiate controller", e);
                    throw new RuntimeException(e);
                }
            });

            Parent root = loader.load();
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            logger.info("Game started with difficulty: " + difficulty);
        } catch (IOException e) {
            logger.error("Failed to start game", e);
        }
    }
}
