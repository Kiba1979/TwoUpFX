package com.kiba.twoupfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private HBox bottom;
    @FXML
    private VBox body;
    @FXML
    private VBox heading;
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


    // Initializes all the buttons, textfield, and password field created in the two-up.fxml file
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        login.setOnAction(event -> DBUtils.logInUser(handleLoginButton(event), "game-time.fxml", username.getText().trim(), password.getText().trim()));

        signup.setOnAction(event -> DBUtils.changeScene(handleSignUpButton(event), "sign-up.fxml", null, 0, 0));

        exit.setOnAction(this::closeGame);
    }

    // Registers the pressing of the Login button and connects to the database
    @FXML
    ActionEvent handleLoginButton (ActionEvent event) {
        DBUtils.createConnection();
        return event;
    }

    // Switches scenes which loads the information created on the sign-up.fxml
    @FXML
    ActionEvent handleSignUpButton(ActionEvent event) {
        return event;
    }

    // Closes the GUI
    @FXML
    ActionEvent closeGame (ActionEvent event) {
        System.out.println("Good Bye!");
        System.exit(0);
        return event;
    }

    // 'Setting' onAction button that changes the gui colour scheme
    @FXML
    public void settingWindow() {
        String green = "-fx-background-color: #FFCD00;";
        String yellow = "-fx-background-color: #00843D;";
        if (heading.getStyle().equals(green)) {
            heading.setStyle(yellow);
            body.setStyle(green);
            bottom.setStyle(yellow);
        } else {
            heading.setStyle(green);
            body.setStyle(yellow);
            bottom.setStyle(green);
        }
    }
}