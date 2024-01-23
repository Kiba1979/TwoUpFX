package com.kiba.twoupfx;

import javafx.beans.property.*;

public class PlayerLeaderboard {

    private StringProperty username;
    private IntegerProperty wins;
    private IntegerProperty played;
    private DoubleProperty percent;

    public PlayerLeaderboard () {
        this.username = new SimpleStringProperty(this, "username");
        this.wins = new SimpleIntegerProperty(this, "wins");
        this.played = new SimpleIntegerProperty(this, "played");
        this.percent = new SimpleDoubleProperty(this, "percent");
    }

    public PlayerLeaderboard (String playerName, int gamesWon, int gamesPlayed, double winPercent) {
        username.set(playerName);
        wins.set(gamesWon);
        played.set(gamesPlayed);
        percent.set(winPercent);
    }

    public String getUsername() {
        return this.username.get();
    }

    public int getWins() {
        return this.wins.get();
    }

    public int getPlayed() {
        return this.played.get();
    }

    public double getPercent() {
        return this.percent.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setWins(int wins) {
        this.wins.set(wins);
    }

    public void setPlayed(int played) {
        this.played.set(played);
    }

    public void setPercent(double percent) {
        this.percent.set(percent);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public IntegerProperty winsProperty() {
        return wins;
    }

    public IntegerProperty playedProperty() {
        return played;
    }

    public DoubleProperty percentProperty() {
        return percent;
    }


}
