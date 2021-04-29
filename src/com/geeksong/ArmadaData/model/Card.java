package com.geeksong.ArmadaData.model;

public class Card {
    private final int x;
    private final String name;
    private final CardType cardType;
    private final String spawnedBySide;

    public Card(String name, int x, CardType cardType, String spawnedBySide) {
        this.name = name;
        this.x = x;
        this.cardType = cardType;
        this.spawnedBySide = spawnedBySide;
    }

    public int getX() { return this.x; }
    public String getName() { return this.name; }
    public String getSpawnedBySide() { return this.spawnedBySide; }

    public String getOutput() {
        switch (this.cardType) {
            case Upgrade:
                return " - " + this.name;
            case ShipOrSquadron:
                return this.name;
        }
        return "";
    }
}

