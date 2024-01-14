package com.kiba.twoupfx;

import javafx.beans.property.*;

public class PlayerLeaderboard {

    private final SimpleStringProperty playerName;
    private final SimpleIntegerProperty gamesWon;
    private final SimpleIntegerProperty gamesPlayed;
    private final SimpleDoubleProperty winPercent;

    public PlayerLeaderboard (String playerName, int gamesWon, int gamesPlayed, double winPercent) {
        this.playerName = new SimpleStringProperty(playerName);
        this.gamesWon = new SimpleIntegerProperty(gamesWon);
        this.gamesPlayed = new SimpleIntegerProperty(gamesPlayed);
        this.winPercent = new SimpleDoubleProperty(winPercent);
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public Integer getGamesWon() {
        return gamesWon.get();
    }

    public Integer getGamesPlayed() {
        return gamesPlayed.get();
    }

    public Double getWinPercent() {
        return winPercent.get();
    }
}
