package com.kiba.twoupfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class GameTimeController implements Initializable {

    @FXML
    private Label welcomeText;
    @FXML
    private Label rbSelection;
    @FXML
    private Label gamesWon;
    @FXML
    private Label gamesPlayed;
    @FXML
    private Label winPercentage;
    @FXML
    private Button logout;
    @FXML
    private Button spinner;
    @FXML
    private ToggleGroup choices;
    @FXML
    private RadioButton hh;
    @FXML
    private RadioButton tt;
    @FXML
    private RadioButton ht;

    private int gameWins;
    private int totalGames;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @FXML
    private TableView<PlayerLeaderboard> leaderboard;
    @FXML
    private TableColumn<PlayerLeaderboard, String> player;
    @FXML
    private TableColumn<PlayerLeaderboard, Integer> wins;
    @FXML
    private TableColumn<PlayerLeaderboard, Integer> played;
    @FXML
    private TableColumn<PlayerLeaderboard, Double> percent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logout.setOnAction(event -> DBUtils.changeScene(logoutScreen(event), "two-up.fxml", null));
        logout.setOnAction(event -> DBUtils.closeConnection());

        spinner.setOnAction(event -> activateButton(winTracker(event)));

    }

    @FXML
    public ActionEvent logoutScreen (ActionEvent event) {
        return event;
    }

    @FXML
    public ActionEvent activateButton (ActionEvent event) {

        RadioButton rbChoice = (RadioButton) choices.getSelectedToggle();
        if (rbChoice != null) {
            spinner.setDisable(false);
            rbSelection.setText(rbChoice.getText());
        }

        return event;
    }

    @FXML
    private ActionEvent handleSpinnerButton (ActionEvent event) {
        return event;
    }



    @FXML
    private void addTopTen (ActionEvent event, String username, int wins, int played, double percent) {
        PlayerLeaderboard pl = new PlayerLeaderboard(username, wins, played, percent);


    }

    @FXML
    public void setUserInfo (String username) {
        welcomeText.setText("Let's play Two-Up, " + username + "!");
    }

    @FXML
    public void currentPlayerStats (int gameWins, int playedGames, double winPercent) {
        gamesWon.setText("Wins: " + gameWins);
        gamesPlayed.setText("Played: " + playedGames);
        winPercentage.setText("Win %: " + winPercent);
    }

    boolean gameResults () {
        Random random = new Random();
        int coin1 = random.nextInt(2);
        int coin2 = random.nextInt(2);
        boolean winLoss = false;
        if ((coin1 == 0 && coin2 == 0) && hh.isSelected()) {
            hh.setSelected(false);
            winLoss = true;
        } else if ((coin1 == 1 && coin2 == 1) && tt.isSelected()) {
            tt.setSelected(false);
            winLoss = true;
        } else if (((coin1 == 1 && coin2 == 0) || (coin1 == 0 && coin2 == 1)) && ht.isSelected()) {
            ht.setSelected(false);
            winLoss = true;
        } else {
            if (hh.isSelected()) {
                hh.setSelected(false);
            } else if (tt.isSelected()) {
                tt.setSelected(false);
            } else {
                ht.setSelected(false);
            }
        }
        spinner.setDisable(true);
        return winLoss;
    }

    public ActionEvent winTracker (ActionEvent event) {
        if (gameResults()) {
            rbSelection.setText("You win!");
            gameWins++;
        } else {
            rbSelection.setText("You are not a winner this time.");
        }
        totalGames++;
        double winPercent = ((double) gameWins / totalGames) * 100;
        currentPlayerStats(gameWins, totalGames, Double.parseDouble(df.format(winPercent)));

        return event;
    }



}
