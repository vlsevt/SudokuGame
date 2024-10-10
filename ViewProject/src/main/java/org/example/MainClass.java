package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainClass extends Application {
    private static final Logger logger = LogManager.getLogger(MainClass.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            Locale.setDefault(new Locale("en", "US"));
            ResourceBundle bundle = ResourceBundle.getBundle("Messages", Locale.getDefault());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SudokuGame.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();

            primaryStage.setTitle(bundle.getString("game_title"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            logger.error("I/O Exception with SudokuGame.fxml file", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
