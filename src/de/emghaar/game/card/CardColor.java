package de.emghaar.game.card;

//Festlegung der Farbe einer Karte mithilfe eines Enums
public enum CardColor {
    //Festlegung der 4 Farben
    EICHEL("eichel"),
    LAUB("laub"),
    HERZ("herz"),
    SCHELLEN("schellen");

    //Name der jeweiligen Farbe (z.B. "eichel")
    private String name;

    //Konstruktor der Klasse
    CardColor(String name) {
        this.name = name;
    }

    //getter Methode für name
    public String getName() {
        return name;
    }

    public int convertToInt()
    {
        switch(getName()) {
            case "eichel": return 3;
            case "laub": return 2;
            case "schellen": return 1;
            default: return -1;
        }
    }
}
