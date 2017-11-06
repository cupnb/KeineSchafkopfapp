package de.emghaar.game;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import de.emghaar.game.card.Card;
import de.emghaar.game.card.CardColor;
import de.emghaar.game.card.CardRank;

import static de.emghaar.game.Mode.MODE_TYPE.NICHTS;
import static de.emghaar.game.Mode.MODE_TYPE.RAMSCH;

/**
 * Klasse, die die komplette Spiellogik beinhaltet.
 *
 * Hier wird alles, was man braucht initialisiert und erstellt. Die Klasse wird in der Main Klasse aufgerufen
 *
 * @author Alex Ullrich, Noah Boeckmann, Lukas Dröse
 */
//TODO Zum Laufen bringen
//Hauptklasse, die alles steuert, und ueber die das Game hauptsaechlich laeuft --> Vergleiche typisches Schafkopfspiel
public class Game {

    //Feld fuer die Spieler; 4 Spieler werden dem Feld im Konstruktor zugewiesen
    private Player[] players;
    //Modus des SPiels (Bsp.: Herzsolo)
    private Mode mode;
    //Beinhaltet 32 Spielkarten
    private Deck deck;
    //Stack, der alle Karten aufnimmt   --> wird zum Mischen in der naechsten Runde verwendet
    private Stack<Card> dump;
    //Ruffarbe des Modes (bei Soli, Ramsch oder Wenz --> UNDEFINED)
    private CardColor callingColor;
    //Rundenummer
    private int roundnumber;
    //Anzahl der Spieler, die selbst spielen wollen
    //private int anzahlSpielenWollen;

    /**
     * Konstruktor der Klasse Game
     *
     * - Initialisierung des Arrays für die Spieler
     * - Mode wird auf NICHTS gesetzt
     * - Spielern wird das Game zugesetzt
     * - alle notwendigen weiteren Variablen werden initialisiert
     *
     * @author Alex Ullrich
     *
     * @param p0 Erster Spieler des Spiels. Er ist in der ersten Runde der "Ausgeber" der Karten
     * @param p1 Zweiter Spieler des Spiels. Er ist in der ersten Runde der Spieler, der zuerst gefragt wird, ob er spielen möchte
     * @param p2 Dritter Spieler des Spiels
     * @param p3 Vierter Spieler des Spiels
     */
    public Game(Human p0, Human p1, Human p2, Human p3) {
        //Initialisierung des Feld Players (siehe Attribute)
        players = new Player[4];
        //Mode wird zurueckgesetzt
        mode = new Mode(NICHTS);

        //Referenz von Game wird den Spielern uebergeben
        p0.setGame(this);
        p1.setGame(this);
        p2.setGame(this);
        p3.setGame(this);

        // /Spieler werden in das Feld gesteckt
        players[0] = p0;
        players[1] = p1;
        players[2] = p2;
        players[3] = p3;

        //Initialisierung von Stacks und dem Deck
        deck = new Deck();
        dump = new Stack<>();
        callingColor = CardColor.UNDEFINED;
        roundnumber = 0;
        //anzahlSpielenWollen = 0;
        System.out.println("Konstruktor fertig abgeschlossen");
        initialize();
    }

    Mode getMode()
    {
        return mode;
    }

    /**
     * Methode, die immer am Anfang eines Games aufgerufen wird
     *
     * - Wichtige Variablen werden auf ihren Standardwert zurückgesetzt
     * - Karten werden an die Spieler ausgegeben
     * - Auswahlverfahren für den Spielmodus des jeweiligen Spiels
     * - bei Sauspielen: Mitspieler des Spielers wird gesucht
     * - Comparisonwerte für Karten werden angepasst
     * - Methode loop() wird aufgerufen
     * - dealer wird um 1 erhoeht für die naechste Runde
     * - Methode ende() wird aufgerufen
     *
     * @author Noch Boeckmann, Alex Ullrich
     */
    private void initialize() {
        System.out.println("Methode initialize aufgerufen");
        //Ruffarbe wird zurueckgesetzt
        setCallingColor(CardColor.UNDEFINED);
        //Roundnumber wird um eins erhöht
        roundnumber++;
        //Ruffarbe wird auf den Standard gesetzt
        mode.setTrumpfcolor(-1);
        //Mode wird zurueckgesetzt
        mode.setModeType(NICHTS);
        //anzahlSpielenWollen wird zurueckgesetzt
        //anzahlSpielenWollen = 0;
        //Deck wird gemischt
        if (roundnumber != 1) {
            deck = new Deck();
        }
        //Dump wird geleert am Anfang der Runde
            dump.clear();
        //Karten werden zu je 4 an die Spieler verteilt
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                players[j].addCards(deck.deal());
            }
        }
        System.out.println(" ----- Die Karten der Spieler werden ausgegeben -----");
        for (int l = 0; l < 4; l++) {
            for (Card karte : players[l].getHand()) {
                System.out.println("Spieler " + players[l].getName() + " : " + karte.getColor() + " mit Wert " + karte.getRank());
                //Gibt Karten aus
            }
            System.out.println("");
        }

        //Spielern wird ihre Spielnummer uebergeben
        for (int o = 0; o < 4; o++) {
            players[o].giveNumber(o);
        }

            //Mode wird an die Spieler bzw. Bots uebergeben
            for (int b = 0; b < 4; b++) {
                players[b].giveMode(mode.getModeType());
            }

            //Aufruf der Methode loop fuehrt 8 Stiche durch
            for (int i = 0; i < 8; i++) {
                loop();
            }

            //Geber wird um eins erhoeht (ganz am Ende von initialize einbauen)
            if (dealer == 3) {
                dealer = 0;
            } else {
                dealer = dealer++;
            }

            //Auszaehlen der Punkte und Bekanntmachung des Gewinners
            ende();
        }
    }

    /**
     * Methode, die einen Stich durchspielt
     *
     * - Standardwerte für Variablen werden gesetzt
     * - Spieler werden nach dem Legen einer Karte gefragt
     * - Matrix für den Bot wird befuellt und uebergeben
     * - addgespieleteKarte() wird aufgerufen --> Karte wird zum Stich hinzugefuegt
     * - Comparisonwerte werden verglichen um die hoechste Karte im Stich zu finden
     * - Spieler, der den Stich bekommt wird er zugeordnet
     *
     *  @author Noah Boeckmann, Alex Ullrich
     */
    //Methode fuer einen Stich
    private void loop() {
        //Stack mit dem Karten des letzten Stichs werden geleert
        played.clear();
        //Die Anzahl der gespielten Stiche wird um 1 erhöht
        playedStiche++;
        //Spieler, der die höchste Karte gelegt hat
        Player best = null;
        //höchste Karte, die gerade im Stich liegt
        Card highest = null;
        //for: 4 Spieler legen Karte
        for (int x = 0; x < 4; x++) {
            //players[turnState.ordinal()].yourTurn();
            Card Spielkarte = players[(dealer + 1 + x) % 4].kartelegen();
            System.out.println(Spielkarte.getColor().getName() + " Wert: " + Spielkarte.getRank().getName());
            //matrix wird mit der jeweiligen Karte befuellt
            matrix[(dealer + 1 + x) % 4][playedStiche - 1] = Spielkarte;
            //uebergeben der aktualisierten Matrix an die Player, wenn der Player ein Bot ist
            for (int u = 0; u < 4; u++) {
                if (players[u].isBot()) {
                    players[u].setMatrix(matrix);
                }
            }
            //Karte wird zu Dump und Played hinzugefuegt
            //TODO addgespielteKarte wird doppelt eingebaut (auch Humane Methode Kartelegen) (habs mal auskommentiert. Sollte also passen. Bei Problemen (warum eigentlich?) in player wieder einfuegen -NB)
            addgespielteKarte(Spielkarte);
            //erste Karte wird hingelegt
            if (best == null) {
                //bester Spieler wird auf den Anfangsspieler gesetzt
                best = players[(dealer + 1 + x) % 4];
                //Karte wird als höchste bezeichnet
                highest = Spielkarte;
            } else {
                //Vergleich der Spielkarten aufgrund der Farbe --> Farbe mit der höheren Zahl bei gleicher Farbe wird highest
                if (Spielkarte.getColor() == highest.getColor()) {
                    if (Spielkarte.getRank().getComparison() > highest.getRank().getComparison()) {
                        highest = Spielkarte;
                        best = players[(dealer + 1 + x) % 4];
                    }
                }
                //Wenn es nicht die gleiche Farbe ist, bleibt die liegende Karte die höchste, außer der Vergleichswert ist größer als 60 (Vergleichswerte höher 60 --> Trumpf)
                else {
                    if (Spielkarte.getRank().getComparison() < 60) {
                        highest = Spielkarte;
                        best = players[(dealer + 1 + x) % 4];
                    }
                }
            }
        }
        //Stich wird dem Winner des Stichs zugeordnet
        best.addStich(played);
        //Stichpunktanzahl des Winners wird erhöht --> Dient zur Auszaehlung am Ende (siehe Methode getPunkte() in Player)
        best.stichpunkterhoehen();

    }

    /**
     * Methode, die verwendet wird um eine bestimmte Karte in einer Hand zu finden
     *
     * Benutzung: Mitspieler bei Sauspiel wird ermittelt
     *
     * @author Alex Ullrich
     *
     * @param c1 Hand des ersten Spielers
     * @param c2 Hand des zweiten Spielers
     * @param c3 Hand des dritten Spielers
     * @param c4 Hand des vierten Spielers
     * @param gesuchtRank Rank der gesuchten Karte
     * @param gesuchtColor Farbe der gesuchten Karte
     *
     * @return Zahl des Spielers, der die Karte besitzt wird zurueckgegeben
     */
    //Suche nach einer bestimmten Karte aufgrund von 4 Listen und dem/der CardRank/CardColor
    private int sucheKarte(LinkedList<Card> c1, LinkedList<Card> c2, LinkedList<Card> c3, LinkedList<Card> c4, CardRank gesuchtRank, CardColor gesuchtColor) {

        //1. Liste wird durchsucht
        for (Card karte:c1) {
            if (karte.getColor() == gesuchtColor && karte.getRank() == gesuchtRank) {
                return 0;
            }
        }

        //2. Liste wird durchsucht
        for (Card karte:c2) {
            if (karte.getColor() == gesuchtColor && karte.getRank() == gesuchtRank) {
                return 1;
            }
        }

        //3. Liste wird durchsucht
        for (Card karte:c3) {
            if (karte.getColor() == gesuchtColor && karte.getRank() == gesuchtRank) {
                return 2;
            }
        }

        //4. Liste wird durchsucht
        for (Card karte:c4) {
            if (karte.getColor() == gesuchtColor && karte.getRank() == gesuchtRank) {
                return 3;
            }
        }

        return -1;
    }

    /**
     * Getter Methode von callingColor
     *
     * @author Alex Ullrich
     *
     * @return Zahl der Spielfarbe
     */
    //getter Methode
    CardColor getCallingColor() {
        return callingColor;
    }

    /**
     * Setter Methode von callingColor
     *
     * @author Alex Ullrich
     *
     * @param cardColor Wert, der als callingColor gesetzt werden soll
     */
    //setter Methode
    private void setCallingColor(CardColor cardColor) {
        callingColor = cardColor;
    }

    /**
     * Methode, die am Ende eines Spiels aufgerufen wird
     *
     * - Initialisierung der noetigen Variablen
     * - Unterscheidung zwischen Ramsch und aneren Spielmodi
     * - Punkte der Spieler werden ausgezaehlt
     * - Gewinner wird bekannt gegeben
     *
     * @author Lukas Dröse, Alex Ullrich
     */
    //Methode, die am Ende aufgerufen wird
    private void ende() {
        //Punkte der Spieler
        int punktePlayer = 0;
        //Punkte der Nichtspieler
        int punkteNotPlayer = 0;
        //Feststellen des Verlierers aufgrund der Punktanzahl
        if (mode.getModeType() == RAMSCH) {
            //int zum Speichern des Spielers mit der höchsten Punktezahl
            int lost = 5;
            //int zum Speichern des Punktestands dieses Spielers
            int most = 0;
            //4 Spieler werden durchgegangen
            for (int i = 0; i < 4; i++) {
                //Wenn der naechste Spieler mehr Punkte hat, wird er gespeichert
                if (players[i].getPunkte() > most) {
                    //Festelegung der Punkte zum weiteren Vergleich
                    most = players[i].getPunkte();
                    //Festlegung des Spielers zum spaeteren Aufrufen
                    lost = i;
                }
            }
            //Bekanntmachung des Verlierers
            System.out.println(players[lost].getName() + " hat verloren!");
        } else {
            //Spieler werden festgelegt in einem Feld
            Player[] playerspp = new Player[2];
            //Nicht-Spieler werden festgelegt in einem Feld
            Player[] notplayerspp = new Player[3];
            //Durchlaufen der 4 Spieler
            for (int b = 0; b < 4; b++) {
                //Speichern der Punkte der Spieler
                if (players[b].getPlayer()) {
                    playerspp[b % 2] = players[b];
                    punktePlayer = punktePlayer + players[b].getPunkte();
                }
                //Speichern der Punkte der Nicht-Spieler
                else {
                    notplayerspp[b % 3] = players[b];
                    punkteNotPlayer = punkteNotPlayer + players[b].getPunkte();
                }
            }

            //Auswertung der Punktzahl
            if (mode.getModeType() != Mode.MODE_TYPE.SOLOEICHEL || mode.getModeType() != Mode.MODE_TYPE.SOLOGRAS || mode.getModeType() != Mode.MODE_TYPE.SOLOSCHELLEN)
            {
                if (punktePlayer <= punkteNotPlayer) {
                    //Bekanntmachung, dass die NIcht-Spieler gewonnen haben
                    System.out.println(notplayerspp[0].getName() + " und " + notplayerspp[1].getName() + " haben gewonnen!");
                } else {
                    //Bekanntmachung, dass die Spieler gewonnen haben
                    System.out.println(playerspp[0].getName() + " und " + playerspp[1] + " haben gewonnen!");
                }

            }
            else
            {
                if (punktePlayer <= punkteNotPlayer) {
                    //Bekanntmachung, dass die NIcht-Spieler gewonnen haben
                    System.out.println(notplayerspp[0].getName() + " und " + notplayerspp[1] +" und " + notplayerspp[2].getName() + " haben gewonnen!");
                } else {
                    //Bekanntmachung, dass der Spieler gewonnen haben
                    System.out.println(playerspp[0].getName() +  " hat gewonnen!");
                }
            }
        }
    }

    /**
     * Methode, die aufgerufen wird in initialize() und schaut, ob ein Spieler spielen moechte oder nicht
     *
     * - nötige Variablen werden initialisiert
     * - Spieler werden durch setWannaplay() nacheinander gefragt
     * - wenn x!=4, dann werden alle Spieler bis auf Spieler x gefragt (Spieler x hat bei einer vorherigen Frage einen Fehler beim Auswaehlen gemacht)
     *
     * @author Noah Boeckmann, Alex Ullrich
     *
     * @param x Zahl eines Spielers, der davor einen fehler gemacht hat --> wenn 4, dann hat keiner einen Fehler gemacht
     * @return Array, der entweder true für spielen bzw. false für nichtspielen beinhaltet
     */
    private boolean[] spielenWill(int x)
    {
        //int x zum Abfragen, ob ein Spieler einen Fehler beim Eingeben eines Spielmodus gemacht hat
        //wenn ja --> darf nicht mehr an der naechsten Fragerunde teilnehmen
        //Standardwert, wenn noch keine Frageunde vorbei ist = 4
        //int zum "Anfaenger" der "Fragerunde"
        System.out.println("Methode spielenWill aufgerufen");
        int auswaehler = (dealer + 1);
        //Anzahl der Leute, die spielen wollen
        //boolean Feld zur Bestimmung, wer spielen will und wer davon spielt
        boolean[] willSpieler = new boolean[4];

        //Erste Runde fuer Standardwert = 4
        if(x == 4) {
            //for: Abfrage wer spielen will --> True setzen des jeweiligen Indexes
            for (int i = 0; i < 4; i++) {
                if (players[(auswaehler + i) % 4].setWannaplay() != 0) {
                    willSpieler[(auswaehler + i) % 4] = true;
                    //Anzahl der Personen, die spielen wollen wird um 1 erhöht
                    anzahlSpielenWollen = anzahlSpielenWollen + 1;
                    System.out.println("spielenWill TRUE");
                } else {
                    willSpieler[(auswaehler + i) % 4] = false;
                    System.out.println("spielenWill FALSE");
                }

            }
        }
        //Else fuer die zweiten Fragerunden
        else
        {
            //Alle vier Spieler werden abgefragt, ob sie spielen wollen mithilfe von for
            for (int i = 0; i < 4; i++)
            {
                //Wenn der Spieler keine Fehler gemacht hat beim Auswaehlen des Spiels, wird er ganz normal gefragt
                //Sonst passiert gar nichts
                if (i != (x-1))
                {
                    if (players[(auswaehler + i) % 4].setWannaplay() != 0) {
                        willSpieler[i] = true;
                        //Anzahl der Personen, die spielen wollen wird um 1 erhöht
                        anzahlSpielenWollen = anzahlSpielenWollen + 1;
                        System.out.println("spielenWill TRUE");
                    } else {
                        willSpieler[i] = false;
                        System.out.println("spielenWill FALSE");
                    }
                }
            }
        }
        System.out.println("spielenWill abgeschlossen");
        return willSpieler;
    }
}