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

    // Initializes all the buttons, textfield, and password field created in the sign-up.fxml file
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        signup.setOnAction(event -> DBUtils.signUpUser(handleSignupButton(event), "game-time.fxml", username.getText().trim(), password.getText().trim()));

        back.setOnAction(event -> DBUtils.changeScene(handleBackButton(event), "two-up.fxml", null, 0, 0));

    }

    // Switches to the login screen and loads information from the two-up.fxml file
    @FXML
    public ActionEvent handleBackButton (ActionEvent event) {
        return event;
    }

    // Registers the pressing of the Login button and connects to the database
    @FXML
    public ActionEvent handleSignupButton (ActionEvent event) {
        DBUtils.createConnection();
        return event;
    }

}