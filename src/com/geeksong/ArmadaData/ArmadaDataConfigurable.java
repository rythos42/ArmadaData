package com.geeksong.ArmadaData;

import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.counters.GamePiece;
import com.geeksong.ArmadaData.model.*;
import com.geeksong.ArmadaData.ui.UploadResultsFrame;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class ArmadaDataConfigurable extends AbstractConfigurable {
    private final int TOP_PLAYER_Y_THRESHOLD = 600;
    private final int BOTTOM_PLAYER_Y_THRESHOLD = 2500;

    private JButton uploadResultsButton;

    @Override
    public void addTo(Buildable buildable) {
        GameModule gameModule = (GameModule) buildable;

        uploadResultsButton = new JButton("Upload Results");
        uploadResultsButton.setPreferredSize(new Dimension(100, 52));  // couldn't figure out how to make it auto-fill
        uploadResultsButton.addActionListener(evt -> ShowResultsFrame());

        gameModule.getToolBar().add(uploadResultsButton);
    }

    private void ShowResultsFrame() {
        var gameModule = GameModule.getGameModule();
        var chat = gameModule.getChatter();
        try {
            var map = Map.getMapById("Table");
            if (map == null) {
                chat.show("ArmadaData: No map by the name 'Table' available. Report this to the extension maintainer.");
                return;
            }

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            var game = getGame(gameModule, map);
            var uploadResultsFrame = new UploadResultsFrame(game);
            uploadResultsFrame.setVisible(true);
        } catch (Exception ex) {
            StringWriter exceptionStackTrace = new StringWriter();
            PrintWriter printWriter = new PrintWriter(exceptionStackTrace);
            ex.printStackTrace(printWriter);

            chat.show(exceptionStackTrace.toString());
            chat.show("ArmadaData: Error uploading results. Send the above text to the extension maintainer.");
        }
    }

    private Game getGame(GameModule gameModule, Map map) {
        // Get and sort cards on the table
        var topPlayerCardsOnTable = new ArrayList<Card>();
        var bottomPlayerCardsOnTable = new ArrayList<Card>();
        var topPlayerObjectivesOnTable = new ArrayList<String>();
        var bottomPlayerObjectivesOnTable = new ArrayList<String>();
        var squadronsOnTable = new Hashtable<String, Integer>();
        String playedObjective = "";
        Boolean topPlayerIsFirstPlayer = null;

        for (var piece : map.getAllPieces()) {
            var markerLayer = piece.getProperty("Layer");
            var name = piece.getProperty("BasicName");
            var spawnedBySide = piece.getProperty("playerSide");
            var pieceX = getNumericProperty(piece, "CurrentX");
            var pieceY = getNumericProperty(piece, "CurrentY");

            if (markerLayer == "Card") {
                if(Arrays.stream(Armada.Objectives).anyMatch(objectiveName -> objectiveName.equals(name))) {
                    // Card is an objective
                    if (pieceY <= TOP_PLAYER_Y_THRESHOLD)
                        topPlayerObjectivesOnTable.add(name.toString());
                    else if(pieceY >= BOTTOM_PLAYER_Y_THRESHOLD)
                        bottomPlayerObjectivesOnTable.add(name.toString());
                    else
                        playedObjective = name.toString();
                } else {
                    // Isn't currently a distinct identifier for "upgrade" vs "ship" or "squadron" cards,
                    // so using a dynamic property that's only on upgrades to distinguish them
                    var isUpgradeCard = piece.getProperty("Discard status") != null;

                    var card = new Card(
                            name.toString(),
                            pieceX,
                            isUpgradeCard ? CardType.Upgrade : CardType.ShipOrSquadron,
                            spawnedBySide.toString());

                    // card belongs to player 1 because it's on the bottom half of the table
                    if (pieceY <= TOP_PLAYER_Y_THRESHOLD)
                        topPlayerCardsOnTable.add(card);
                    else if(pieceY >= BOTTOM_PLAYER_Y_THRESHOLD)
                        bottomPlayerCardsOnTable.add(card);
                }
            }

            if (markerLayer == "Squadron") {
                var squadronName = name.toString();
                if(squadronsOnTable.containsKey(squadronName))
                    squadronsOnTable.put(squadronName, squadronsOnTable.get(squadronName) + 1);
                else
                    squadronsOnTable.put(squadronName, 0);  // putting 0, rather than 1 -- the card counts as the first squadron
            }

            if("Initiative Token".equals(name)) {
                if (pieceY <= TOP_PLAYER_Y_THRESHOLD)
                    topPlayerIsFirstPlayer = true;
                else if(pieceY >= BOTTOM_PLAYER_Y_THRESHOLD)
                    topPlayerIsFirstPlayer = false;
            }
        }

        topPlayerCardsOnTable.sort(new CardComparator());
        bottomPlayerCardsOnTable.sort(new CardComparator());

        // count squadrons on table and duplicate those cards for those players
        for(var squadronData : squadronsOnTable.entrySet()) {
            var playerOneSquadron = containsSquadron(topPlayerCardsOnTable, squadronData.getKey());
            if (playerOneSquadron != null) {
                for(var i = 0; i < squadronData.getValue(); i++) {
                    topPlayerCardsOnTable.add(playerOneSquadron);
                }
            }

            var playerTwoSquadron = containsSquadron(bottomPlayerCardsOnTable, squadronData.getKey());
            if (playerTwoSquadron != null) {
                for(var i = 0; i < squadronData.getValue(); i++) {
                    bottomPlayerCardsOnTable.add(playerTwoSquadron);
                }
            }
        }

        // Determine players
        var players = gameModule.getPlayerRoster().getPlayers();
        Player playerOne = null, playerTwo = null;
        for (var playerInfo : players) {
            var player = new Player(playerInfo.getSide(), playerInfo.playerName, playerInfo.playerId);
            switch (playerInfo.getSide()) {
                case "Player 1":
                    playerOne = player;
                    break;
                case "Player 2":
                    playerTwo = player;
                    break;
            }
        }

        return new Game(
                new Player[] { playerOne, playerTwo },
                new Fleet(topPlayerCardsOnTable, topPlayerObjectivesOnTable),
                new Fleet(bottomPlayerCardsOnTable, bottomPlayerObjectivesOnTable),
                playedObjective,
                topPlayerIsFirstPlayer
        );
    }

    private Card containsSquadron(ArrayList<Card> fleet, String squadronName) {
        for(var card : fleet) {
            var cardName = card.getName();
            var slashIndex = cardName.indexOf("/");
            if(slashIndex >= 0) {
                // card is a unique, so name is of the form "Axe/V-19 Squadron"
                // squadronName is "Axe" or "V-19 Torrent", so must match the text before the "/"
                var actualName = cardName.substring(0, slashIndex);
                if(actualName == squadronName)
                    return card;
            } else {
                // card is a generic, so name is of the form "V-19 TorrentSquadron"
                // squadronName is "V-19 Torrent", so cardName must contain squadronName
                if(cardName.contains(squadronName))
                    return card;
            }
        }
        return null;
    }

    private Integer getNumericProperty(GamePiece piece, String key) {
        var property = piece.getProperty(key);
        if(property == null)
            return null;

        return Integer.parseInt(property.toString());
    }

    /********

     Largely unused required overrides

     ********/

    @Override
    public void removeFrom(Buildable buildable) {
        GameModule gameModule = (GameModule)buildable;
        gameModule.getToolBar().remove(uploadResultsButton);
    }

    @Override
    public String[] getAttributeDescriptions() {
        return new String[0];
    }

    @Override
    public Class<?>[] getAttributeTypes() {
        return new Class[0];
    }

    @Override
    public String[] getAttributeNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String s, Object o) {
    }

    @Override
    public String getAttributeValueString(String s) {
        return null;
    }

    @Override
    public HelpFile getHelpFile() {
        return null;
    }

    @Override
    public Class[] getAllowableConfigureComponents() {
        return new Class[0];
    }
}

