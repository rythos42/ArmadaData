package com.geeksong.ArmadaData;

import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import com.geeksong.ArmadaData.model.*;
import com.geeksong.ArmadaData.ui.UploadResultsFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ArmadaDataConfigurable extends AbstractConfigurable {
    private final int BOTTOM_PLAYER_Y_THRESHOLD = 1500;

    private JButton aiButton;

    @Override
    public void addTo(Buildable buildable) {
        GameModule gameModule = (GameModule) buildable;

        aiButton = new JButton("Upload Results");
        aiButton.setPreferredSize(new Dimension(100, 52));  // couldn't figure out how to make it auto-fill
        aiButton.addActionListener(evt -> ShowResultsFrame());

        gameModule.getToolBar().add(aiButton);
    }

    private void ShowResultsFrame() {
        var gameModule = GameModule.getGameModule();
        var chat = gameModule.getChatter();
        var map = Map.getMapById("Table");
        if(map == null) {
            chat.show("No map by the name 'Table' available. Report this to the extension maintainer.");
            return;
        }

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        var game = getGame(gameModule, map);
        var uploadResultsFrame = new UploadResultsFrame(game);
        uploadResultsFrame.setVisible(true);
    }

    private Game getGame(GameModule gameModule, Map map) {
        // Get and sort cards on the table
        var topPlayerCardsOnTable = new ArrayList<Card>();
        var bottomPlayerCardsOnTable = new ArrayList<Card>();
        var squadronsOnTable = new ArrayList<String>();
        for (var piece : map.getAllPieces()) {
            var markerLayer = piece.getProperty("Layer");
            var name = piece.getProperty("BasicName");

            if (markerLayer == "Card") {
                var cardX = Integer.parseInt(piece.getProperty("CurrentX").toString());
                var cardY = Integer.parseInt(piece.getProperty("CurrentY").toString());

                // Isn't currently a distinct identifier for "upgrade" vs "ship" or "squadron" cards,
                // so using a dynamic property that's only on upgrades to distinguish them
                var isUpgradeCard = piece.getProperty("Discard status") != null;

                var card = new Card(name.toString(), cardX, isUpgradeCard ? CardType.Upgrade : CardType.ShipOrSquadron);

                // card belongs to player 1 because it's on the bottom half of the table
                if (cardY < BOTTOM_PLAYER_Y_THRESHOLD)
                    topPlayerCardsOnTable.add(card);
                else
                    bottomPlayerCardsOnTable.add(card);
            }

            if (markerLayer == "Squadron") {
                squadronsOnTable.add(name.toString());
            }
        }

        topPlayerCardsOnTable.sort(new CardComparator());
        bottomPlayerCardsOnTable.sort(new CardComparator());

        // count squadrons on table and duplicate those cards for those players
        boolean skipFirstPlayerOne = true, skipFirstPlayerTwo = true;
        for (var squadronName : squadronsOnTable) {
            var playerOneSquadron = topPlayerCardsOnTable.stream().filter(card -> card.getName().contains(squadronName)).findFirst();
            if (playerOneSquadron.isPresent()) {
                if (skipFirstPlayerOne)
                    skipFirstPlayerOne = false;
                else
                    topPlayerCardsOnTable.add(playerOneSquadron.get());
            }

            var playerTwoSquadron = bottomPlayerCardsOnTable.stream().filter(card -> card.getName().contains(squadronName)).findFirst();
            if (playerTwoSquadron.isPresent()) {
                if (skipFirstPlayerTwo)
                    skipFirstPlayerTwo = false;
                else
                    bottomPlayerCardsOnTable.add(playerTwoSquadron.get());
            }
        }

        // Determine players
        var players = gameModule.getPlayerRoster().getPlayers();
        Player playerOne = null, playerTwo = null;
        for (var playerInfo : players) {
            switch (playerInfo.getSide()) {
                case "Player 1":
                    playerOne = new Player(playerInfo.playerName, playerInfo.playerId);
                    break;
                case "Player 2":
                    playerTwo = new Player(playerInfo.playerName, playerInfo.playerId);
                    break;
            }
        }

        return new Game(
                new Player[] { playerOne, playerTwo },
                new Fleet(topPlayerCardsOnTable),
                new Fleet(bottomPlayerCardsOnTable)
        );
    }


    /********

     Largely unused required overrides

     ********/

    @Override
    public void removeFrom(Buildable buildable) {
        GameModule gameModule = (GameModule)buildable;
        gameModule.getToolBar().remove(aiButton);
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
