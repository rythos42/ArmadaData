package com.geeksong.ArmadaData.model;

import java.util.ArrayList;

public class Fleet {
    private ArrayList<Card> cards;

    public Fleet(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() { return this.cards; }

    public String getFleetForOutput() {
        StringBuilder output = new StringBuilder();
        String prefix = "";

        for(var card : this.cards) {
            output.append(prefix);
            output.append(card.getOutput());
            prefix = "\n";
        }

        return output.toString();
    }
}
