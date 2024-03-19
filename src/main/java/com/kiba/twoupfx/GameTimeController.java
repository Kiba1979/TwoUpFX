package com.kiba.twoupfx;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class GameTimeController implements Initializable {

    @FXML
    private Label playerName;
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
    private TableView<PlayerLeaderboard> leaderboard;
    @FXML
    private TableColumn<PlayerLeaderboard, String> player;
    @FXML
    private TableColumn<PlayerLeaderboard, Integer> wins;
    @FXML
    private TableColumn<PlayerLeaderboard, Integer> played;
    @FXML
    private TableColumn<PlayerLeaderboard, Double> percent;
    // Sets a limit on how many decimal points the win percentage goes to
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            initializeTableView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        logout.setOnAction(event -> DBUtils.closeConnection(logoutScreen(event), "two-up.fxml", null, 0, 0, 0.00));

        spinner.setOnAction(event -> {
            try {
                winTracker();
                initializeTableView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    // Initializes the top ten leaderboard, getting information through PlayerLeaderboard.java file(Class)
    @FXML
    private void initializeTableView () throws Exception {

        player.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        wins.setCellValueFactory(cellData -> cellData.getValue().winsProperty().asObject());
        played.setCellValueFactory(cellData -> cellData.getValue().playedProperty().asObject());
        percent.setCellValueFactory(cellData -> cellData.getValue().percentProperty().asObject());

        ObservableList<PlayerLeaderboard> topTenList = DBUtils.topTenList();
        populateTable(topTenList);
    }

    // Populates the TableView from the
    private void populateTable (ObservableList<PlayerLeaderboard> topTenList) {
        leaderboard.setItems(topTenList);
    }

    // Registers the logout button being pressed. Cause the program to close database connections from the DBUtils.java file
    // and then loads the two-up.fxml file information
    @FXML
    public ActionEvent logoutScreen (ActionEvent event) {
        return event;
    }

    // Registers the pressing of the spinner button and starts the game to find out whether you have won or not
    @FXML
    private ActionEvent handleSpinnerButton (ActionEvent event) {
        return event;
    }

    // Checks to see if you have selected any of the radio buttons
    // If yes, it activates the spinner button
    @FXML
    public ActionEvent activateButton (ActionEvent event) {
        RadioButton rbChoice = (RadioButton) choices.getSelectedToggle();
        if (rbChoice != null) {
            rbSelection.setText(rbChoice.getText());
            spinner.setDisable(false);
        } else {
            spinner.setDisable(true);
        }
        return event;
    }

    // Loads information into the game-time.fxml scene
    @FXML
    public void setUserInfo (String username, int wins, int played, double percent) {
        welcomeText.setText("Let's play Two-Up, " + name(username) + "!");
        gamesWon.setText(String.valueOf(wins));
        gamesPlayed.setText(String.valueOf(played));
        winPercentage.setText(String.valueOf(percent));
    }



    // A method that checks whether your radio button choice and coin flips are true or not.
    // It then deselects the selected radio button
    boolean gameResults () throws Exception {
        CoinFlipController cfc = new CoinFlipController();
        int coin1 = cfc.result();
        int coin2 = cfc.result();
        coinFlip(coin1, coin2);
        boolean winLoss = false;
        if ((coin1 == 0 && coin2 == 0) && hh.isSelected()) {
            hh.setSelected(false);
            winLoss = true;
        } else if ((coin1 == 1 && coin2 == 1) && tt.isSelected()) {
            tt.setSelected(false);
            winLoss = true;
        } else if (((coin1 == 0 && coin2 == 1) || (coin1 == 1 && coin2 == 0)) && ht.isSelected()) {
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
        return winLoss;
    }

    // This method takes the stored information in setUserInfo and checks to see if you have won.
    // It then updates setUserInfo and the database with the new information.
    public void winTracker () throws Exception {
        String username = name(playerName.getText());
        int gameWins = Integer.parseInt(gamesWon.getText());
        int totalGames = Integer.parseInt(gamesPlayed.getText());
        if (gameResults()) {
            rbSelection.setText("You win, " + username + "!");
            gameWins++;
        } else {
            rbSelection.setText("Not a winner, " + username + ".");
        }
        totalGames++;
        double percent = setWinPercentage(gameWins, totalGames);
        setUserInfo(username, gameWins, totalGames, percent);
        DBUtils.updateDB(username, gameWins, totalGames, percent);
    }

    // A method that works out a players win percentage
    public double setWinPercentage (int wins, int played) {
        return Double.parseDouble(df.format(((double) wins / played) * 100));
    }

    // It is here to store a players name
    public String name (String name) {
        playerName.setText(name);
        return name;
    }

    public void coinFlip (int coin1, int coin2) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("coin-flip.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        CoinFlipController cfc = loader.getController();

        try {
            cfc.coinLabels(cfc.flip(coin1), cfc.flip(coin2));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        stage.setScene(new Scene(root));
        stage.show();
    }

}
