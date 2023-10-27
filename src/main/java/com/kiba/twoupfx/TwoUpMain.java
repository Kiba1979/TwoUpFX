package com.kiba.twoupfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TwoUpMain extends Application {

    @Override
    public void start (Stage loginStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(TwoUpMain.class.getResource("two-up.fxml"));
        Scene scene = new Scene(loader.load(), 500, 500);
        loginStage.setTitle("Two-Up Login!");
        loginStage.setScene(scene);
        loginStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
