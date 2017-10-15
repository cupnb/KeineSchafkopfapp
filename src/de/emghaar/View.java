package de.emghaar;


import de.emghaar.screens.GameScreen;
import de.emghaar.screens.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class View extends JFrame implements Observer {
    private JPanel panel;

    private Controller controller;

    public View(Controller controller) {
        super("Keine Schafkopfapp");
        setSize(640, 360);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        this.controller = controller;
        panel = new JPanel(new CardLayout());
        panel.add(new GameScreen(this));
        panel.add(new MainMenu(this));
        add(panel);
        setVisible(true);

    }

    public void restart() {

    }

    public void buttonPressed(ButtonAction b) {
        controller.buttonPressed(b);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch (o.toString()) {
            case "Game":
                break;
            case "Controller":
                CardLayout c1 = (CardLayout) panel.getLayout();
                switch ((String) arg) {
                    case "MainMenu":
                        c1.show(panel, (String) arg);
                        break;
                }
        }
    }
}
