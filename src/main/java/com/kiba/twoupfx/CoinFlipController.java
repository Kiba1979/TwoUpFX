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
import java.util.Objects;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        close.setOnAction(this::gameReturn);

        flipCoin();
        }

    @FXML
    private void flipCoin () {

        coinTranslate(coinOne);
        coinTranslate(coinTwo);

        coinView(coinOne, leftCoin());
        coinView(coinTwo, rightCoin());
    }

    @FXML
    private void coinView (ImageView coinImageView, Coin coin) {

        String imagePath = coin.isHeads(coin.flip()) ? "img/AusPennyHeads.png" : "img/AusPennyTails.png";

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        coinImageView.setImage(image);
    }

    @FXML
    public void coinLabels (Coin coin1, Coin coin2) {
        coinLeft.setText(coin1.toString());
        coinRight.setText(coin2.toString());
    }

    @FXML
    public Coin leftCoin () {
        return new Coin();
    }

    @FXML
    public Coin rightCoin () {
        return new Coin();
    }

    @FXML
    private void gameReturn(ActionEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void coinTranslate (ImageView image) {
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
