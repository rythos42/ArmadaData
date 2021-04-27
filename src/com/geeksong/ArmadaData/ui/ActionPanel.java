package com.geeksong.ArmadaData.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ActionPanel extends JPanel {
    public ActionPanel(ActionListener uploadActionListener, ActionListener cancelActionListener) {
        setLayout(new FlowLayout(FlowLayout.RIGHT, Constants.SpaceBetweenComponents, Constants.SpaceBetweenComponents));

        JLabel helpHyperLink = new JLabel("Less data entry?");
        helpHyperLink.setForeground(Color.BLUE.darker());
        helpHyperLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        helpHyperLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/rythos42/ArmadaData/wiki/Less-data-entry%3F"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                helpHyperLink.setText("<html><a href=''>Less data entry?</a></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                helpHyperLink.setText("Less data entry?");
            }
        });
        add(helpHyperLink);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(cancelActionListener);
        add(cancelButton);

        JButton uploadButton = new JButton("Upload");
        uploadButton.addActionListener(uploadActionListener);
        add(uploadButton);
    }


}
