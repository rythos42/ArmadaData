package com.geeksong.ArmadaData.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Fleet {
    private final ArrayList<Card> cards;
    private String assaultObjective;
    private String defenseObjective;
    private String navigationObjective;

    public Fleet(ArrayList<Card> cards, ArrayList<String> objectives) {
        this.cards = cards;

        for(var objective : objectives) {
            if(Arrays.stream(Armada.AssaultObjectives).anyMatch(objectiveName -> objectiveName.equals(objective)))
                this.assaultObjective = objective;
            if(Arrays.stream(Armada.DefenseObjectives).anyMatch(objectiveName -> objectiveName.equals(objective)))
                this.defenseObjective = objective;
            if(Arrays.stream(Armada.NavigationObjectives).anyMatch(objectiveName -> objectiveName.equals(objective)))
                this.navigationObjective = objective;
        }
    }

    public String getAssaultObjective() { return this.assaultObjective; }
    public String getDefenseObjective() { return this.defenseObjective; }
    public String getNavigationObjective() { return this.navigationObjective; }

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
