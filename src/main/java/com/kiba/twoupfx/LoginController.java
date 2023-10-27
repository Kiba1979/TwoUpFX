package com.kiba.twoupfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        //TODO

        login.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.logInUser(handleLoginButton(event), "game-time.fxml", username.getText(), password.getText());
            }
        });

        signup.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sign-up.fxml", null, null);
            }
        });

        exit.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
    }

    @FXML
    ActionEvent handleLoginButton (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-time.fxml"));
            Parent root2 = loader.load();
            Stage stage = new Stage();
            //stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Game Time!");
            stage.setScene(new Scene(root2));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window!");
        }
        return event;
    }

    @FXML
    ActionEvent handleSignUpButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sign-up.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();
            //stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Two-Up Sign Up!");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window!");
        }
        return event;
    }

    @FXML
    ActionEvent handleExitButton (ActionEvent event) {

        return event;
    }
}
