package com.geeksong.ArmadaData.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class PromptComboBoxRenderer implements ListCellRenderer {
    private String prompt;
    private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    public PromptComboBoxRenderer(String prompt) {
        this.prompt = prompt;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        var component = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value == null || value.equals(""))
            component.setText(prompt);

        if (cellHasFocus || isSelected) {
            component.setBorder(new LineBorder(Color.DARK_GRAY));
        } else {
            component.setBorder(null);
        }

        return component;
    }
}
