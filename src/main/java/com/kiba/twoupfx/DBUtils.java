package com.kiba.twoupfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
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

    //SELECT * FROM player_stats
    //JOIN sign_in_credentials ON sign_in_credentials.user_id = player_stats.player_id
    //ORDER BY win_percent desc LIMIT 10;

    public static void createConnection () {
        try {
            connection = DriverManager.getConnection(db_connect, db_username, db_password);
            System.out.println("Database connection successful.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void closeConnection () {
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
        }
    }

    // Method that changes between the different FXML scenes
    public static void changeScene (ActionEvent event, String fxmlFile, String username) {
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                GameTimeController gameTimeController = loader.getController();
                gameTimeController.setUserInfo(username);
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

    // Signup screen that adds details to a database
    public static void signUpUser (ActionEvent event, String fxmlFile, String username, String password) {
        int wins = 0;
        int played = 0;
        double percent = 0.00;
        try {
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats WHERE username = ? AND password = ?");
            psCheckUserExists.setString(1, username);
            psCheckUserExists.setString(2, password);
            resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) {
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
            }
            changeScene(event, fxmlFile, username);
        } catch (SQLException e) {
            System.out.println("Error!");
            System.out.println(e.getMessage());
        }
    }

    public static void logInUser (ActionEvent event, String fxmlFile, String username, String password) {
        PlayerLeaderboard pl = new PlayerLeaderboard();
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
                    int gamesPlayed = resultSet.getInt("played");
                    double winPercent = resultSet.getDouble("percent");
                    if (retrievePassword.equals(password)) {
                        System.out.println("Player name and password match!");
                        changeScene(event, fxmlFile, playerName);
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

    public static void playerStats () {
        PlayerLeaderboard pl = new PlayerLeaderboard();
        GameTimeController gtc = new GameTimeController();
        ObservableList<PlayerLeaderboard> topTenList = FXCollections.observableArrayList();
        try {
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats ORDER BY percent DESC LIMIT 10");
            resultSet = psCheckUserExists.executeQuery();
            while (resultSet.next()) {
                pl.setUsername(resultSet.getString("username"));
                pl.setWins(resultSet.getInt("wins"));
                pl.setPlayed(resultSet.getInt("played"));
                pl.setPercent(resultSet.getDouble("percent"));
                
                StringProperty username = pl.usernameProperty();
                IntegerProperty wins = pl.winsProperty();
                IntegerProperty played = pl.playedProperty();
                DoubleProperty percent = pl.percentProperty();

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void updatePlayerStats (int gameWins, int gamesPlayed, double winPercent) {
        try {
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats");
            resultSet = psCheckUserExists.executeQuery();
            while (resultSet.next()) {
                int played = resultSet.getInt("played");
                if (played != 0) {
                    psUpdate = connection.prepareStatement("UPDATE player_stats SET username = " + gameWins + ", played = " + gamesPlayed + ", percent = " + winPercent);
                    psUpdate.setInt(1, gameWins);
                    psUpdate.setInt(2, gamesPlayed);
                    psUpdate.setDouble(3, winPercent);
                    psUpdate.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ObservableList<PlayerLeaderboard> topTenList () throws ClassNotFoundException, SQLException {
        ObservableList<PlayerLeaderboard> topTen = FXCollections.observableArrayList();
        try {
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats");
            resultSet = psCheckUserExists.executeQuery();
            topTen = getTopTenList(resultSet);
            return topTen;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return topTen;
    }

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

    public static ArrayList topTenArrayList () throws SQLException {
        ArrayList<PlayerLeaderboard> data = new ArrayList<>();
        while (resultSet.next()) {
            PlayerLeaderboard pl = new PlayerLeaderboard();
            pl.setUsername(resultSet.getString("username"));
            pl.setWins(resultSet.getInt("wins"));
            pl.setPlayed(resultSet.getInt("played"));
            pl.setPercent(resultSet.getDouble("percent"));
            data.add(pl);
        } return data;
    }
}















