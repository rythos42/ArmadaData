package com.geeksong.ArmadaData.model;

import java.util.ArrayList;

public class PlayerData {
    private String[] fleet;
    private Player player;
    private int fleetPoints;
    private int pointsScored;
    private String faction;

    public PlayerData(String[] fleet, Player player, String faction, int fleetPoints, int pointsScored) {
        this.fleet = fleet;
        this.player = player;
        this.faction = faction;
        this.fleetPoints = fleetPoints;
        this.pointsScored = pointsScored;
    }

    public String[] getFleetText() { return this.fleet; }
    public String getOwnerName() { return this.player.getName(); }
    public String getOwnerId() { return this.player.getId(); }
    public int getFleetPoints() { return this.fleetPoints; }
    public int getPointsScored() { return this.pointsScored; }
    public String getFaction() { return this.faction; }
}
