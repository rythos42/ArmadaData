package com.geeksong.ArmadaData.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class CreationUtility {
    public static JComboBox<String> createComboBox(JPanel parent, String labelText, String[] options, String placeholderText) {
        parent.add(Box.createRigidArea(new Dimension(1, Constants.SpaceBetweenComponents)));
        JLabel label = new JLabel(labelText, SwingConstants.LEFT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
        return internalCreateComboBox(parent, options, placeholderText, false);
    }

    public static JComboBox<String> createEditableComboBox(JPanel parent, String labelText, String[] options) {
        parent.add(Box.createRigidArea(new Dimension(1, Constants.SpaceBetweenComponents)));
        JLabel label = new JLabel(labelText, SwingConstants.LEFT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
        return internalCreateComboBox(parent, options, "", true);
    }

    public static JComboBox<String> createComboBox(JPanel parent, String[] options, String placeholderText) {
        parent.add(Box.createRigidArea(new Dimension(1, Constants.SpaceBetweenComponents)));
        return internalCreateComboBox(parent, options, placeholderText, false);
    }

    private static JComboBox<String> internalCreateComboBox(JPanel parent, String[] options, String placeholderText, boolean editable) {
        Vector<String> comboBoxOptions = new Vector<>();
        comboBoxOptions.add(null);
        comboBoxOptions.addAll(Arrays.asList(options));
        JComboBox<String> comboBox = new JComboBox<>(comboBoxOptions);
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBox.setRenderer(new PromptComboBoxRenderer(placeholderText));
        comboBox.setEditable(editable);
        comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);    // allows arrow keys to open popup and be selectable
        parent.add(comboBox);
        return comboBox;
    }

    public static JTextField createTextField(JPanel parent, String label) {
        parent.add(Box.createRigidArea(new Dimension(1, Constants.SpaceBetweenComponents)));
        JLabel fleetPointsLabel = new JLabel(label, SwingConstants.LEFT);
        fleetPointsLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        parent.add(fleetPointsLabel);
        JTextField textField = new JTextField();
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(textField);
        return textField;
    }
}
