package com.kiba.twoupfx;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import java.sql.*;

public class Test {

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

    public static void dbConnect () throws SQLException {

        try {
            connection = DriverManager.getConnection(db_connect, db_username, db_password);
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public static void dbDisconnect () throws SQLException {

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void PlayerLogin (ActionEvent event, String fxmlFile, String username, String password) throws SQLException {

        try {
            dbConnect();
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
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect! ");
                        alert.show();
                    }
                }
            }
            DBUtils.changeScene(event, fxmlFile, username);
        } catch (SQLException e) {
                throw e;
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
}
