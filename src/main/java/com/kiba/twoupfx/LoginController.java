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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        login.setOnAction(event -> DBUtils.logInUser(handleLoginButton(event), "game-time.fxml", username.getText().trim(), password.getText().trim()));

        signup.setOnAction(event -> DBUtils.changeScene(handleSignUpButton(event), "sign-up.fxml", null));

        exit.setOnAction(this::closeGame);
    }

    @FXML
    ActionEvent handleLoginButton (ActionEvent event) {
        DBUtils.createConnection();
        return event;
    }

    @FXML
    ActionEvent handleSignUpButton(ActionEvent event) {
        return event;
    }

    @FXML
    ActionEvent closeGame (ActionEvent event) {
        System.out.println("Good Bye!");
        System.exit(0);
        return event;
    }

}