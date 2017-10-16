package de.emghaar.game.card;

/**
 * Enum, in dem alle Farben der Karten enthalten sind
 *
 * Variable Name: Name der jeweiligen Farbe
 *
 * @author Sebastian Waetzold
 */
//Festlegung der Farbe einer Karte mithilfe eines Enums
public enum CardColor {
    //Festlegung der 4 Farben
    EICHEL("eichel"),
    LAUB("laub"),
    HERZ("herz"),
    SCHELLEN("schellen");

    //Name der jeweiligen Farbe (z.B. "eichel")
    private String name;

    /**
     * Konstruktor des Enums CardColor
     *
     * @author Sebastian Waetzold
     * @param name Name der Kartenfarbe
     */
    //Konstruktor der Klasse
    CardColor(String name) {
        this.name = name;
    }

    /**
     * Getter Methode von Name
     *
     * @author Sebastian Waetzold
     * @return Name der Karte
     */
    //getter Methode für name
    public String getName() {
        return name;
    }

    /**
     * Konvertiert die Kartenfarbe in Nummern
     *
     * @author Alex Ullrich
     * @return Zahl der Kartenfarbe
     */
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
