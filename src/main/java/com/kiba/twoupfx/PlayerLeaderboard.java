package com.kiba.twoupfx;

import javafx.beans.property.*;

// This is a Constructor file for the TableView
public class PlayerLeaderboard {

    private final StringProperty username;
    private final IntegerProperty wins;
    private final IntegerProperty played;
    private final DoubleProperty percent;

    public PlayerLeaderboard () {
        this.username = new SimpleStringProperty(this, "username");
        this.wins = new SimpleIntegerProperty(this, "wins");
        this.played = new SimpleIntegerProperty(this, "played");
        this.percent = new SimpleDoubleProperty(this, "percent");
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setWins(int wins) {
        this.wins.set(wins);
    }

    public IntegerProperty winsProperty() {
        return wins;
    }

    public void setPlayed(int played) {
        this.played.set(played);
    }

    public IntegerProperty playedProperty() {
        return played;
    }

    public void setPercent(double percent) {
        this.percent.set(percent);
    }

    public DoubleProperty percentProperty() {
        return percent;
    }


}
