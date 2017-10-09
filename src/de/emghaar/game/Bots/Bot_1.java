package de.emghaar.game.Bots;/*package de.emg_haar.schafkopfdeluxe.game.Bots;
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.LinkedList;

import de.emg_haar.schafkopfdeluxe.game.Mode;
import de.emg_haar.schafkopfdeluxe.game.card.Card;
import de.emg_haar.schafkopfdeluxe.game.card.CardColor;
import de.emg_haar.schafkopfdeluxe.game.card.CardRank;
import sun.awt.image.ImageWatched;

public class Bot_1 extends Bot {



    Die Attribute, die Bot_1 erbt:

    protected LinkedList<Card> hand;
    protected String name;
    protected Game game;
    protected int points;
    protected boolean player;
    protected boolean wannaplay;
    protected boolean online;
    protected Stack<Card> stiche;
    protected int punkte;
    protected int stichanzahl;
    protected boolean turn;
    protected boolean bot;



    public Bot_1() {
        this.name = "Faust";
        bot = true;
    }


     * Um das zu verstehen, bieten sich die Methoden an, die ich aus Javadocs habe:
     * https://docs.oracle.com/javase/8/docs/api/index.html?java/util/LinkedList.html
     *
     * Die Abfragen sehen wie eine ganze Menge Müll aus.
     * Sie erleichtern aber so gut wie ALLES, was danach kommt.
     *
     * @return gewünschter Spielmodus des Bots

    private String chooseGamemode() {

        // Es braucht alle 8 Karten auf der Hand.
        if( hand.size() != 8 ) {
            return null;
        }

        // Hilfsvariablen:

        // Anzahl Herz + Anzahl Ober + Anzahl Unter
        int anzahlTrumpf = 0;
        for(int i = 0; i <= 7; i++) {
            if(
                    hand.get(i).getColor() == CardColor.HERZ ||
                    hand.get(i).getRank() == CardRank.UNTER ||
                    hand.get(i).getRank() == CardRank.OBER
                    ){
                anzahlTrumpf++;
            }
        }

        // Anzahl der Säue, die nicht Trumpf sind
        //Wofür wird die anzahlFarbSau gebraucht?
        //Ulli
        int anzahlFarbSau = 0;
        for( int i = 0; i <= 7; i++ ) {
            if(
                    hand.get(i).getRank() == CardRank.ASS &&
                    hand.get(i).getColor() != CardColor.HERZ
                    ) {
                anzahlFarbSau++;
            }
        }

        // Anzahl der Säue
        int anzahlSau = anzahlKarten(CardRank.ASS);

        // Anzahl Ober
        int anzahlOber = anzahlKarten(CardRank.OBER);

        // Anzahl Unter
        int anzahlUnter = anzahlKarten(CardRank.UNTER);

        // Anzahl Ober außer SchellenOber
        //Wofür ist Anzahl Brems nötig?
        //Ulli
        int anzahlBrems = anzahlOber;
        for( int i = 0; i <= 7; i++ ) {
            if(
                    hand.get(i).getRank() == CardRank.OBER &&
                    hand.get(i).getColor() == CardColor.SCHELLEN
                    ) {
                anzahlBrems--;
            }
        }

        // Anzahl Eichel, die nicht Trumpf sind
        int anzahlFarbEichel = anzahlFarbKartenFinden(CardColor.EICHEL);

        // Anzahl Herz, die nicht Ober oder Unter sind
        int anzahlFarbHerz = anzahlFarbKartenFinden(CardColor.HERZ);

        // Anzahl Gras, die nicht Trumpf sind
        int anzahlFarbGras = anzahlFarbKartenFinden(CardColor.LAUB);

        // Anzahl Schelle, die nicht Trumpf sind
        int anzahlFarbSchelle = anzahlFarbKartenFinden(CardColor.SCHELLEN);

        // Anzahl Säue + Anzahl 10er
        int anzahlSchmier = 0;
        for( int i = 0; i <= 7; i++ ) {
            if(
                    hand.get(i).getRank() == CardRank.ASS ||
                    hand.get(i).getRank() == CardRank.ZEHN
                    ) {
                anzahlSchmier++;
            }
        }


        Es tut mir so Leid, dass das so unüberisichtlich ist.
        Hier eine Zusammenfassung der Hilfsvariablen:

        anzahlTrumpf
        anzahlFarbSau
        anzahlSau
        anzahlOber
        anzahlUnter
        anzahlBrems
        anzahlFarbEichel
        anzahlFarbHerz
        anzahlFarbGras
        anzahlFarbSchelle
        anzahlSchmier




        // Schritt 1:   Eichelsolo

        // Mit 8 Trumpfkarten:
        // Fast immer, eine Trumpfschmier oder eine hohe Karte (Eichel Ober) sollte man selber haben
        if(
                anzahlOber + anzahlUnter + anzahlFarbEichel == 8 &&
                anzahlBrems >= 1 &&
                anzahlSchmier >= 1
                ) {
            return "SOLOEICHEL";
        }
        // Mit 7 Trumpfkarten:
        // Mit einem Spatz (niedrige Karte): Mindestens 2 Ober und 2 Unter
        if(
                anzahlOber + anzahlUnter + anzahlFarbEichel == 7 &&
                anzahlOber >= 2 &&
                anzahlOber + anzahlUnter >= 4
                ) {
            return "SOLOEICHEL";
        }
        // Mit einer Fehlsau: Zumindest eine Trumpfschmier
        if(
                anzahlOber + anzahlUnter + anzahlFarbEichel == 7 &&
                ( anzahlSau == 2 || ( anzahlSau == 1 && anzahlSchmier >= 2 ) )
                ) {
            return "SOLOEICHEL";
        }
        // Mit 6 Trumpfkarten:
        // 2 Laufende Ober oder 3 Ober und eine Sau
        if(
                anzahlOber + anzahlUnter + anzahlFarbEichel == 6 &&
                ( anzahlBrems >= 2 || ( anzahlOber >= 1 && anzahlSau >= 1 ) )
                ) {
            return "SOLOEICHEL";
        }
        // Mit 5 Trumpfkarten:
        // 3 Laufende Ober und 2 Sauen, 3. Farbe frei
        if(
                anzahlOber + anzahlUnter + anzahlFarbEichel == 5 &&
                anzahlBrems == 3 &&
                anzahlSau >= 2 &&
                (anzahlFarbGras == 0 || anzahlFarbSchelle == 0 || anzahlFarbHerz == 0)
                ) {
            return "SOLOEICHEL";
        }


        // Schritt 2:   Grassolo

        // Mit 8 Trumpfkarten:
        // Fast immer, eine Trumpfschmier oder eine hohe Karte (Eichel Ober) sollte man selber haben
        if(
                anzahlOber + anzahlUnter + anzahlFarbGras == 8 &&
                anzahlBrems >= 1 &&
                anzahlSchmier >= 1
                ) {
            return "SOLOGRAS";
        }
        // Mit 7 Trumpfkarten:
        // Mit einem Spatz (niedrige Karte): Mindestens 2 Ober und 2 Unter
        if(
                anzahlOber + anzahlUnter + anzahlFarbGras == 7 &&
                anzahlOber >= 2 &&
                anzahlOber + anzahlUnter >= 4
                ) {
            return "SOLOGRAS";
        }
        // Mit einer Fehlsau: Zumindest eine Trumpfschmier
        if(
                anzahlOber + anzahlUnter + anzahlFarbGras == 7 &&
                ( anzahlSau == 2 || ( anzahlSau == 1 && anzahlSchmier >= 2 ) )
                ) {
            return "SOLOGRAS";
        }
        // Mit 6 Trumpfkarten:
        // 2 Laufende Ober oder 3 Ober und eine Sau
        if(
                anzahlOber + anzahlUnter + anzahlFarbGras == 6 &&
                ( anzahlBrems >= 2 || ( anzahlOber >= 1 && anzahlSau >= 1 ) )
                ) {
            return "SOLOGRAS";
        }
        // Mit 5 Trumpfkarten:
        // 3 Laufende Ober und 2 Sauen, 3. Farbe frei
        if(
                anzahlOber + anzahlUnter + anzahlFarbGras == 5 &&
                anzahlBrems == 3 &&
                anzahlSau >= 2 &&
                (anzahlFarbEichel == 0 || anzahlFarbHerz == 0 || anzahlFarbSchelle == 0)
                ) {
            return "SOLOGRAS";
        }


        // Schritt 3:   Herzsolo

        // Mit 8 Trumpfkarten:
        // Fast immer, eine Trumpfschmier oder eine hohe Karte (Eichel Ober) sollte man selber haben
        if(
                anzahlOber + anzahlUnter + anzahlFarbHerz == 8 &&
                        anzahlBrems >= 1 &&
                        anzahlSchmier >= 1
                ) {
            return "SOLOHERZ";
        }
        // Mit 7 Trumpfkarten:
        // Mit einem Spatz (niedrige Karte): Mindestens 2 Ober und 2 Unter
        if(
                anzahlOber + anzahlUnter + anzahlFarbHerz == 7 &&
                        anzahlOber >= 2 &&
                        anzahlOber + anzahlUnter >= 4
                ) {
            return "SOLOHERZ";
        }
        // Mit einer Fehlsau: Zumindest eine Trumpfschmier
        if(
                anzahlOber + anzahlUnter + anzahlFarbHerz == 7 &&
                        ( anzahlSau == 2 || ( anzahlSau == 1 && anzahlSchmier >= 2 ) )
                ) {
            return "SOLOHERZ";
        }
        // Mit 6 Trumpfkarten:
        // 2 Laufende Ober oder 3 Ober und eine Sau
        if(
                anzahlOber + anzahlUnter + anzahlFarbHerz == 6 &&
                        ( anzahlBrems >= 2 || ( anzahlOber >= 1 && anzahlSau >= 1 ) )
                ) {
            return "SOLOHERZ";
        }
        // Mit 5 Trumpfkarten:
        // 3 Laufende Ober und 2 Sauen, 3. Farbe frei
        if(
                anzahlOber + anzahlUnter + anzahlFarbHerz == 5 &&
                        anzahlBrems == 3 &&
                        anzahlSau >= 2 &&
                        (anzahlFarbEichel == 0 || anzahlFarbGras == 0 || anzahlFarbSchelle == 0)
                ) {
            return "SOLOHERZ";
        }


        // Schritt 4:   Schellensolo

        // Mit 8 Trumpfkarten:
        // Fast immer, eine Trumpfschmier oder eine hohe Karte (Eichel Ober) sollte man selber haben
        if(
                anzahlOber + anzahlUnter + anzahlFarbSchelle == 8 &&
                anzahlBrems >= 1 &&
                anzahlSchmier >= 1
                ) {
            return "SOLOSCHELLEN";
        }
        // Mit 7 Trumpfkarten:
        // Mit einem Spatz (niedrige Karte): Mindestens 2 Ober und 2 Unter
        if(
                anzahlOber + anzahlUnter + anzahlFarbSchelle == 7 &&
                anzahlOber >= 2 &&
                anzahlOber + anzahlUnter >= 4
                ) {
            return "SOLOSCHELLEN";
        }
        // Mit einer Fehlsau: Zumindest eine Trumpfschmier
        if(
                anzahlOber + anzahlUnter + anzahlFarbSchelle == 7 &&
                ( anzahlSau == 2 || ( anzahlSau == 1 && anzahlSchmier >= 2 ) )
                ) {
            return "SOLOSCHELLEN";
        }
        // Mit 6 Trumpfkarten:
        // 2 Laufende Ober oder 3 Ober und eine Sau
        if(
                anzahlOber + anzahlUnter + anzahlFarbSchelle == 6 &&
                ( anzahlBrems >= 2 || ( anzahlOber >= 1 && anzahlSau >= 1 ) )
                ) {
            return "SOLOSCHELLEN";
        }
        // Mit 5 Trumpfkarten:
        // 3 Laufende Ober und 2 Sauen, 3. Farbe frei
        if(
                anzahlOber + anzahlUnter + anzahlFarbSchelle == 5 &&
                anzahlBrems == 3 &&
                anzahlSau >= 2 &&
                (anzahlFarbEichel == 0 || anzahlFarbHerz == 0 || anzahlFarbSchelle == 0)
                ) {
            return "SOLOSCHELLEN";
        }


        // Schritt 5: Wenz

        // Eichel Unter auf der Hand?
        boolean EichelUnter = false;
        for (int i = 0; i <= 7; i++) {
            if (
                    hand.get(i).getRank() == CardRank.UNTER &&
                    hand.get(i).getColor() == CardColor.EICHEL
                    ) {
                EichelUnter = true;
            }
        }

        // Einen regulären Wenz spielen wir bei 2 oder mehr Untern:
        if( anzahlUnter >= 3 || ( anzahlUnter == 2 && EichelUnter) ) {

            // Wir sortieren die Karten in folgende Unterlisten:
            LinkedList<Card> UnterListe = new LinkedList();
            LinkedList<Card> EichelListe = listenFuellen(CardColor.EICHEL);
            LinkedList<Card> GrasListe = listenFuellen(CardColor.LAUB);
            LinkedList<Card> HerzListe = listenFuellen(CardColor.HERZ);
            LinkedList<Card> SchelleListe = listenFuellen(CardColor.SCHELLEN);

            // Stiche, die einen abgehen werden
            int verlust = 0;

            if ( !EichelUnter) {
                verlust++;
            }

            for( Card karte :hand ) {
                if(karte.getRank() == CardRank.UNTER )
                {
                    UnterListe.add(karte);
                }
            }

            verlust = verlust + verlustpruefen(EichelListe);
            verlust = verlust + verlustpruefen(GrasListe);
            verlust = verlust + verlustpruefen(HerzListe);
            verlust = verlust + verlustpruefen(SchelleListe);



                kleine Karte heißt: König oder schwächer

                0 Karten:
                => 0 Stiche

                1 Karte:
                keine Sau => 1 Stich
                Sau => 0 Stiche

                2 Karten:
                2 kleine Karten => 2 Stiche
                10er ohne König => 2 Stiche
                10er mit König =>  1 Stich
                Sau ohne 10er => 1 Stich
                Sau mit 10er => 0 Stiche

                3 Karten:
                3 kleine Karten => 2 Stiche
                10er mit 2 kleinen Karten => 2 Stiche
                10er mit König und kleiner Karte => 1 Stich
                Sau mit 2 kleinen Karten => 1 Stich
                Sau, 10er und kleine Karte => 0 Stiche

                4 Karten:
                4 kleine Karten => 2 Stiche
                10er und 3 kleine Karten => 2 Stiche
                10er mit König und 2 kleine Karten => 1 Stich
                Sau und 3 kleine Karten => 1 Stich
                Sau, 10 und 2 kleine Karten => 0 Stiche

                5 Karten:
                ohne Sau und ohne 10er => 2 Stiche
                ohne Sau und mit 10er => 1 Stich
                mit Sau und ohne 10er => 1 Stich
                mit Sau und mit 10er => 0 Stiche

                6 Karten:
                HALLO ALEX D.: MIT 6 KARTEN KANN EINEM NICHT DIE 10 UND DAS ASS NICHT BESITZEN!!!!!!!!!!!!!!!!!!!!!!!!!!
                ohne Sau und ohne 10er => 2 Stiche
                ohne Sau und mit 10er => 1 Stich
                mit Sau => 0 Stiche




            if ( verlust <= 3 ) {
                return "WENZ";
            }

        }

        // Besondere Wenze

        if( anzahlUnter == 2 && !EichelUnter && anzahlSau >= 3) {
            return "WENZ";
        }

        if ((anzahlUnter == 1) && (EichelUnter) && (anzahlSau >= 3) && ((anzahlSchmier - anzahlSau) >= 2)){
            return "WENZ";
        }

        if ( anzahlUnter == 0 && anzahlSau == 4 && anzahlSchmier == 4) {
            return "WENZ";
        }


        //Was soll das hier? :D
        //Da passiert doch gar nichts.....Wird nicht mal was zurückgegeben
        //Ulli
        // Schritt 6: Sauspiel

        // Ist das Blatt gut genug?
        boolean Sauspiel = false;

        if( anzahlTrumpf >= 6 ) {
            Sauspiel = true;
        }
        if( anzahlTrumpf == 5 ) {

        }
        return null;
    }

    //Verlust an Stichen wird geprüft, die beim Wenz verloren gehen
    private int verlustpruefen(LinkedList<Card> c1)
    {
        //Wenn die LinkedList leer ist, dann geht kein Stich verloren, weil der Bot 0 Karten der Farbe auf der Hand hat
        if (c1.getFirst() == null) {
            return 0;
        }
        //Boolean Array zur Bestimmung, ob das Ass, die Zehn und Der König auf der Gand sind
        //c2[0] --> Ass
        //c2[1] --> Zehn
        //c2[2] --> König
        boolean[] c2 = new boolean[3];
        //LinkedList wird durchsucht, ob das Ass, die zehn und der König auf der Hand sind
        for(Card karte:c1)
        {
            //Durchsuchen
            int rank = rankFinden(karte);
            //True setzen
            c2[rank] = true;
        }

        //Durchlaufen, je nachdem wie viele Karten der Farbe der Bot hat
        switch(c1.size())
        {
            //Wenn er eine Karte davon auf der Hand hat
            case 1:
                //Wenn die Karte das Ass ist
                if (c2[0])
                {
                    //Dann gehen null Stiche weg
                    return 0;
                }
                else
                {
                    //Sonst geht 1 Stich weg
                    return 1;
                }
            //Wenn er 2 Karten davon auf der Hand hat
            case 2:
                //Wenn die Zehn vorhanden ist
                if(c2[1])
                {
                    //Und wenn der König vorhanden ist
                    if(c2[2])
                    {
                        //Dann geht ein Stich weg
                        return 1;
                    }
                    //Oder wenn und das Ass vorhanden ist
                    else if(c2[0])
                    {
                        //Geht kein Stich weg
                        return 0;
                    }
                }
                //Wenn die Zehn nicht vorhanden ist, sondern das Ass mit einer anderen Karte, aber das Ass vorhanden ist
                else if(c2[0])
                {
                    //Ass mit einer Karte, die nicht die 10 ist --> 1 Stich geht flöten
                    return 1;
                }
                else
                {
                    //in allen anderen Fällen --> 2 Stiche gehen flöten
                    return 2;
                }
            //Wenn er 3 Karten davon auf der Hand hat
            case 3:
                //Wenn das Ass vorhanden ist
                if(c2[0])
                {
                    //Und wenn die 10 vorhanden ist
                    if(c2[1])
                    {
                        //Dann geht kein Stich flöten
                        return 0;
                    }
                    //Wenn das Ass mit 2 kleinen Karten vorhanden ist
                    else if(!c2[2])
                    {
                        //Dann geht ein Stich flöten
                        return 1;
                    }
                }
                //Wenn die 10 vorhanden ist, aber das Ass nicht
                if(c2[1])
                {
                    //Und wenn der König vorhanden ist
                    if(c2[2])
                    {
                        //Dann geht ein Stich weg
                        return 1;
                    }
                }
                //Sonst
                else
                {
                    //Gehen 2 Stiche flöten
                    return 2;
                }
            //Wenn er 4 Karten auf der Hand hat
            case 4:
                //Wenn das Ass vorhanden ist
                if(c2[0])
                {
                    //Und wenn die 10 vorhanden ist
                    if(c2[1])
                    {
                        //Geht kein Stich flöten
                        return 0;
                    }
                    //Wenn das Ass mit 3 kleinen Karten vorhanden ist
                    else if(!c2[2])
                    {
                        //Dann geht ein Stich flöten
                        return 1;
                    }
                }
                //Wenn die 10 vorhanden ist, aber nicht das Ass
                else if(c2[1])
                {
                    //Und wenn der König vorhanden ist
                    if(c2[2])
                    {
                        //Dann geht ein Stich flöten
                        return 1;
                    }
                }
                //Sonst
                else
                    {
                    //Gehen 2 Stiche flöten
                    return 2;
                }
            //Wenn er 5 Karten davon auf der Hand hat
            case 5:
                //Wenn das Ass vorhanden ist
                if(c2[0])
                {
                    //Und wenn die 10 vorhanden ist
                    if(c2[1])
                    {
                        //Dann geht kein Stich flöten
                        return 0;
                    }
                    //Ass ohne 10
                    else
                    {
                        //Dann geht ein Stich flöten
                        return 1;
                    }
                }
                //Wenn das Ass nicht vorhanden ist, aber die 10 vorhanden ist
                else if(c2[1])
                {
                    //Dann geht ein Stich flöten
                    return 1;
                }
                //Wenn weder ASs noch 10 vorhanden sind
                else
                {
                    //Dann gehen 2 Stiche flöten
                    return 2;
                }
            //Wenn er 6 Karten davon auf der Hand hat
            case 6:
                //Wenn das Ass vorhanden ist
                if(c2[0])
                {
                    //Gehen kein Stich flöten
                    return 0;
                }
                //Wenn das Ass nicht vorhanden ist
                else
                {
                    //Geht ein Stich flöten
                    return 1;
                }
            //Default muss gesetzt werden, weil sonst die Methode nicht funktioniert
            default:
                return 123456789;
        }

    }

    //Karte wird geprüft, ob sie ein Ass, König oder Zehn ist --> Sonst automatisch König
    private int rankFinden(Card c1)
    {
        switch (c1.getRank()) {
            case ASS:
                return 0;
            case ZEHN:
                return 1;
            case KOENIG:
                return 2;
            default:
                return 2;
        }
    }

    //Methode zum Finden von Farbkarten
    private int anzahlFarbKartenFinden(CardColor c1)
    {
        //Anzahl der Farbkarten auf der Hand
        int anzahlFarbKarte = 0;
        //Hand wird durchlaufen
        for( int i = 0; i <= 7; i++ ) {
            //Wenn die Karte eine Farbkarte ist, dann wird die Anzahl der Farbkarten um eins erhöht
            if(hand.get(i).getColor() == c1 && hand.get(i).getRank() != CardRank.OBER && hand.get(i).getRank() != CardRank.UNTER)
            {
                anzahlFarbKarte++;
            }
        }
        //Rückgabe von anzahlFarbKarte
        return anzahlFarbKarte;
    }

    //Methode zum Füllen einer Liste mit einer bestimmen Art einer Karte
    //hand wird als c1 übergeben. Könnte man theoretisch auch über einen Typecast machen, sodass hier eine weitere Liste als Hand fungiert. Man kann nicht direkt hand nehmen, da diese sonst über removeFirst() geleert wird
    private LinkedList<Card> listenFuellen(CardColor c2)
    {
        //Liste, die am Ende zurückgegeben wird
        LinkedList<Card> templist = new LinkedList<>();
        //Hand wird durchlaufen
        for(Card karte: hand) {
            //Wenn die Karte der zu entsprechenden Art übereinstimmt, wird sie zur templist hinzugefügt
            if( karte.getColor() == c2 ) {
                templist.add(karte);
            }
        }
        //templist wird zurückgegeben
        return templist;
    }

    //Methode zum Berechnen der Anzahl von bestimmten Karten aufgrund eines CardRanks
    private int anzahlKarten(CardRank c1)
    {
        //Int, der am Ende zurückgegeben wird
        int tempint = 0;
        //Hand wird durchlaufen
        for( int i = 0; i <= 7; i++ ) {
            //Wenn eine Karte mit dem bestimmten Rank auf der Hand vorhanden ist, dann wird empint um eins erhöht
            if(hand.get(i).getRank() == c1) {
                tempint++;
            }
        }
        //tempint wird zurückgegeben
        return tempint;
    }
}
*/