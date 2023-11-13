package com.kiba.twoupfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.ResourceBundle;

public class GameTimeController implements Initializable {


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
    private Label welcomeText;
    @FXML
    private Button spinner;
    @FXML
    private RadioButton hh;
    @FXML
    private RadioButton tt;
    @FXML
    private RadioButton ht;
    @FXML
    private int winner;
    @FXML
    private int totalGames;
    @FXML
    private TableView leaderboard;
    @FXML
    private TableColumn player;
    @FXML
    private TableColumn wins;
    @FXML
    private TableColumn played;
    @FXML
    private TableColumn percent;


    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logout.setOnAction(event -> DBUtils.changeScene(event, "two-up.fxml", null));

        spinner.setOnAction(event -> winTracker());

    }

    @FXML
    public ActionEvent logoutScreen (ActionEvent event) {
        return event;
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

    @FXML
    public void leaderBoard (String playerName, int winner, int totalGames, double winPercent) {
        player.setText(String.valueOf(playerName));
        gamesWon.setText(String.valueOf(winner));
        gamesPlayed.setText(String.valueOf(totalGames));
        winPercentage.setText(String.valueOf(winPercent));
    }

    @FXML
    public void currentPlayerStats (int gameWins, int playedGames, double winsPercent) {
        gamesWon.setText("Games Won: " + gameWins);
        gamesPlayed.setText("Games Played: " + playedGames);
        winPercentage.setText("Win Percentage: " + winsPercent + "%");
    }
    boolean gameResults (Label rbSelection) {
        Random random = new Random();
        int coin1 = random.nextInt(2);
        int coin2 = random.nextInt(2);
        boolean winLoss = false;
        if ((coin1 == 0 && coin2 == 0) && hh.isSelected()) {
            rbSelection.setText("You win!");
            hh.setSelected(false);
            winLoss = true;
        } else if ((coin1 == 1 && coin2 == 1) && tt.isSelected()) {
            rbSelection.setText("You win!");
            tt.setSelected(false);
            winLoss = true;
        } else if (((coin1 == 1 && coin2 == 0) || (coin1 == 0 && coin2 == 1)) && ht.isSelected()) {
            rbSelection.setText("You win!");
            ht.setSelected(false);
            winLoss = true;
        } else {
            rbSelection.setText("You are not a winner this time.");
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

    public void winTracker () {
        if (gameResults(rbSelection)) {
            winner++;
        }
        totalGames++;
        double winningPercent = (((double) winner / totalGames) * 100);
        currentPlayerStats(winner, totalGames, Double.parseDouble(df.format(winningPercent)));
    }


}
