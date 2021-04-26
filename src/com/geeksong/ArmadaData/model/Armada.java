package com.geeksong.ArmadaData.model;

import java.util.stream.Stream;

public class Armada {
    public static final String[] Factions = new String[] {
            "Imperial",
            "Rebel",
            "Republic",
            "Separatist",
    };

    public static final String[] AssaultObjectives = new String[] {
            "Advanced Gunnery",
            "Blockade Run",
            "Close-Range Intel Scan",
            "Ion Storm",
            "Marked for Destruction",
            "Most Wanted",
            "Opening Salvo",
            "Precision Strike",
            "Rift Assault",
            "Station Assault",
            "Surprise Attack",
            "Targeting Beacons"
    };

    public static final String[] DefenseObjectives = new String[] {
            "Abandoned Mining Facility",
            "Asteroid Tactics",
            "Capture the VIP",
            "Contested Outpost",
            "Fighter Ambush",
            "Fire Lanes",
            "Fleet Ambush",
            "Fleet in Being",
            "Hyperspace Assault",
            "Jamming Barrier",
            "Planetary Ion Cannon",
            "Rift Ambush"
    };

    public static final String[] NavigationObjectives = new String[] {
            "Dangerous Territory",
            "Doomed Station",
            "Hyperspace Migration",
            "Infested Fields",
            "Intel Sweep",
            "Minefields",
            "Navigational Hazards",
            "Salvage Run",
            "Sensor Net",
            "Solar Corona",
            "Superior Positions",
            "Volatile Deposits"
    };

    public static final String[] Objectives = Stream.of(AssaultObjectives, DefenseObjectives, NavigationObjectives).flatMap(Stream::of).sorted().toArray(String[]::new);
}
