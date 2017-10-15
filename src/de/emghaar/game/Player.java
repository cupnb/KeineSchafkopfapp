package de.emghaar.game;

import java.util.LinkedList;
import java.util.Stack;

import de.emghaar.game.card.Card;

public interface Player
{
    void setGame(Game g);
    void setPlayer(boolean p);
    boolean getPlayer();
    void stichpunkterhoehen();
    int getPunkte();
    void addStich(Stack<Card> s);
    void addCards(Stack<Card> c);
    LinkedList<Card> getHand();
    String getName();
    int setWannaplay();
    Mode.MODE_TYPE play();
    Card kartelegen();
    void onlineSpiel();
    boolean isBot();
    void giveMode(Mode.MODE_TYPE m);
    void giveSpielender(int p);
    void giveNumber(int n);
}
