package com.geeksong.ArmadaData.model;

public class Game {
    private final Player[] players;
    private final Fleet topPlayerFleet;
    private final Fleet bottomPlayerFleet;
    private final String playedObjective;

    public Game(Player[] players, Fleet topPlayerFleet, Fleet bottomPlayerFleet, String playedObjective) {
        this.players = players;
        this.topPlayerFleet = topPlayerFleet;
        this.bottomPlayerFleet = bottomPlayerFleet;
        this.playedObjective = playedObjective;
    }

    public Player[] getPlayers() { return this.players; }
    public Fleet getTopPlayerFleet() { return this.topPlayerFleet; }
    public Fleet getBottomPlayerFleet() { return this.bottomPlayerFleet; }
    public String getPlayedObjective() { return this.playedObjective; }
}

