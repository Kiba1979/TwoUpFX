package com.kiba.twoupfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class GameTimeController implements Initializable {

    @FXML
    private Button logout;
    @FXML
    private Label welcomeText;
    @FXML
    private Label username;
    @FXML
    private Button spinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "two-up.fxml", "Two-Up Login", null);
            }
        });

        spinner.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

    }

    public void setUserInfo (String username) {
        welcomeText.setText("Let's play Two-Up, " + username);
    }
}
