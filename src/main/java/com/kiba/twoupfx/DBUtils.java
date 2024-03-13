package com.kiba.twoupfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DBUtils {

    private static final String db_connect = "jdbc:mysql://127.0.0.1:3306/adams_two_up";
    private static final String db_username = "root";
    private static final String db_password = "root";
    private static Connection connection;
    private static PreparedStatement psInsert;
    private static PreparedStatement psUpdate;
    private static PreparedStatement psCheckUserExists;
    private static ResultSet resultSet;
    private static Parent root;

    // Creates a connection to the database
    public static void createConnection () {
        try {
            connection = DriverManager.getConnection(db_connect, db_username, db_password);
            System.out.println("Database connection successful.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Closes all connections to the database
    public static void closeConnection (ActionEvent event, String fxmlFile, String username, int wins, int played, double percent) {
        {
            if (resultSet != null) {
                try {
                    resultSet.close();
                    if (resultSet.isClosed()) {
                        System.out.println("resultSet has closed!");
                    } else {
                        System.out.println("resultSet was never opened!");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                    if (psCheckUserExists.isClosed()) {
                        System.out.println("psCheckUserExists has closed!");
                    } else {
                        System.out.println("psCheckUserExists was never opened!");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psUpdate != null) {
                try {
                    psUpdate.close();
                    if (psUpdate.isClosed()) {
                        System.out.println("psForeignInsert has closed!");
                    } else {
                        System.out.println("psForeignInsert was never opened!");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                    if (psInsert.isClosed()) {
                        System.out.println("psInsert has closed!");
                    }else {
                        System.out.println("psInsert was never opened!");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psUpdate != null) {
                try {
                    psUpdate.close();
                    if (psUpdate.isClosed()) {
                        System.out.println("psUpdate has closed!");
                    }else {
                        System.out.println("psUpdate was never opened!");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                    if (connection.isClosed()) {
                        System.out.println("connection has closed!");
                    } else {
                        System.out.println("connection was never opened!");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Exiting game!");
            changeScene(event, fxmlFile, username, wins, played);
        }
    }

    // Changes GUI scenes when method is called
    // Method that changes between the different FXML scenes
    public static void changeScene (ActionEvent event, String fxmlFile, String username, int wins, int played) {
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                GameTimeController gtc = loader.getController();
                gtc.setUserInfo(username, wins, played, gtc.setWinPercentage(wins, played));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(DBUtils.class.getResource(fxmlFile)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(fxmlFile);
        //assert root != null;
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Connects and updates the database when someone signs up to the game
    public static void signUpUser (ActionEvent event, String fxmlFile, String username, String password) {
        int wins = 0;
        int played = 0;
        double percent = 0.00;
        try {
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();
            if (resultSet.next()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            } else {
                System.out.println("User has been created!");
                psInsert = connection.prepareStatement("INSERT INTO player_stats (username, password, wins, played, percent) VALUES (?, ?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setInt(3, wins);
                psInsert.setInt(4, played);
                psInsert.setDouble(5, percent);
                psInsert.executeUpdate();
                changeScene(event, fxmlFile, username, wins, played);
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            System.out.println(e.getMessage());
        }
    }

    // Logs a player in and retrieves information form the database as a check
    public static void logInUser (ActionEvent event, String fxmlFile, String username, String password) {
        try {
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect.");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievePassword = resultSet.getString("password");
                    String playerName = resultSet.getString("username");
                    int gamesWon = resultSet.getInt("wins");
                    int totalGames = resultSet.getInt("played");
                    double winPercent = resultSet.getDouble("percent");
                    if (retrievePassword.equals(password)) {
                        System.out.println("Player name and password match!");
                        changeScene(event, fxmlFile, playerName, gamesWon, totalGames);
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect! ");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Selects information from the database, organises players by win percentage, and limits them to the first 10
    public static ObservableList<PlayerLeaderboard> topTenList () throws ClassNotFoundException, SQLException {
        ObservableList<PlayerLeaderboard> topTen = FXCollections.observableArrayList();
        try {
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats ORDER BY percent DESC LIMIT 10");
            resultSet = psCheckUserExists.executeQuery();
            topTen = getTopTenList(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return topTen;
    }

    // Ceates an ObservableList as preparation for the top ten TableView leaderboard
    private static ObservableList<PlayerLeaderboard> getTopTenList (ResultSet resultSet) throws ClassNotFoundException, SQLException {
        ObservableList<PlayerLeaderboard> playerList = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                PlayerLeaderboard pl = new PlayerLeaderboard();
                pl.setUsername(resultSet.getString("username"));
                pl.setWins(resultSet.getInt("wins"));
                pl.setPlayed(resultSet.getInt("played"));
                pl.setPercent(resultSet.getDouble("percent"));
                playerList.add(pl);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return playerList;
    }

    // Updates the database with the latest information
    public static void updateDB (String username, int wins, int played, double percent) {
        try {
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats");
            resultSet = psCheckUserExists.executeQuery();
            while (resultSet.next()) {
                String playerName = resultSet.getString("username");
                int playerID = resultSet.getInt("player_id");
                if (playerName.equals(username)) {
                    psUpdate = connection.prepareStatement("UPDATE player_stats SET wins = ?, played = ?, percent = ? WHERE player_id = ?");
                    psUpdate.setInt(1, wins);
                    psUpdate.setInt(2, played);
                    psUpdate.setDouble(3, percent);
                    psUpdate.setInt(4, playerID);
                    psUpdate.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}















