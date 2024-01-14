package com.kiba.twoupfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Button signup;
    @FXML
    private Button exit;

    GameTimeController gtc = new GameTimeController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO

        login.setOnAction(event -> DBUtils.logInUser(handleLoginButton(event), "game-time.fxml", username.getText(), password.getText()));

        signup.setOnAction(event -> DBUtils.changeScene(handleSignUpButton(event), "sign-up.fxml", null));

        exit.setOnAction(event -> System.exit(0));
    }

    @FXML
    ActionEvent handleLoginButton (ActionEvent event) {
        return event;
    }

    @FXML
    ActionEvent handleSignUpButton(ActionEvent event) {
        return event;
    }

    @FXML
    ActionEvent closeGame (ActionEvent event) {
        return event;
    }

}