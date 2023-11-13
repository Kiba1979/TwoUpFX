package com.kiba.twoupfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class DBUtils {

    private static final String db_connect = "jdbc:mysql://127.0.0.1:3306/adams_two_up";
    private static final String db_username = "root";
    private static final String db_password = "root";
    private static Connection connection;
    private static PreparedStatement psInsert;
    private static PreparedStatement psForeignInsert;
    private static PreparedStatement psCheckUserExists;
    private static PreparedStatement psUpdate;
    private static ResultSet resultSet;
    private static Parent root;
//    private static String signUpWindow = "Sign Up!";
//    private static String gameTimeWindow = "Game Time!";

    //SELECT * FROM player_stats
    //JOIN sign_in_credentials ON sign_in_credentials.user_id = player_stats.player_id
    //ORDER BY win_percent desc LIMIT 10;

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
        assert root != null;
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void signUpUser (ActionEvent event, String fxmlFile, String username, String password) {

        try {
            connection = DriverManager.getConnection(db_connect, db_username, db_password);
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_signin WHERE player_name = ? AND player_password = ?");
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
                psInsert = connection.prepareStatement("INSERT INTO player_signin (player_name, player_password) VALUES (?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();
                psForeignInsert = connection.prepareStatement("INSERT INTO player_stats (game_wins, games_played, win_percentage) VALUES (?, ?, ?)");
                psForeignInsert.setInt(1, 0);
                psForeignInsert.setInt(2, 0);
                psForeignInsert.setDouble(3, 0);
                psForeignInsert.executeUpdate();
            }
            changeScene(event, fxmlFile, username);
        } catch (SQLException e) {
            System.out.println("Error!");
            System.out.println(e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psForeignInsert != null) {
                try {
                    psForeignInsert.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void logInUser (ActionEvent event, String fxmlFile, String username, String password) {
        
        try {
            connection = DriverManager.getConnection(db_connect, db_username, db_password);
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_signin WHERE player_name = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect.");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievePassword = resultSet.getString("player_password");
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
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void savePlayerStats (String username, int gamesWon, int gamesPlayed, int winPercentage) {
        try {
            connection = DriverManager.getConnection(db_connect, db_username, db_password);
            psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats WHERE player_id = player_signin.player_id");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("That name does not exist!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The provided credentials are incorrect! ");
                alert.show();
            } else {
                System.out.println("User exists");
                psUpdate = connection.prepareStatement("UPDATE player_stats SET games_won = ?, games_played = ?, win_percentage = ? WHERE player_signin.player_id = player_stats.player_id");
                psUpdate.setInt(1, gamesWon);
                psUpdate.setInt(2, gamesPlayed);
                psUpdate.setInt(3, winPercentage);
                psUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psUpdate != null) {
                try {
                    assert resultSet != null;
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void loadPlayerStats (String fxmlFile, String username, int gameWins, int gamesPlayed, double winPercent) {

        try {
            connection = DriverManager.getConnection(db_connect, db_username, db_password);
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();
            GameTimeController gameTimeController = loader.getController();
            gameTimeController.setUserInfo(username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.next()) {
                PreparedStatement psGetStats = connection.prepareStatement("SELECT * FROM player_stats WHERE player_id = player_signin.player_id");
                psGetStats.setString(1, username);
                psGetStats.setInt(2, gameWins);
                psGetStats.setInt(3, gamesPlayed);
                psGetStats.setDouble(4, winPercent);
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}