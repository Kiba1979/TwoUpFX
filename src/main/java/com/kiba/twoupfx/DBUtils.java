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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBUtils {

    private static final String db_connect = "jdbc:mysql://127.0.0.1:3306/adams_two_up";
    private static final String db_username = "root";
    private static final String db_password = "root";
    private static Connection connection;
    private static PreparedStatement psInsert;
    private static PreparedStatement psForeignInsert;
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
            if (psForeignInsert != null) {
                try {
                    psForeignInsert.close();
                    if (psForeignInsert.isClosed()) {
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
        GameTimeController gtc = new GameTimeController();
        try {
            createConnection();
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
                psInsert = connection.prepareStatement("INSERT INTO player_stats (username, password) VALUES (?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();
                psForeignInsert = connection.prepareStatement("INSERT player_stats (wins, played, percent) VALUES (?, ?, ?)");
                psForeignInsert.setInt(1, 0);
                psForeignInsert.setInt(2, 0);
                psForeignInsert.setDouble(3, 0.00);
                psForeignInsert.executeUpdate();
                int wins = resultSet.getInt("wins");
                int played = resultSet.getInt("played");
                double percent = resultSet.getDouble("percent");
                gtc.currentPlayerStats(wins, played, percent);
            }
            changeScene(event, fxmlFile, username);
        } catch (SQLException e) {
            System.out.println("Error!");
            System.out.println(e.getMessage());
        }
    }

    public static void logInUser (ActionEvent event, String fxmlFile, String username, String password) {
        try {
            createConnection();
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
                    if (retrievePassword.equals(password)) {
                        System.out.println("Player name and password match!");
                        changeScene(event, fxmlFile, username);
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



}















