package de.emghaar.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import de.emghaar.game.card.Card;
import de.emghaar.game.card.CardColor;
import de.emghaar.game.card.CardRank;

//Klasse für das Deck des Spiels. Enthält alle 32 Karten Hallu
class Deck {

    //Karten werden in einem Stack gespeichert
    private Stack<Card> cards = new Stack<>();

    //Konstruktor der Klasse Deck
    Deck() {
        //Karten werden erzeugt
        for (CardColor color : CardColor.values()) {
            for (CardRank rank : CardRank.values()) {
                Card card = new Card(color, rank);
                cards.push(card);
            }
        }
        Collections.shuffle(cards);
    }

    //Mischmethode --> Muss man eigentlich nicht verstehen (sie funktionier) --> Fragen an Lukas
    void initialize(Stack<Card> prevDumpedCards) {
        List<Card> shuffleList2 = new ArrayList<>();
        List<Card> shuffleList = new ArrayList<>();

        Random x = new Random();
        int kartenRaus = x.nextInt(15) + 5;
        Random y = new Random();
        int kartenzahlStart = y.nextInt(31) - kartenRaus;
        for (int i = 0;i < kartenzahlStart; i++) {
            shuffleList.add(i, prevDumpedCards.pop());
        }
        for (int j = 0; j < kartenRaus; j++) {
            shuffleList2.add(j,prevDumpedCards.pop());
        }
        for (int k = 0; k < 32-kartenRaus; k++) {
            shuffleList.add(kartenzahlStart + k, prevDumpedCards.pop());
        }
        for (int l = 0; l < kartenRaus; l++) {
            shuffleList.add(32 - kartenRaus, shuffleList2.remove(1));
        }
        cards.clear();
        for (int m = 0; m < 32; m++) {
            cards.push(shuffleList.remove(1));
        }
    }

    //Packt 4 Karten vom Stack in ein neues Stack und gibt dieses zurueck
    Stack<Card> deal() {
        Stack<Card> deal = new Stack<>();
        deal.addAll(cards.subList(0, 4));
        cards.subList(0, 4).clear();
        return deal;
    }

}
