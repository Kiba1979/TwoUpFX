package com.kiba.twoupfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Button back;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button signup;
    @FXML
    private int wins;
    @FXML
    private int played;
    @FXML
    private double percent;

    public SignUpController(int wins, int played, double percent) {
        this.wins = wins;
        this.played = played;
        this.percent = percent;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        back.setOnAction(event -> DBUtils.changeScene(handleBackButton(event), "two-up.fxml", null));

        signup.setOnAction(event -> DBUtils.signUpUser(handleSignupButton(event), "game-time.fxml", username.getText(), password.getText()));
    }

    @FXML
    public ActionEvent handleBackButton (ActionEvent event) {
        return event;
    }

    @FXML
    public ActionEvent handleSignupButton (ActionEvent event) {
        return event;
    }

}