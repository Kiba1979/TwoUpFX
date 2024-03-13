package com.kiba.twoupfx;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.*;

class TwoUpTest {

    @Test
    void setWinPercentageTest () {
        GameTimeController gtc = new GameTimeController();
        double percent = gtc.setWinPercentage(10, 20);
        Assertions.assertEquals(50.0, percent);
    }

    @Test
    void dbConnectionTest () throws SQLException {
        String playerName = "Adam";
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/adams_two_up", "root", "root");
        PreparedStatement psCheckUserExists = connection.prepareStatement("SELECT * FROM player_stats WHERE username = ?");
        psCheckUserExists.setString(1, "Adam");
        ResultSet resultSet = psCheckUserExists.executeQuery();
        while (resultSet.next()) {
            String username = resultSet.getString("username");
            if (!username.equals(playerName)) {
                Assertions.assertEquals(playerName, username);
            }
        }

    }



}