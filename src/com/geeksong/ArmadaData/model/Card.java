package com.geeksong.ArmadaData.model;

import static com.geeksong.ArmadaData.model.CardType.*;

public class Card {
    private int x;
    private String name;
    private CardType cardType;

    public Card(String name, int x, CardType cardType) {
        this.name = name;
        this.x = x;
        this.cardType = cardType;
    }

    public int getX() { return this.x; }
    public String getName() { return this.name; }
    public CardType getCardType() { return this.cardType; }

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

