package de.emghaar.game;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Scanner;
import de.emghaar.game.card.Card;

/**
 * Klasse, die den Spieler eines Games beschreibt
 *
 * Alles, was mit dem Spieler in einem Schafkopfspiel zu tun hat wird hier erledigt
 *
 * @author Noah Boeckmann, Lukas Droese, Alex Ullrich
 */
//Klasse, die alle Methoden um den Spieler beinhaltet
public class Human implements Player
{
    //"Hand" des Spielers --> Speichern aller 8 Karten in einer Liste
    private LinkedList<Card> hand;
    //Name des Spielers
    private String name;
    //Spiel, dem der Spieler beiwohnt
    private Game game;
    //Punkte, die der Spieler besitzt
    private int points;
    //Boolean zum Festlegen von Spieler bzw. Nicht-Spieler
    private boolean player;
    //Boolean zum Festlegen, ob der Spieler spielen will, oder nicht
    private boolean wannaplay;
    //Boolean zum festlegen, ob der Spieler online spielt, oder nicht
    private boolean online;
    //Speichern der gewonnenen Stiche des Spielers
    private Stack<Card> stiche;
    //Anzahl der Stiche zur Auszählung
    private int stichanzahl;

    /**
     * Konstruktor der Klasse Human
     *
     * Alle wichtigen Variablen werden initialisiert
     *
     * @author Lukas Droese, Alex Ullrich
     * @param name Name des jeweiligen Spielers
     */
    //Konstruktor der Klasse Player
    public Human(String name)
    {
        this.name = name;
        wannaplay = false;
        game = null;
        points = 0;
        player = false;
        hand = new LinkedList<>();
        online = false;
        stiche = new Stack<>();
        stichanzahl = 0;
        boolean bot = false;
    }

    /**
     * Setter Methode von Game
     *
     * @author Alex Ullrich
     * @param g Game, in dem der Spieler gerade ist
     */
    //setter Methode von game
    public void setGame(Game g)
    {
        game = g;
    }

    /**
     * Setter Methode von Player
     *
     * @author Alex Ullrich
     * @param p Boolean, ob der Human Spieler bzw. Nicht-Spieler im Game ist
     */
    //setter Methode von player
    public void setPlayer(boolean p)
    {
        player = p;
    }
    // -#-#-#-#-#-#- Frage -#-#-#-#-#-#-
    // Was ist das?
    // - Alex D.
    // -#-#-#-#-#-# Antwort #-#-#-#-#-#-
    // Damit wird festgelegt, wer mit wem in einem Team ist.
    // Wenn du beispielsweise jetzt auf das Eichelass spielst,
    // wird bei dir und bei der Person, die das Eichelass hat Player gesettet.
    // - Ulli
    // -#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-

    /**
     * Getter Methode von Player
     *
     * @author Alex Ullrich
     * @return boolean, ober der Human spielt oder nicht
     */
    //getter Methode von player
    public boolean getPlayer()
    {
        return player;
    }

    /**
     * Methode, die die Stichpunktanzahl um eins erhoeht
     *
     * @author Lukas Droese
     */
    //Methode, die die Stichpunktanzahl des Players um 1 erhöht
    public void stichpunkterhoehen(){
        stichanzahl = stichanzahl + 1;
    }

    /**
     * Methode, die die Anzahl der Punkte eines Human übergibt
     *
     * @author Lukas Droese, Alex Ullrich
     * @return Anzahl der Punkte, die der Spieler besitzt
     */
    //Gibt die Punkte des Spielers wieder
    public int getPunkte(){

        for (int y = stichanzahl * 4; y > 0; y--){
            points = points + stiche.pop().getPoints();
        }
        return points;
    }

    /**
     * Methode uebergibt dem Human einen Stich, den er gewonnen hat
     *
     * @author Lukas Droese
     * @param s Stich (in Form eines Stack), der dem Spieler gehoert
     */
    //Stich von einem übergebenen Stack wird in stiche übertragen
    public void addStich(Stack<Card> s){
        for (int l = 0; l < 5; l++){
            stiche.push(s.pop());
        }
    }

    /**
     * Methode uebergibt 4 Karten an die Hand des Human
     *
     * @author Lukas Droese
     * @param c Stack mit Karten, die der Hand des Human hinzugefuegt werden
     */
    //Die Karten aus dem Stack werden auf die Hand gebracht
    public void addCards(Stack<Card> c)
    {
        hand.add(c.pop());
        hand.add(c.pop());
        hand.add(c.pop());
        hand.add(c.pop());
    }

    /**
     * Getter Methode von Hand
     *
     * @author Lukas Droese, Alex Ullrich
     * @return Hand des jeweiligen Spielers
     */
    //getter Methode von hand
    public LinkedList<Card> getHand()
    {
        return hand;
    }

    /**
     * Getter Methode von Name
     *
     * @author Lukas Droese
     * @return Name des jeweiligen Spielers
     */
    //getter Methode von name
    public String getName()
    {
        return name;
    }

    /**
     * Methode, die den Human mit Hilfe eines Scanners fragt, ob er spielen moechte
     *
     * Bei falschen Werten wird die Methode rekursiv erneut aufgerufen
     *
     * @author Lukas Droese, Noah Boeckmann, Alex Ullrich
     * @return 1 für "ja" und 0 für "nein"
     */
    //setter Methode von wannaplay
    public int setWannaplay()
    {
        System.out.println("Spielst du, " + name + "?");
        Scanner sca = new Scanner(System.in);
        int k;
        try {k = sca.nextInt();}
        catch (java.util.InputMismatchException e)
        {
            System.err.println("Unerlaubter Wert, nochmal versuchen...");
            return setWannaplay();
        }
        if (k==1)
        {
            wannaplay = true;
        }
        else if (k != 0)
        {
            System.out.println("Error. Tippe 1 für Spiel oder 0 für nicht spielen");
            return setWannaplay();
        }
        return k;
    }


    /**
     * Methode, die den Spieler mit Hilfe eines Scanners fragt, was er spielen moechte
     *
     * @author Lukas Droese, Alex Ullrich
     * @return ModeType, den der Spieler spielen moechte
     */
    //Was will Player spielen, wenn er spielen will
    public Mode.MODE_TYPE play()
    {

        //darf nur gemacht werden, wenn wannaplay true ist
        //TODO Fehler: Wenn man etwas < 1 bzw. > 8 eingibt, crasht das Spiel
        if (wannaplay) {
            System.out.println("Was spielst du, " + name + "? 1 - 3 Sauspiele S G E, 4 Wenz, 5 - 8 Solo S G E H");
            Scanner scan = new Scanner(System.in);
            int l = scan.nextInt();
            switch (l){
                case 1 : return Mode.MODE_TYPE.SAUSPIELSCHELLEN;
                case 2 : return Mode.MODE_TYPE.SAUSPIELGRAS;
                case 3 : return Mode.MODE_TYPE.SAUSPIELEICHEL;
                case 4 : return Mode.MODE_TYPE.WENZ;
                case 5 : return Mode.MODE_TYPE.SOLOSCHELLEN;
                case 6 : return Mode.MODE_TYPE.SOLOGRAS;
                case 7 : return Mode.MODE_TYPE.SOLOEICHEL;
                case 8 : return Mode.MODE_TYPE.SOLOHERZ;
                default : System.err.println("Error"); return play();
            }
        }
        else
        {
            System.err.println("Fehler: Spieler wurde gefragt, obwohl er nicht spielen moechte");
            return null;
        }
    }

    /**
     * Methode, die zum Auswaehlen einer Karte benutzt wird, die der Spieler legen möchte
     *
     * - Noetige Variablen werden intialisiert
     * - showPlayableCards() wird aufgerufen, um zu sehen, welche Karten der Spieler legen darf
     * - Auswahl wird mit Hilfe eines Scanners durchgefuegt
     * - Methode scannerFortInt(temp) behebt die Fehler, die bei falscher Eingabe gemacht werden
     * - Karte wird von der Hand entfernt
     *
     * @author Lukas Droese, Noah Boeckmann, Alex Ullrich
     * @return Karte, die in einen Stich gelegt werden soll
     */
    //mögliche karten werden gezeigt, eine ausgewählt und gelegt
    public Card kartelegen()
    {
        Card playingCard;
        Mode m = game.getMode();
        LinkedList<Card> temp = m.showPlayableCards((LinkedList<Card>) hand.clone(), game.getDump(), game.getCallingColor(), game.getMode().getModeType());
        System.out.println(name + ", du hast die Karten : ");
        for (Card karte: temp)
        {
            System.out.println(karte.getColor() + " mit Wert " + karte.getRank());
            //Gibt spielbare Karten aus
        }
        System.out.println("Gib eine Karte ein");

        System.out.println("Hand size: " + hand.size());
        playingCard = temp.get(scannerForInt(temp));
        hand.remove(playingCard);
        //game.addgespielteKarte(playingCard); Testweise auskommentiert, siehe _TODO
        return playingCard;
        //habe ich jetzt mal in loop() in Game gemacht -Ulli
    }

    /**
     * Doofe Methode, die eigentlich niemand braucht. Mal sehen, wer das hier sich wirklich alles durchliest :)
     */
    // sagt an ob er ein Bot-Game oder ein Online-Game spielen will
    public void onlineSpiel()
    {
        online = true;
        //Problem: muss irgendwie in MainMenu vom Player gewählt werden
    }

    /**
     * Methode, die die Eigenschaft des Humans durchsucht --> Kein Bot
     * Getter von bot
     *
     * @author Alex Ullrich
     * @return false, da der Human kein Bot ist
     */
    //Methode, die sagt, dass der Spieler kein Bot, sondern ein Human ist
    public boolean isBot()
    {
        return false;
    }

    /**
     * Methode, die den Spielmodus (z.B. Ramsch, Solo) an den Spieler uebergibt
     *
     * @author Alex Ullrich
     * @param m Mode des jeweiligen Spieles, in dem er gerade ist
     */
    public void giveMode(Mode.MODE_TYPE m) {
        System.out.println(name + "Der gespielte Mode ist: " +m);
    }

    /**
     * Methode, die dem Spieler den Spieler uebergibt, der spielt
     *
     * @author NOah Boeckmann, Alex Ullrich
     * @param p Index des Spielers, der spielt
     */
    public void giveSpielender(int p) {
        System.out.println("Der Spieler " + (p+1) +" spielt");
    }

    /**
     * Methode, die den Index des Spielers uebergibt
     *
     * @author Alex Ullrich
     * @param n Index des Spielers
     */
    public void giveNumber(int n) {
        System.out.println("Du bis Spieler Nummer " + (n+1));
    }

    /**
     * Methode, die die Karte, die gespielt werden soll ausgibt
     *
     * - Catched mögliche Exceptions
     * --> wenn gecatched, dann wird die Methode rekursiv erneut aufgerufen
     *
     * @author Alex Ullrich
     * @param c1 Liste der spielbaren Karten auf einer Hand
     * @return Zahl des Indexes der Karte, die gespielt werden soll
     */
    public int scannerForInt(LinkedList<Card> c1)
    {
        Scanner sc = new Scanner(System.in);
        int k;
        try {k = sc.nextInt();}
        catch (java.util.InputMismatchException | NullPointerException  e)
        {
            System.err.println(e);
            System.out.println("Error. Wert nicht möglich! Neue Eingabe");
            return scannerForInt(c1);
        }
        try {c1.get(k);}
        catch (IndexOutOfBoundsException e)
        {
            System.err.println(e);
            System.out.println("Error. Wert nicht möglich! Neue Eingabe");
            return scannerForInt(c1);
        }
        return k;
    }

    /**
     * Methode ist egal. Ist nur aufgrund des Interfaces implementiert (wird bei der Klasse Bot gebraucht)
     */
    public void setMatrix(Card[][] botMatrix) {}
}