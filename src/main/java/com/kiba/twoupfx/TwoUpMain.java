package com.kiba.twoupfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class TwoUpMain extends Application {

    // Loads GUI information from two-up.fxml file
    @Override
    public void start (Stage loginStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(TwoUpMain.class.getResource("two-up.fxml"));
        Scene scene = new Scene(loader.load());
        loginStage.setTitle("Game Time!");
        loginStage.setScene(scene);
        loginStage.show();
    }

// Starts the program
    public static void main(String[] args) {
        launch(args);
    }
}