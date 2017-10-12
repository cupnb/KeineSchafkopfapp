package de.emghaar.game;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import de.emghaar.game.card.Card;
import de.emghaar.game.card.CardColor;
import de.emghaar.game.card.CardRank;

import static de.emghaar.game.Mode.MODE_TYPE.NICHTS;
import static de.emghaar.game.Mode.MODE_TYPE.RAMSCH;
//TODO Zum Laufen bringen
//Hauptklasse, die alles steuert, und ueber die das Game hauptsaechlich laeuft --> Vergleiche typisches Schafkopfspiel
public class Game {

    //Feld fuer die Spieler; 4 Spieler werden dem Feld im Konstruktor zugewiesen
    private Player[] players;

    //Person, die imaginaer die Karten austeilt; wichtig fuer Ansage wer spielt und wer rauskommt
    private int dealer;
    //Stack fuer die Karten, die gerade im Stich gespielt wurden
    private Stack<Card> played;
    //Modus des SPiels (Bsp.: Herzsolo)
    private Mode mode;
    //Beinhaltet 32 Spielkarten
    private Deck deck;
    //Stack, der alle Karten aufnimmt   --> wird zum Mischen in der naechsten Runde verwendet
    private Stack<Card> dump;
    //Matrix zum Speichern der Karten --> Nötig fuer den Bot
    private Card[][] matrix;
    //Anzahl der gespielten Stiche
    private int playedStiche;
    //Ruffarbe des Modes (bei Soli, Ramsch oder Wenz --> null)
    //1 = Schellen
    //2 = Laub
    //3 = Eichel
    //-1 = keine
    private int callingColor;
    //Rundenummer
    private int roundnumber;
    //Anzahl der SPieler, die selbst spielen wollen
    private int anzahlSpielenWollen;

    public Game(Human p0, Human p1, Human p2, Human p3) {
        //Random Zahl zur Bestimmung des Dealers in der ersten Runde
        Random rnd = new Random();
        //Initialisierung des Feld Players (siehe Attribute)
        players = new Player[4];
        //Mode wird zurueckgesetzt
        mode = new Mode(NICHTS);

        //irgendein Graphikzeugs
        //InputStreamReader Alpha = new InputStreamReader(System.in);
        //BufferedReader Eingabe = new BufferedReader(Alpha);

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
        played = new Stack<>();

        //Der Geber wird zufaellig bestimmt
        dealer = rnd.nextInt(4);
        matrix = new Card[4][8];
        playedStiche = 0;
        callingColor = -1;
        roundnumber = 0;
        anzahlSpielenWollen = 0;
        System.out.println("Konstruktor fertig abgeschlossen");
        initialize();
    }


    Mode getMode()
    {
        return mode;
    }

    //Fuegt eine gespielte Karte zu dump und played hinzu
    void addgespielteKarte(Card f) {
        dump.add(f);
        played.add(f);
    }

    Stack getDump()
    {
        return dump;
    }

    private void initialize() {
        System.out.println("Methode initialize aufgerufen");
        //Ruffarbe wird zurueckgesetzt
        setCallingColor(-1);
        //Roundnumber wird um eins erhöht
        roundnumber++;
        //Ruffarbe wird auf den Standard gesetzt
        mode.setTrumpfcolor(2);
        //Mode wird zurueckgesetzt
        mode.setModeType(NICHTS);
        //PlayedStiche wird reseted
        playedStiche = 0;
        //anzahlSpielenWollen wird zurueckgesetzt
        anzahlSpielenWollen = 0;
        //Deck wird gemischt
        if (roundnumber != 1) {
            deck.initialize(dump);
        }
        //Dump wird geleert am Anfang der Runde
        else {
            dump.clear();
        }
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

        for (int o = 0; o < 4; o++) {
            players[o].giveNumber(o);
        }

        System.out.println("Auswahlverfahren fuer den Mode gestartet");
        boolean[] willSpieler;
        willSpieler = spielenWill(4);
        //Abfrage wer SPIELT
        //Array Mode zur Auswahl des Mode's durch Ausnutzen (der Arme D:)des Enums
        Mode.MODE_TYPE[] modefeld = new Mode.MODE_TYPE[4];
        //int zur Festlegung des endgueltigen Spielers --> Festlegen von Spieler und Nicht-Spieler
        int endgueltigerPlayer = -1;
        //Wenn niemand spielen will --> Ramsch
        //Wenn genau eine Person spielen will, dann wird der gewuenschte Mode der Person genommen
        if (anzahlSpielenWollen == 1) {
            System.out.println("1 Spieler will spielen. Sein Spielmodus wird uebernommen");
            for (int p = 0; p < 4; p++) {
                if (willSpieler[p]) {
                    mode.setModeType(players[p].play());
                    //es wird nachgeprueft, ob der Spieler mit seiner Hand spielen darf --> Sauspiel darf nur ohne die Rufass gespielt werden
                    if (mode.SauSpielSpielbar(players[p].getHand(), mode)) {
                        endgueltigerPlayer = p;
                    }
                    //Sonst wird nochmal abgefragt, wer spielen will und diese Prozedere von vorne angefangen (p ist der Spieler, der es nicht hinbekommen hat einen richtigen Mode zu waehlen)
                    else {
                        mode.setModeType(NICHTS);
                        willSpieler = spielenWill(p);
                    }
                }
            }
        }
        //Wenn mehr als eine Person spielen will, wird aufgrund der Ordinalzahl des Modes abgewaegt, was gespielt wird
        if (anzahlSpielenWollen > 1) {
            System.out.println("2-4 Spieler wollen spielen. Sein Spielmodus wird uebernommen");
            //Modes der Spieler, die spielen wollen werden aufgenommen in das Mode Array
            for (int i = 0; i < 4; i++) {
                if (willSpieler[i]) {
                    modefeld[i] = players[(dealer + 1 + i) % 4].play();
                } else {
                    modefeld[i] = NICHTS;
                }
            }

            //vergleicht ob jemand der spaeter spielen will einen höher priorisierten Mode spielen will (ueber die Ordinalzahl)
            for (int z = 0; z < 4; z++) {
                if (modefeld[z] != null) {
                    //erster Mode wird gesetzt
                    if (mode.getModeType() == NICHTS) {
                        mode.setModeType(modefeld[z]);
                        endgueltigerPlayer = z;
                    }
                    //Vergleich der Modes aufgrund der Ordinalzahl im Enum
                    if (modefeld[z].getOrdinal(modefeld[z].toString()) > mode.getModeType().getOrdinal(mode.toString())) {
                        mode.setModeType(modefeld[z]);
                        endgueltigerPlayer = z;
                    }

                }
            }
            //Mode fuerSpiel ist der endgueltige Mode
        }

        if (anzahlSpielenWollen == 0) {
            System.out.println("0 Spieler wollen spielen. Ramsch wird ausgewaehlt");
            mode.setModeType(RAMSCH);
        }

        if (endgueltigerPlayer == 0 || endgueltigerPlayer == 1 || endgueltigerPlayer == 2 || endgueltigerPlayer == 3) {
            //Spieler wird gesetzt
            players[endgueltigerPlayer].setPlayer(true);
            System.out.println("Spieler " + players[endgueltigerPlayer].getName() + " spielt");
        }

        {
            players[0].giveSpielender(endgueltigerPlayer);

            //Hilfsvariable
            int now;
            //Mitspieler wird gesucht
            switch (mode.getModeType()) {
                case SAUSPIELEICHEL:
                    now = sucheKarte(players[0].getHand(), players[1].getHand(), players[2].getHand(), players[3].getHand(), CardRank.ASS, CardColor.EICHEL);
                    players[now].setPlayer(true);
                    setCallingColor(3);
                    System.out.println("Zusammen mit " + players[now].getName());
                    break;
                case SAUSPIELSCHELLEN:
                    now = sucheKarte(players[0].getHand(), players[1].getHand(), players[2].getHand(), players[3].getHand(), CardRank.ASS, CardColor.SCHELLEN);
                    players[now].setPlayer(true);
                    setCallingColor(1);
                    System.out.println("Zusammen mit " + players[now].getName());
                    break;
                case SAUSPIELGRAS:
                    now = sucheKarte(players[0].getHand(), players[1].getHand(), players[2].getHand(), players[3].getHand(), CardRank.ASS, CardColor.LAUB);
                    players[now].setPlayer(true);
                    setCallingColor(2);
                    System.out.println("Zusammen mit " + players[now].getName());
                    break;
                //Kontrollfunktion, falls der Mode nicht ausgewaehlt werden konnte
                case NICHTS:
                    mode.setModeType(RAMSCH);
                    System.err.println("Kein Spielmodus ausgwaehlt. Ramsch wurde als Spielmodus gesetzt");
            }

            //Trumpfcolor wird aufgrund des Modes ferstgelegt

            switch (mode.getModeType()) {
                case SOLOGRAS:
                    mode.setTrumpfcolor(3);
                case SOLOEICHEL:
                    mode.setTrumpfcolor(4);
                case SOLOSCHELLEN:
                    mode.setTrumpfcolor(1);
                case WENZ:
                    mode.setTrumpfcolor(0);
            }

            //Vergleichswerte fuer Ober und Unter werden angepasst (z.B. Eichel Ober ist ueber Schellen Ober)
            for (int a = 0; a < 4; a++) {
                mode.comparisonOberUnter(players[a].getHand());
            }

            //Vergleichswerte der Trumpfarbe werden angepasst (solange es nicht Herz ist) bzw. die Vergleichswerte werden fuer einen Wenz angepasst
            if (mode.getTrumpfcolor() == 0 || mode.getTrumpfcolor() == 1 || mode.getTrumpfcolor() == 3 || mode.getTrumpfcolor() == 4) {

                for (int b = 0; b < 4; b++) {
                    mode.comparisonAktualisieren(players[b].getHand(), mode.getModeType());
                }
            }

            //Vergleichswerte von Herz werden erhöht, wenn Herz Trumpffarbe ist
            if (mode.getTrumpfcolor() == 2) {
                for (int s = 0; s < 4; s++) {
                    mode.comparisonSetStandard(players[s].getHand());
                }

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

    //getter Methode
    int getCallingColor() {
        return callingColor;
    }

    //setter Methode
    private void setCallingColor(int x) {
        callingColor = x;
    }

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
                if (i != x)
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