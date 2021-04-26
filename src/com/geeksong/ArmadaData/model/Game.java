package com.geeksong.ArmadaData.model;

public class Game {
    private Player[] players;
    private Fleet topPlayerFleet;
    private Fleet bottomPlayerFleet;

    public Game(Player[] players, Fleet topPlayerFleet, Fleet bottomPlayerFleet) {
        this.players = players;
        this.topPlayerFleet = topPlayerFleet;
        this.bottomPlayerFleet = bottomPlayerFleet;
    }

    public Player[] getPlayers() { return this.players; }
    public Fleet getTopPlayerFleet() { return this.topPlayerFleet; }
    public Fleet getBottomPlayerFleet() { return this.bottomPlayerFleet; }
}

