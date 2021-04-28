package com.geeksong.ArmadaData.model;

public class Player {
    private final String name;
    private final String id;
    private final String side;

    public Player(String side, String name, String id) {
        this.side = side;
        this.name = name;
        this.id = id;
    }

    public String getSide() { return this.side; }
    public String getName() { return this.name; }
    public String getId() { return this.id; }
}
