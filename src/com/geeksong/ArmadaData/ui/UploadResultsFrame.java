package com.geeksong.ArmadaData.ui;

import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import com.geeksong.ArmadaData.model.Armada;
import com.geeksong.ArmadaData.model.EntryData;
import com.geeksong.ArmadaData.model.Game;
import com.geeksong.ArmadaData.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class UploadResultsFrame extends JFrame implements ActionListener {
    private final PlayerEntryPanel leftPanel;
    private final PlayerEntryPanel rightPanel;
    private final JComboBox<String> firstPlayerComboBox;
    private final JComboBox<String> objectivePlayedComboBox;
    private final JTextField tournamentCodeTextField;

    public UploadResultsFrame(Game game) {
        super("Upload Results");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        setFocusCycleRoot(true);
        content.setLayout(new BorderLayout());
        getContentPane().add(content);

        var players = game.getPlayers();
        leftPanel = new PlayerEntryPanel("Fleet One", players, game.getTopPlayerFleet());
        content.add(BorderLayout.WEST, leftPanel);

        rightPanel = new PlayerEntryPanel("Fleet Two", players, game.getBottomPlayerFleet());
        content.add(BorderLayout.EAST, rightPanel);

        leftPanel.add(Box.createRigidArea(new Dimension(1, Constants.SpaceBetweenComponents)));
        firstPlayerComboBox = CreationUtility.createEditableComboBox(
                leftPanel,
                "First Player",
                Arrays.stream(players).filter(player -> player != null).map(Player::getName).toArray(String[]::new));
        objectivePlayedComboBox = CreationUtility.createComboBox(leftPanel, "Objective Played", Armada.Objectives, "Choose an objective...");
        tournamentCodeTextField = CreationUtility.createTextField(leftPanel, "Tournament Code (optional)");

        // Not a great plan. This forces the right content to the top of the right panel, even after adding all the above, using a carefully determined static height.
        // Better solution is likely something with GridBag or Grid, but I'd got it working mostly with BoxLayout and didn't want to do it all again.
        rightPanel.add(Box.createRigidArea(new Dimension(1, 140)));

        var actionPanel = new ActionPanel(this);
        content.add(actionPanel, BorderLayout.SOUTH);

        pack();
        leftPanel.focusFirstField();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Validate results
        ArrayList<String> validationErrors = new ArrayList<>();
        boolean validatedSuccessfully = true;

        if(!this.leftPanel.validate(validationErrors))
            validatedSuccessfully = false;

        if(!this.rightPanel.validate(validationErrors))
            validatedSuccessfully = false;

        if(this.firstPlayerComboBox.getSelectedItem() == null) {
            validationErrors.add("No first player selected.");
            validatedSuccessfully = false;
        }

        if(this.objectivePlayedComboBox.getSelectedItem() == null) {
            validationErrors.add("No objective selected.");
            validatedSuccessfully = false;
        }

        // Display validation errors if any
        if(!validatedSuccessfully) {
            JOptionPane.showMessageDialog(
                    this,
                    String.join("\n", validationErrors),
                    "Validation Errors",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // build API request
        var entryData = getEntryData();

        // Submit to API
        var successful = sendDataToApi(entryData);

        // Close frame
        if(successful)
            setVisible(false);
    }

    private EntryData getEntryData() {
        if (isLeftFirstPlayer()) {
            return new EntryData(
                    this.leftPanel.getPlayerData(),
                    this.rightPanel.getPlayerData(),
                    this.objectivePlayedComboBox.getSelectedItem().toString(),
                    this.tournamentCodeTextField.getText());
        } else {
            return new EntryData(
                    this.rightPanel.getPlayerData(),
                    this.leftPanel.getPlayerData(),
                    this.objectivePlayedComboBox.getSelectedItem().toString(),
                    this.tournamentCodeTextField.getText());
        }
    }

    private boolean isLeftFirstPlayer() {
        var firstPlayerName = this.firstPlayerComboBox.getSelectedItem().toString();
        return this.leftPanel.belongsToPlayer(firstPlayerName);
    }

    private boolean sendDataToApi(EntryData entryData) {
        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest
                .newBuilder(URI.create("https://firestore.googleapis.com/v1/projects/ttsarmada/databases/(default)/documents/games_vassal"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(entryData.getJsonRequest()))
                .build();
        var chatter = GameModule.getGameModule().getChatter();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() >= 200 && response.statusCode() <= 299) {
                chatter.show("Upload successful! Thanks!");
                return true;
            } else {
                showError(chatter, response.body());
                return false;
            }
        } catch(IOException | InterruptedException ex) {
            showError(chatter, ex.toString());
            return false;
        }
    }

    private void showError(Chatter chatter, String error) {
        JOptionPane.showMessageDialog(
                this,
                "Error uploading results, see Vassal log for details.",
                "Error uploading results.",
                JOptionPane.ERROR_MESSAGE);
        chatter.show("Upload unsuccessful, please send this to the extension maintainer.");
        chatter.show(error.toString());
    }
}

