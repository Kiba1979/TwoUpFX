package com.kiba.twoupfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class GameTimeController implements Initializable {

    @FXML
    public Label rbSelection;
    @FXML
    private Button logout;
    @FXML
    private Label welcomeText;
    @FXML
    private Button spinner;
    @FXML
    private RadioButton hh;
    @FXML
    private RadioButton tt;
    @FXML
    private RadioButton ht;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logout.setOnAction(event -> DBUtils.changeScene(event, "two-up.fxml", null));

        spinner.setOnAction(event -> {
            if (hh.isSelected()) {
                rbSelection.setText("You have chosen " + hh.getText() + "!");
                hh.setSelected(false);
            } else if (tt.isSelected()) {
                rbSelection.setText("You have chosen " + tt.getText() + "!");
                tt.setSelected(false);
            } else {
                rbSelection.setText("You have chosen " + ht.getText() + "!");
                ht.setSelected(false);
            }
            spinner.setDisable(true);
        });

    }

    @FXML
    public Button logoutScreen () {
        return logout;
    }

    @FXML
    public ActionEvent activateButton (ActionEvent event) {
        spinner.setDisable(false);
        return event;
    }

    @FXML
    public void setUserInfo (String username) {
        welcomeText.setText("Let's play Two-Up, " + username + "!");
    }
}
