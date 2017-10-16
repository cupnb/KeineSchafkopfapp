package de.emghaar.game.card;

/**
 * Klasse, die alles beinhaltet, was für eine Karte notwendig ist
 *
 * - Vereinigt CardRank und CardColor
 * - beinhaltet verschiedenste Getter Methoden
 *
 * @author Sebastian Waetzold
 */
//Klasse, die die Eigenschaften einer Karte festlegen
public class Card {
    //Festlegung der Farbe der Karte (z.B. Eichel)
    private CardColor color;
    //Festlegung des "Ranks" (z.B. Ass)
    private CardRank rank;

    /**
     * Konstruktor der Klasse Card
     *
     * Initialisierung von CardRank und CardColor
     *
     * @author Sebastian Waetzold
     * @param c CardColor der Karte
     * @param r CardRank der Karte
     */
    //Konstuktor der Klasse Card
    public Card(CardColor c, CardRank r) {
        color = c;
        rank = r;
    }

    /**
     * Getter Methode von Rank
     * --> Uebergibt den Rank der Karte
     *
     * @author Sebastian Waetzold
     * @return CardRank der Karte
     */
    //getter Methode vom Rank
    public CardRank getRank()
    {
        return rank;
    }

    /**
     * Getter Methode von Color
     * --> Uebergubt die Farbe der Karte
     *
     * @author Sebastian Waetzold
     * @return CardColor der Karte
     */
    //getter Methode von Color
    public CardColor getColor()
    {
        return color;
    }

    /**
     * Uebergibt die Punkte des Spielers
     * --> Getter Methode wird in CardRank aufgerufen
     *
     * @author Sebastian Waetzold
     * @return Punkte, die der Spieler besitzt
     */
    //getter Methode von den Punkten
    public int getPoints() {
        return rank.getPoints();
    }
}
