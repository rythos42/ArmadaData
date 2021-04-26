package com.geeksong.ArmadaData.ui;

import com.geeksong.ArmadaData.model.Armada;
import com.geeksong.ArmadaData.model.Fleet;
import com.geeksong.ArmadaData.model.Player;
import com.geeksong.ArmadaData.model.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class PlayerEntryPanel extends JPanel {
    private final JTextArea fleetTextArea;
    private final JComboBox<String> ownerComboBox;
    private final JComboBox<String> factionComboBox;
    private final JTextField fleetPointsTextField;
    private final JTextField pointsScoredTextField;
    private final String title;
    private final Player[] players;
    private final JComboBox<String> assaultObjectiveComboBox;
    private final JComboBox<String> defenseObjectiveComboBox;
    private final JComboBox<String> navigationObjectiveComboBox;

    public PlayerEntryPanel(String title, Player[] players, Fleet fleet) {
        this.title = title;
        this.players = players;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(Constants.SpaceBetweenComponents, Constants.SpaceBetweenComponents, Constants.SpaceBetweenComponents, Constants.SpaceBetweenComponents));

        JLabel fleetLabel = new JLabel(title, SwingConstants.LEFT);
        fleetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(fleetLabel);
        fleetTextArea = new JTextArea(Constants.FleetRows, Constants.FleetColumns);
        fleetTextArea.addKeyListener(new KeyAdapter() { // make TAB go to next field in form
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    if (e.getModifiersEx() > 0) {
                        fleetTextArea.transferFocusBackward();
                    } else {
                        fleetTextArea.transferFocus();
                    }
                    e.consume();
                }
            }
        });
        fleetTextArea.setText(fleet.getFleetForOutput());
        fleetTextArea.setLineWrap(true);
        fleetTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(fleetTextArea);

        ownerComboBox = CreationUtility.createEditableComboBox(
                this,
                "Fleet Owner",
                Arrays.stream(players).filter(Objects::nonNull).map(Player::getName).toArray(String[]::new));

        add(Box.createRigidArea(new Dimension(1, Constants.SpaceBetweenComponents)));

        factionComboBox = CreationUtility.createComboBox(this, "Faction", Armada.Factions, "Choose a faction...");
        fleetPointsTextField = CreationUtility.createTextField(this, "Fleet Points");
        pointsScoredTextField = CreationUtility.createTextField(this, "Points Scored");
        assaultObjectiveComboBox = CreationUtility.createComboBox(this, "Objectives", Armada.AssaultObjectives, "Choose an assault objective...");
        defenseObjectiveComboBox = CreationUtility.createComboBox(this, Armada.DefenseObjectives, "Choose a defense objective...");
        navigationObjectiveComboBox = CreationUtility.createComboBox(this, Armada.NavigationObjectives, "Choose a navigation objective...");

        if(fleet.getAssaultObjective() != null)
            assaultObjectiveComboBox.setSelectedItem(fleet.getAssaultObjective());
        if(fleet.getDefenseObjective() != null)
            defenseObjectiveComboBox.setSelectedItem(fleet.getDefenseObjective());
        if(fleet.getNavigationObjective() != null)
            navigationObjectiveComboBox.setSelectedItem(fleet.getNavigationObjective());
    }

    public void focusFirstField() {
        this.ownerComboBox.requestFocus();
    }

    public boolean validate(ArrayList<String> validationErrors) {
        boolean validatedSuccessfully = true;
        var fleetContents = this.fleetTextArea.getText();
        if(fleetContents == null || fleetContents.equals("")) {
            validationErrors.add(this.title + " has no fleet contents.");
            validatedSuccessfully = false;
        }

        if(this.ownerComboBox.getSelectedItem() == null) {
            validationErrors.add(this.title + " has no selected owner.");
            validatedSuccessfully = false;
        }

        if(this.factionComboBox.getSelectedItem() == null) {
            validationErrors.add(this.title + " has no selected faction.");
            validatedSuccessfully = false;
        }

        var fleetPoints = this.fleetPointsTextField.getText();
        if(fleetPoints.equals("")) {
            validationErrors.add(this.title + " has no fleet points.");
            validatedSuccessfully = false;
        }

        try {
            Integer.parseInt(fleetPoints);
        } catch(NumberFormatException e) {
            validationErrors.add(this.title + " fleet points are not a number.");
            validatedSuccessfully = false;
        }

        var pointsScored = this.pointsScoredTextField.getText();
        if(pointsScored.equals("")) {
            validationErrors.add(this.title + " has no points scored.");
            validatedSuccessfully = false;
        }

        try {
            Integer.parseInt(pointsScored);
        } catch(NumberFormatException e) {
            validationErrors.add(this.title + " points scored are not a number.");
            validatedSuccessfully = false;
        }

        return validatedSuccessfully;
    }

    public PlayerData getPlayerData() {
        var selectedPlayerName = this.ownerComboBox.getSelectedItem().toString();
        var selectedPlayer = Arrays.stream(this.players).filter(player -> player.getName().equals(selectedPlayerName)).findFirst().get();

        return new PlayerData(
                this.getFleet(),
                selectedPlayer,
                this.factionComboBox.getSelectedItem().toString(),
                Integer.parseInt(this.fleetPointsTextField.getText()),
                Integer.parseInt(this.pointsScoredTextField.getText()));
    }

    public boolean belongsToPlayer(String playerName) {
        return this.ownerComboBox.getSelectedItem().equals(playerName);
    }

    private String[] getFleet() {
        Vector<String> fleet = new Vector<>();
        fleet.add("Assault: " + this.assaultObjectiveComboBox.getSelectedItem().toString());
        fleet.add("Defense: " + this.defenseObjectiveComboBox.getSelectedItem().toString());
        fleet.add("Navigation: " + this.navigationObjectiveComboBox.getSelectedItem().toString());
        fleet.addAll(Arrays.asList(this.fleetTextArea.getText().split("\\R")));
        return fleet.toArray(String[]::new);
    }
}
