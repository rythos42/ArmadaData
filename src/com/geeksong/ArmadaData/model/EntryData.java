package com.geeksong.ArmadaData.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class EntryData {
    private final PlayerData firstPlayerData;
    private final PlayerData secondPlayerData;
    private final String objectivePlayed;
    private final String tournamentCode;

    public EntryData(PlayerData firstPlayerData, PlayerData secondPlayerData, String objectivePlayed, String tournamentCode) {
        this.firstPlayerData = firstPlayerData;
        this.secondPlayerData = secondPlayerData;
        this.objectivePlayed = objectivePlayed;
        this.tournamentCode = tournamentCode;
    }

    public String getJsonRequest() {
        var tournamentPoints = getTournamentPoints();

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .appendPattern("MM")
                .appendPattern("dd")
                .appendLiteral("T")
                .appendPattern("kk")
                .appendPattern("mm")
                .appendPattern("ss")
                .appendLiteral(".")
                .appendPattern("SSS")
                .toFormatter();
        String formattedDate = LocalDateTime.now().format(formatter);

        // Avoiding figuring out external library dependencies in Vassal, this isn't the best way to do this.
        return "{" +
                "   \"fields\": {" +
                "       \"player1\": { \"stringValue\": \"" + this.firstPlayerData.getOwnerName() + "\" }," +
                "       \"player1SteamId\": { \"stringValue\": \"" + this.firstPlayerData.getOwnerId() + "\" }," +
                "       \"player1fleet\": { \"arrayValue\": { \"values\": " + getFleetJson(this.firstPlayerData) + "} }," +
                "       \"player1Faction\": { \"stringValue\": \"" + this.firstPlayerData.getFaction() + "\" }," +
                "       \"player1score\": { \"integerValue\": " + this.firstPlayerData.getPointsScored() + " }," +
                "       \"player1scoreVerified\": { \"integerValue\": " + this.firstPlayerData.getPointsScored() + " }," +
                "       \"player1fleetPoints\": { \"integerValue\": " + this.firstPlayerData.getFleetPoints() + " }," +
                "       \"player1points\": { \"integerValue\": " + tournamentPoints[0] + " }," +
                "       \"objective\": { \"stringValue\": \"" + this.objectivePlayed + "\" }," +
                "       \"player2\": { \"stringValue\": \"" + this.secondPlayerData.getOwnerName() + "\" }," +
                "       \"player2SteamId\": { \"stringValue\": \"" + this.secondPlayerData.getOwnerId() + "\" }," +
                "       \"player2fleet\": { \"arrayValue\": { \"values\": " + getFleetJson(this.secondPlayerData) + "} }," +
                "       \"player2Faction\": { \"stringValue\": \"" + this.secondPlayerData.getFaction() + "\" }," +
                "       \"player2score\": { \"integerValue\": " + this.secondPlayerData.getPointsScored() + " }," +
                "       \"player2scoreVerified\": { \"integerValue\": " + this.secondPlayerData.getPointsScored() + " }," +
                "       \"player2fleetPoints\": { \"integerValue\": " + this.secondPlayerData.getFleetPoints() + " }," +
                "       \"player2points\": { \"integerValue\": " + tournamentPoints[1] + " }," +
                "       \"submitted\": { \"stringValue\": \"" + formattedDate + "\" }," +
                "       \"ranked\": { \"booleanValue\": false }," +
                "       \"tournamentCode\": { \"stringValue\": \"" + this.tournamentCode + "\" }" +
                "   }" +
                "}";
    }

    private int[] getTournamentPoints() {
        int diff = this.firstPlayerData.getPointsScored() - this.secondPlayerData.getPointsScored();

        if(diff <= -300) return new int[] { 1, 10 };
        if(diff <= -220) return new int[] { 2, 9 };
        if(diff <= -140) return new int[] { 3, 8 };
        if(diff <= -60) return new int[] { 4, 7 };
        if(diff <= 0) return new int [] { 5, 6 };
        if(diff < 60) return new int[] { 6, 5 };
        if(diff < 140) return new int[] { 7, 4 };
        if(diff < 220) return new int[] { 8, 5 };
        if(diff < 300) return new int[] { 9, 2 };
        return new int[] { 10, 1 };
    }

    private String getFleetJson(PlayerData playerData) {
        StringBuilder json = new StringBuilder();
        String prefix = "";

        json.append("[");
        for(var fleetString : playerData.getFleetText()) {
            json.append(prefix);
            json.append("   { \"stringValue\": \"").append(fleetString).append("\" }");
            prefix = ",";
        }
        json.append("]");

        return json.toString();
    }
}
