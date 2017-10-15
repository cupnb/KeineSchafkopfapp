package de.emghaar.screens;

import de.emghaar.ButtonAction;
import de.emghaar.View;

import javax.swing.*;

public abstract class Screen extends JPanel {
    private View view;

    public Screen(View view) {
        this.view = view;
    }

    public void buttonPressed(ButtonAction b) {
        view.buttonPressed(b);
    }
}
