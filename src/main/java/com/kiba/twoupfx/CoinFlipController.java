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

    private String face = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        close.setOnAction(this::gameReturn);

        try {
            coinRotation(coinOne);
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

    public Image coinView () {
        Image view;
        if (face.equals("Heads")) {
            view = new Image("AusPennyHeads.png");
        } else {
            view = new Image("AusPennyTails.png");
        }
        return view;
    }

    public void coinRotation (ImageView image) {
        image.setImage(coinView());

        TranslateTransition translate1 = new TranslateTransition();
        translate1.setNode(image);
        translate1.setDuration(Duration.millis(1000));
        translate1.setCycleCount(2);
        translate1.setByY(-250);
        translate1.setAutoReverse(true);
        translate1.play();

        RotateTransition rotate1 = new RotateTransition();
        rotate1.setNode(image);
        rotate1.setDuration(Duration.millis(500));
        rotate1.setCycleCount(4);
        rotate1.setInterpolator(Interpolator.LINEAR);
        rotate1.setByAngle(360);
        rotate1.setAxis(Rotate.X_AXIS);
        rotate1.play();

    }



}
