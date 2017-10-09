package de.emghaar.game.card;

//Klasse für den Rank einer Karte (z.B. Ass)
public enum CardRank {
    //Festlegung der 8 Ranks
    ASS("ass", 11, 60),
    ZEHN("zehn", 10, 50),
    KOENIG("koenig", 4, 40),
    OBER("ober", 3, 80),
    UNTER("unter", 2, 70),
    NEUN("neun", 0, 30),
    ACHT("acht", 0, 20),
    SIEBEN("sieben", 0, 10);

    //Name des Ranks (z.B. "ass")
    private String name;
    //Punktanzahl des jeweiligen Ranks
    private int points;
    //Wert zum Vergleich von 2 Karten --> Wird gebracuht um in verschieden Modi verschiedene Werte für die Karten festzulegen
    private int comparison;

    //Konstruktor der Klasse CardRank
    CardRank(String displayName, int points, int comparison) {
        this.name = displayName;
        this.points = points;
        this.comparison = comparison;
    }

    //getter Methode von name
    public String getName()
    {
        return name;
    }

    //getter Methode von points
    public int getPoints()
    {
        return points;
    }

    //setter Methode von comparision
    public void setComparison(int comparison)
    {
        this.comparison = comparison;
    }

    //getter Methode von comparision
    public int getComparison()
    {
        return comparison;
    }
}
