package de.emghaar;

import de.emghaar.game.Game;
import de.emghaar.game.Human;

import java.util.Observable;

public class Controller extends Observable {
    private View view;
    private Game game;

    public Controller() {
        //game = new Game(new Human("Alex"), new Human("Noah"), new Human("Sebi"), new Human("Niggo"));
        view = new View(this);
        addObserver(view);
        setChanged();
        notifyObservers("MainMenu");
        //game.addObserver(view);
    }

    void buttonPressed(ButtonAction b) {
        System.out.println(b);
        switch (b)
        {
            case START:
                game = new Game(new Human("Alex"), new Human("Noah"), new Human("Sebi"), new Human("Niggo"));
        }

    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}