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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        signup.setOnAction(event -> DBUtils.signUpUser(handleSignupButton(event), "game-time.fxml", username.getText(), password.getText()));

        back.setOnAction(event -> DBUtils.changeScene(handleBackButton(event), "two-up.fxml", null));

    }

    @FXML
    public ActionEvent handleBackButton (ActionEvent event) {
        return event;
    }

    @FXML
    public ActionEvent handleSignupButton (ActionEvent event) {
        DBUtils.createConnection();
        return event;
    }

}