package com.kiba.twoupfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
    @FXML
    private int gameWins;
    @FXML
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

    @FXML
    private void initializeTableView () throws Exception {

        player.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        wins.setCellValueFactory(cellData -> cellData.getValue().winsProperty().asObject());
        played.setCellValueFactory(cellData -> cellData.getValue().playedProperty().asObject());
        percent.setCellValueFactory(cellData -> cellData.getValue().percentProperty().asObject());

        ObservableList<PlayerLeaderboard> topTenList = DBUtils.topTenList();
        populateTable(topTenList);
    }

    private void populateTable (ObservableList<PlayerLeaderboard> topTenList) {
        leaderboard.setItems(topTenList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            initializeTableView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        logout.setOnAction(event -> DBUtils.changeScene(logoutScreen(event), "two-up.fxml", null));

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
    private ActionEvent handleSpinnerButton (ActionEvent event) throws SQLException, ClassNotFoundException {
        DBUtils.topTenList();
        return event;
    }

    @FXML
    public void setUserInfo (String username) {
        welcomeText.setText("Let's play Two-Up, " + username + "!");
    }

    @FXML
    public void currentPlayerStats (int gameWins, int playedGames, double winPercent) {
        gamesWon.setText("Wins: " + gameWins);
        gamesPlayed.setText("Played: " + playedGames);
        winPercentage.setText("Win Percent: " + winPercent + "%");
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
        PlayerLeaderboard pl = new PlayerLeaderboard();
        System.out.println(pl.getUsername());
        if (gameResults()) {
            rbSelection.setText("You win!");
            gameWins++;
        } else {
            rbSelection.setText("You are not a winner this time.");
        }
        totalGames++;
        double percent = Double.parseDouble(df.format(((double) gameWins / totalGames) * 100));
        currentPlayerStats(gameWins, totalGames, percent);
        DBUtils.updatePlayerStats(gameWins, totalGames, percent);
        return event;
    }

    public void topTenTableview () {

        leaderboard.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        leaderboard.getItems().add(new PlayerLeaderboard());

    }


}
