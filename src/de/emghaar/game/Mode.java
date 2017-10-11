package de.emghaar.game;


import java.util.LinkedList;
import java.util.Stack;

import de.emghaar.game.card.Card;
import de.emghaar.game.card.CardColor;
import de.emghaar.game.card.CardRank;

import static de.emghaar.game.Mode.MODE_TYPE.*;


//Klasse die alles um den Spielmodus enthält
public class Mode {
    private MODE_TYPE mode_type;
    //Ruffarbe des Modes (bei Soli, Ramsch oder Wenz --> null)
    //1 = Schellen
    //2 = Laub
    //3 = Eichel
    //-1 = keine
    private int trumpfcolor;

    Mode(MODE_TYPE m)
    {
        mode_type = m;
        trumpfcolor = -1;
    }

    //getter Methode
    MODE_TYPE getModeType()
    {
        return mode_type;
    }

    //setter Methode
    void setModeType(MODE_TYPE m)
    {
        mode_type = m;
    }

    //getter Methode
    int getTrumpfcolor()
    {
        return trumpfcolor;
    }

    //setter Methode
    void setTrumpfcolor(int y)
    {
        trumpfcolor = y;
    }

    //spielbare Karten werden geckeckt und mit einer LinkedList
    //Methode zur anzeigen der spielbaren Karten
    LinkedList<Card> showPlayableCards(LinkedList<Card> c1, Stack<Card> c2, int Ruffarbe, MODE_TYPE c3)
    {
        //Karte, die im Stich ganz unten liegt
        Card unten = null;
        //unterste Karte wird durch pop() vom Stack geholt
        if(!c2.empty()) {
            unten = c2.lastElement();
        }
        //LinkedList wird in einen Array umgewandelt
        Card[] temporaryArray = new Card[c1.size()];
        //Erste Karte von der LinkedList wird einem Index zugeordnet und danach aus der LinkedList gelöscht
        for(int j=c1.size(); j>0; j--)
        {
            temporaryArray[j-1]= c1.removeFirst();
        }
        //LinkedList die am Ende zurückgegeben wird
        LinkedList<Card> giveBack = new LinkedList<>();
        //Alle Karten im Array werden geprüft
        for(int i=temporaryArray.length; i>0; i--)
        {
            //Wenn der Mode ein Sauspiel ist, wird das Prüfen darauf angepasst
            if(c3 == SAUSPIELEICHEL || c3 == SAUSPIELGRAS || c3 == SAUSPIELSCHELLEN)
            {
                //Wenn das Prüfen positiv ausfällt, wird die Karte zur LinkedList hinzugefügt
                if (pruefenSauSpiel(temporaryArray[i - 1], unten, temporaryArray, Ruffarbe)) {
                    //Karte wird zur LinkedList hinzugefügt
                    giveBack.addFirst(temporaryArray[i - 1]);
                }
            }
            //Wenn der Mode ein Solo, Wenz oder Ramsch ist, wird das Prüfen darauf angepasst
            else
            {
                //Wenn das Prüfen positiv ausfällt, wird die Karte zur LinkedList hinzugefügt
                if (pruefenSoloRamschWenz(temporaryArray[i - 1], unten, temporaryArray, c3)) {
                    //Karte wird zur LinkedList hinzugefügt
                    giveBack.addFirst(temporaryArray[i - 1]);
                }
            }
        }
        //LinkedList wird zurückgegeben
        return giveBack;
    }

    //Methode zum Prüfen einer Karte und ob sie gespielt werden darf bzw. nicht
    private boolean pruefenSauSpiel(Card c1, Card unten, Card[] c3, int Ruffarbe)
    {
        //Boolean zur Bestimmung, ob das Ass auf der Hand ist
        boolean ass = false;
        //Wenn unten keine Karte liegt
        if(unten == null)
        {
            //Wenn die angespielte Farbe die Ruffarbe ist (Farbe, auf die gespielt wird)
            if(c1.getColor().convertToInt() == Ruffarbe)
            {
                //Durchschauen, ob das Ass auf der Hand ist
                for(int p=c3.length;p>0;p--)
                {
                    //Wenn das Ass dabei ist wird der Boolean auf true gesetzt
                    if(c3[p-1].getColor().convertToInt() == Ruffarbe && c3[p-1].getRank() == CardRank.ASS)
                    {
                        ass = true;
                    }
                }
                //Wenn das Ass vorhanden ist
                if(ass)
                {
                    //Wenn die zu überprüfende Karte das Ass ist, darf sie gelegt werden
                    if(c1.getRank().getName().equals("ass"))
                    {
                        return true;
                    }
                    //Sonst wird geschaut, ob der Spieler mehr als 4 Karten der Farbe hat
				else
                    {
                        //Int zur Bestimmung der Anzahl der Karten
                        int y = 0;
                        //Durchlaufen aller Karten der Hand
                        for(int k=c3.length;k>0;k--)
                        {
                            //Farbe der Karten wird mit der Ruffarbe abgeglichen --> Ja, dann wird y um 1 erhöht
                            if(c3[k-1].getColor().convertToInt() == Ruffarbe)
                            {
                                //Ober und Unter der Ruffarbe werden ausgeschlossen --> Zählen nicht als normale Farbkarten --> y wird um 1 erhöht
                                if(c3[k-1].getRank() != CardRank.OBER|| c3[k-1].getRank() != CardRank.UNTER)
                                {
                                    y = y+1;
                                }
                            }
                        }
                        //Wenn y größer vier, darf sie gespielt werden
                        return y >= 4;
                    }
                }
                //Wenn das Ass nicht da ist, darf er die Karte spielen
                else
                {
                    return true;
                }

            }
            //Wenn es nicht mal die Ruffarbe ist, dann darf er sie auch spielen
            else
            {
                return true;
            }
        }
        //Wenn unten eine Karte liegt
        else {
            //Wenn die unterste Karte die gleiche Farbe hat, wie die Karte, die geprüft wird und die Karte kein Ober bzw. Unter ist
            if (c1.getColor() == unten.getColor() && c1.getRank() != CardRank.UNTER && c1.getRank() != CardRank.OBER)
            {
                //Wenn die angespielte Farbe die Ruffarbe ist (Farbe, auf die gespielt wird)
                if (c1.getColor().convertToInt() == Ruffarbe) {
                    //Durchschauen, ob das Ass auf der Hand ist
                    for (int z = c3.length; z > 0; z--) {
                        //Wenn ja, wird der boolean auf true gesetzt
                        if (c3[z - 1].getColor().convertToInt() == Ruffarbe && c3[z - 1].getRank() == CardRank.ASS) {
                            ass = true;
                        }
                    }
                    //Wenn das Ass auf der Hand vorhanden ist
                    //Wenn die Karte das Ass ist, darf er sie legen; Wenn es nicht das Ass sist, dann nicht
                    //Er darf die Karte legen, wenn es nicht das Ass ist
                    return !ass || c1.getRank() == CardRank.ASS;
                }
                //Er darf die Karte legen, wenn die vorherigen Bedingungen nicht erfüllt sind (RufAss nicht auf Hand vorhanden bzw. Karte ist nicht das RufAss)
                else
                    {
                    return true;
                }
            } else if (c1.getRank() == CardRank.UNTER && c1.getRank() == CardRank.OBER) {
                //Boolean zum Herausfinden, ob eine weitere Karte der gleichen Farbe in der Hand ist, die nicht der gespielte Ober bzw. Unter ist
                boolean weitereKarteOU = false;
                //Durchsuchen des Feldes auf andere Karten der gleichen Farben, die nicht der gesuchte Ober bzw. Unter sind
                for (int q = c3.length; q > 0; q--) {
                    if (c3[q - 1].getColor() == unten.getColor() && c1.getRank() != CardRank.UNTER && c1.getRank() != CardRank.OBER) {
                        weitereKarteOU = true;
                    }
                }
                //Wenn keine weitere Karte der Farbe vorhanden ist, darf die Karte gelegt werden
                return !weitereKarteOU;
            }
            //Wenn die unterste Karte eine andere Farbe hat und die Karte kein Ober bzw. Unter ist
            else {
                //Boolean zum Herausfinden, ob eine weitere Karte der gleichen Farbe in der Hand ist
                boolean weitereKarte = false;
                for (int q = c3.length; q > 0; q--) {
                    if (c3[q - 1].getColor() == unten.getColor()) {
                        weitereKarte = true;
                    }
                }
                //Wenn keine weitere Karte der Farbe vorhanden ist, darf die Karte gelegt werden
                return !weitereKarte;
            }
        }
    }

    private boolean pruefenSoloRamschWenz(Card c1, Card unten, Card[] c3, MODE_TYPE c4)
    {
        if(c4 == SOLOEICHEL || c4 == SOLOGRAS || c4 == SOLOHERZ || c4 == SOLOSCHELLEN || c4 == RAMSCH) {
            //Wenn noch keine Karte liegt, darf der Spiler frei wählen, welche Karte er legen will --> Jede Karte darf gelegt werden
            if (unten == null) {
                return true;
            }
            //Wenn die unterste Karte die gleiche Farbe hat, wie die geprüfte und die Karte kein Ober bzw. Unter ist, darf sie gelegt werden
            if (c1.getColor() == unten.getColor() && c1.getRank() != CardRank.UNTER && c1.getRank() != CardRank.OBER) {
                return true;
            }
            //Sonst wird geschaut, ob noch weitere Karten der Farbe auf der Hand sind
            else {
                //Boolean, der auf true gesetzt wird, sobald der Player eine Farbkarte der zuerst gespielten Karte hat
                boolean farbeVorhanden = false;
                //Durchlaufen des Feldes zum Suchen einer Farbkarte der zuerst gespielten Karte
                for (int i = 0; i < c3.length; i++) {
                    //Wenn der Spieler eine weitere Farbkarte auf der Hand hat, wird der Boolean auf true gesetzt
                    if (c1.getColor() == unten.getColor() && c1.getRank() != CardRank.UNTER && c1.getRank() != CardRank.OBER) {
                        farbeVorhanden = true;
                    }
                }
                //Wenn eine Farbkarte vorhanden ist, darf der Spieler sie nicht spielen
                //Wenn er keine weitere Farbkarte vorhanden ist, darf er sie legen
                return !farbeVorhanden;
            }
        }
        else
        {
            //Wenn noch keine Karte liegt, darf der Spiler frei wählen, welche Karte er legen will --> Jede Karte darf gelegt werden
            if (unten == null) {
                return true;
            }
            //Wenn die unterste Karte die gleiche Farbe hat, wie die geprüfte und die Karte kein Unter ist, darf sie gelegt werden
            if (c1.getColor() == unten.getColor() && c1.getRank() != CardRank.UNTER) {
                return true;
            }
            //Sonst wird geschaut, ob noch weitere Karten der Farbe auf der Hand sind
            else {
                //Boolean, der auf true gesetzt wird, sobald der Player eine Farbkarte der zuerst gespielten Karte hat
                boolean farbeVorhanden = false;
                //Durchlaufen des Feldes zum Suchen einer Farbkarte der zuerst gespielten Karte
                for (int i = 0; i < c3.length; i--) {
                    //Wenn der Spieler eine weitere Farbkarte auf der Hand hat, wird der Boolean auf true gesetzt
                    if (c1.getColor() == unten.getColor() && c1.getRank() != CardRank.UNTER) {
                        farbeVorhanden = true;
                    }
                }
                //Wenn eine Farbkarte vorhanden ist, darf der Spieler sie nicht spielen
                //Wenn er keine weitere Farbkarte vorhanden ist, darf er sie legen
                return !farbeVorhanden;
            }
        }
    }

    //geeignet für Wenz und alle Soli (außer Herz)
    void comparisonAktualisieren(LinkedList<Card> c1, MODE_TYPE m)
    {
        for(Card temporary: c1)
        {
            switch (m)
            {
                case WENZ:
                    switch(temporary.getRank())
                    {
                        case ZEHN: temporary.getRank().setComparison(35);
                            break;
                        case OBER: temporary.getRank().setComparison(36);
                            break;
                    }
                    break;
                case SOLOSCHELLEN:
                    if(temporary.getColor() == CardColor.SCHELLEN)
                    {
                        setComparison(temporary);
                    }
                    break;
                case SOLOGRAS:
                    if(temporary.getColor() == CardColor.LAUB)
                    {
                        setComparison(temporary);
                    }
                    break;
                case SOLOEICHEL:
                    if(temporary.getColor() == CardColor.EICHEL)
                    {
                        setComparison(temporary);
                    }
                    break;
            }

        }
    }

    //geeignet für Ramsch, Solo Herz und alle Sauspiele
    void comparisonSetStandard(LinkedList<Card> c1)
    {
        c1.stream().filter(temporary -> temporary.getColor() == CardColor.HERZ).forEach(this::setComparison);
    }

    //muss immer gemacht werden
    void comparisonOberUnter(LinkedList<Card> c1)
    {
        for(Card temporary: c1)
        {
            if(temporary.getRank() == CardRank.UNTER)
            {
                switch (temporary.getColor())
                {
                    case SCHELLEN: temporary.getRank().setComparison(71);
                        break;
                    case HERZ: temporary.getRank().setComparison(72);
                        break;
                    case LAUB: temporary.getRank().setComparison(73);
                        break;
                    case EICHEL: temporary.getRank().setComparison(74);
                        break;
                }
            }
            if(temporary.getRank() == CardRank.OBER)
            {
                switch (temporary.getColor())
                {
                    case SCHELLEN: temporary.getRank().setComparison(81);
                        break;
                    case HERZ: temporary.getRank().setComparison(82);
                        break;
                    case LAUB: temporary.getRank().setComparison(83);
                        break;
                    case EICHEL: temporary.getRank().setComparison(84);
                        break;
                }
            }
        }
    }

    boolean SauSpielSpielbar(LinkedList<Card> c1, Mode c2)
    {
        //Wenn ein Spiel ein Sauspiel ist, dann
        if (c2.getModeType() == SAUSPIELGRAS || c2.getModeType() == SAUSPIELSCHELLEN || c2.getModeType() == SAUSPIELEICHEL)
        {
            switch (c2.getModeType()) {
                case SAUSPIELGRAS:
                    return assSuchen(c1, CardColor.LAUB);
                case SAUSPIELEICHEL:
                    return assSuchen(c1, CardColor.EICHEL);
                case SAUSPIELSCHELLEN:
                    return assSuchen(c1, CardColor.SCHELLEN);
                default:
                    return true;
            }
        }
        else
        {
            return false;
        }
    }

    private boolean assSuchen(LinkedList<Card> c3, CardColor colorNew)
    {
        for(Card karte:c3)
        {
            if(karte.getRank() == CardRank.ASS && karte.getColor() == colorNew)
            {
                return false;
            }
        }
        return true;
    }

    private void setComparison(Card temporary) {
        switch (temporary.getRank()) {
            case SIEBEN:
                temporary.getRank().setComparison(61);
                break;
            case ACHT:
                temporary.getRank().setComparison(62);
                break;
            case NEUN:
                temporary.getRank().setComparison(63);
                break;
            case KOENIG:
                temporary.getRank().setComparison(64);
                break;
            case ZEHN:
                temporary.getRank().setComparison(65);
                break;
            case ASS:
                temporary.getRank().setComparison(66);
                break;
        }
    }

    //Enumeration für alle Modes
    public enum MODE_TYPE {
        NICHTS,
        SAUSPIELSCHELLEN,
        SAUSPIELGRAS,
        SAUSPIELEICHEL,
        WENZ,
        SOLOSCHELLEN,
        SOLOGRAS,
        SOLOEICHEL,
        SOLOHERZ,
        RAMSCH;

        //Vergleichen von zwei Modes
        public static MODE_TYPE vergleiche(String value) {
            if (value.equalsIgnoreCase(SAUSPIELSCHELLEN.toString()))
                return MODE_TYPE.SAUSPIELSCHELLEN;
            else if (value.equalsIgnoreCase(SAUSPIELGRAS.toString()))
                return MODE_TYPE.SAUSPIELGRAS;
            else if (value.equalsIgnoreCase(SAUSPIELEICHEL.toString()))
                return MODE_TYPE.SAUSPIELEICHEL;
            else if (value.equalsIgnoreCase(WENZ.toString()))
                return WENZ;
            else if (value.equalsIgnoreCase(SOLOSCHELLEN.toString()))
                return MODE_TYPE.SOLOSCHELLEN;
            else if (value.equalsIgnoreCase(SOLOGRAS.toString()))
                return MODE_TYPE.SOLOGRAS;
            else if (value.equalsIgnoreCase(SOLOEICHEL.toString()))
                return MODE_TYPE.SOLOEICHEL;
            else if (value.equalsIgnoreCase(SOLOHERZ.toString()))
                return MODE_TYPE.SOLOHERZ;
            else if (value.equalsIgnoreCase(RAMSCH.toString()))
                return MODE_TYPE.RAMSCH;
            else if (value.equalsIgnoreCase(NICHTS.toString()))
                return MODE_TYPE.NICHTS;
            else
                return null;
        }

        //Methode, die einen Modus zum String macht
        public String toString() {
            switch (this) {
                case SAUSPIELSCHELLEN:
                    return "SAUSPIELSCHELLEN";
                case SAUSPIELGRAS:
                    return "SAUSPIELGRAS";
                case SAUSPIELEICHEL:
                    return "SAUSPIELEICHEL";
                case WENZ:
                    return "WENZ";
                case SOLOSCHELLEN:
                    return "SOLOSCHELLEN";
                case SOLOGRAS:
                    return "SOLOGRAS";
                case SOLOEICHEL:
                    return "SOLOEICHEL";
                case SOLOHERZ:
                    return "SOLOHERZ";
                case RAMSCH:
                    return "RAMSCH";
                case NICHTS:
                    return "NICHTS";
            }
            return null;
        }

        int getOrdinal(String name) {
            try {
                return MODE_TYPE.valueOf(name).ordinal();
            } catch (IllegalArgumentException e) {
                return -1;
            }
        }
    }
}