package com.geeksong.ArmadaData.ui;

import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import com.geeksong.ArmadaData.model.Armada;
import com.geeksong.ArmadaData.model.EntryData;
import com.geeksong.ArmadaData.model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class UploadResultsFrame extends JFrame implements ActionListener {
    private final PlayerEntryPanel leftPanel;
    private final PlayerEntryPanel rightPanel;
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
        var topPlayerIsFirstPlayer = game.isTopPlayerFirstPlayer();
        leftPanel = new PlayerEntryPanel("Fleet One", players, game.getTopPlayerFleet(), topPlayerIsFirstPlayer);
        content.add(BorderLayout.WEST, leftPanel);

        rightPanel = new PlayerEntryPanel("Fleet Two", players, game.getBottomPlayerFleet(), topPlayerIsFirstPlayer != null && !topPlayerIsFirstPlayer);
        content.add(BorderLayout.EAST, rightPanel);

        leftPanel.getFirstPlayerRadio().addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                rightPanel.getFirstPlayerRadio().setSelected(false);
        });

        rightPanel.getFirstPlayerRadio().addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                leftPanel.getFirstPlayerRadio().setSelected(false);
        });

        leftPanel.add(Box.createRigidArea(new Dimension(1, Constants.SpaceBetweenComponents)));
        objectivePlayedComboBox = CreationUtility.createComboBox(leftPanel, "Objective Played", Armada.Objectives, "Choose an objective...");
        objectivePlayedComboBox.setSelectedItem(game.getPlayedObjective());
        tournamentCodeTextField = CreationUtility.createTextField(leftPanel, "Tournament Code (optional)");

        // Not a great plan. This forces the right content to the top of the right panel, even after adding all the above, using a carefully determined static height.
        // Better solution is likely something with GridBag or Grid, but I'd got it working mostly with BoxLayout and didn't want to do it all again.
        rightPanel.add(Box.createRigidArea(new Dimension(1, 100)));

        var actionPanel = new ActionPanel(this, e -> this.setVisible(false));
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

        if(!this.leftPanel.getFirstPlayerRadio().isSelected() && !this.rightPanel.getFirstPlayerRadio().isSelected()) {
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
        if (this.leftPanel.getFirstPlayerRadio().isSelected()) {
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

    private boolean sendDataToApi(EntryData entryData) {
        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest
                .newBuilder(URI.create("https://firestore.googleapis.com/v1/projects/ttsarmada/databases/(default)/documents/games_vassal"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(entryData.getJsonRequest()))
                .build();
        var chat = GameModule.getGameModule().getChatter();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() >= 200 && response.statusCode() <= 299) {
                chat.show("ArmadaData: Upload successful! Thanks!");
                return true;
            } else {
                showError(chat, response.body());
                return false;
            }
        } catch(IOException | InterruptedException ex) {
            StringWriter exceptionStackTrace = new StringWriter();
            PrintWriter printWriter = new PrintWriter(exceptionStackTrace);
            ex.printStackTrace(printWriter);

            showError(chat, exceptionStackTrace.toString());
            return false;
        }
    }

    private void showError(Chatter chat, String error) {
        JOptionPane.showMessageDialog(
                this,
                "Error uploading results, see Vassal log for details.",
                "Error uploading results.",
                JOptionPane.ERROR_MESSAGE);

        chat.show(error);
        chat.show("ArmadaData: Error uploading results. Send the above text to the extension maintainer.");
    }
}

