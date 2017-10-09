package de.emghaar.game.card;

//Klasse, die die Eigenschaften einer Karte festlegen
public class Card {
    //Festlegung der Farbe der Karte (z.B. Eichel)
    private CardColor color;
    //Festlegung des "Ranks" (z.B. Ass)
    private CardRank rank;

    //Konstuktor der Klasse Card
    public Card(CardColor c, CardRank r) {
        color = c;
        rank = r;
    }

    //getter Methode vom Rank
    public CardRank getRank()
    {
        return rank;
    }

    //getter Methode von Color
    public CardColor getColor()
    {
        return color;
    }

    //getter Methode von den Punkten
    public int getPoints() {
        return rank.getPoints();
    }
}
