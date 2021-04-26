package com.geeksong.ArmadaData.model;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card o1, Card o2) {
        return Integer.compare(o1.getX(), o2.getX());
    }
}
