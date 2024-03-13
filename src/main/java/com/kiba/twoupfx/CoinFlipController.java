package com.kiba.twoupfx;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class CoinFlipController implements Initializable {

    @FXML
    private Label coinLeft;
    @FXML
    private Label coinRight;
    @FXML
    private Button close;
    @FXML
    private ImageView coinOne;
    @FXML
    private ImageView coinTwo;

    String heads = "img/AusPennyHeads.png";
    String tails = "img/AusPennyTails.png";

    private String face = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        close.setOnAction(this::gameReturn);

        try {
            coinOneRotation();
            coinTwoRotation();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getFace () {
        return face;
    }

    public int result() {
        return (int) (Math.random() * 2);
    }

    public String flip (int coin) {
        if (coin == 0) {
            face = "Heads";
        } else {
            face = "Tails";
        }
        return face;
    }

    @FXML
    public void coinLabels (String coin1, String coin2) {
        coinLeft.setText(String.valueOf(coin1));
        coinRight.setText(String.valueOf(coin2));
    }

    public String toString () {
        return getFace();
    }

    @FXML
    private void gameReturn(ActionEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void coinOneRotation () {

        // translate1
        TranslateTransition translate1 = new TranslateTransition();
        RotateTransition rotate1 = new RotateTransition();
        translate1.setNode(coinOne);
        translate1.setDuration(Duration.millis(1000));
        translate1.setCycleCount(2);
        translate1.setByY(-250);
        translate1.setAutoReverse(true);
        translate1.play();

        // rotate1

        rotate1.setNode(coinOne);
        rotate1.setDuration(Duration.millis(500));
        rotate1.setCycleCount(4);
        rotate1.setInterpolator(Interpolator.LINEAR);
        rotate1.setByAngle(360);
        rotate1.setAxis(Rotate.X_AXIS);
        rotate1.play();
    }

    @FXML
    public void coinTwoRotation () {

        // translate2
        TranslateTransition translate2 = new TranslateTransition();
        RotateTransition rotate2 = new RotateTransition();
        translate2.setNode(coinTwo);
        translate2.setDuration(Duration.millis(1000));
        translate2.setCycleCount(2);
        translate2.setByY(-250);
        translate2.setAutoReverse(true);
        translate2.play();

        // rotate2
        rotate2.setNode(coinTwo);
        rotate2.setDuration(Duration.millis(500));
        rotate2.setCycleCount(4);
        rotate2.setInterpolator(Interpolator.LINEAR);
        rotate2.setByAngle(360);
        rotate2.setAxis(Rotate.X_AXIS);
        rotate2.play();
    }

}
