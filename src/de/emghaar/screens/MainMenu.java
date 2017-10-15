package de.emghaar.screens;

import de.emghaar.ButtonAction;
import de.emghaar.View;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends Screen {

    private JButton startButton;
    private JLabel headLine;

    public MainMenu(View view) {
        super(view);
        headLine = new JLabel("Willkommen in der GUI unserer Schafkopf App");
        startButton = new JButton("Starte das Spiel");
        startButton.addActionListener(e -> buttonPressed(ButtonAction.START));
        add(startButton, BorderLayout.CENTER);
        add(headLine, BorderLayout.PAGE_START);
        setVisible(true);
    }
}
