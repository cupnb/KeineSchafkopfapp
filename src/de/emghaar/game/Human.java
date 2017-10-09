package de.emghaar.game;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Scanner;
import de.emghaar.game.card.Card;

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
    //Listenlänge von der Hamd
    private int laenge;

    //Konstruktor der Klasse Player
    public Human(String name)
    {
        laenge = 8;
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

    //setter Methode von game
    public void setGame(Game g)
    {
        game = g;
    }

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

    //getter Methode von player
    public boolean getPlayer()
    {
        return player;
    }

    //Methode, die die Stichpunktanzahl des Players um 1 erhöht
    public void stichpunkterhoehen(){
        stichanzahl = stichanzahl + 1;
    }

    //Gibt die Punkte des Spielers wieder
    public int getPunkte(){

        for (int y = stichanzahl * 4; y > 0; y--){
            points = points + stiche.pop().getPoints();
        }
        return points;
    }

    //Stich von einem übergebenen Stack wird in stiche übertragen
    public void addStich(Stack<Card> s){
        for (int l = 0; l < 5; l++){
            stiche.push(s.pop());
        }
    }

    //Die Karten aus dem Stack werden auf die Hand gebracht
    public void addCards(Stack<Card> c)
    {
        hand.add(c.pop());
        hand.add(c.pop());
        hand.add(c.pop());
        hand.add(c.pop());
    }

    //getter Methode von hand
    public LinkedList<Card> getHand()
    {
        return hand;
    }

    //getter Methode von name
    public String getName()
    {
        return name;
    }

    //setter Methode von wannaplay
    public int setWannaplay()
    {
        System.out.println("Spielst du?");
        Scanner sca = new Scanner(System.in);
        int k = sca.nextInt();
        if (k == 1)
        {
            wannaplay = true;
        }
        else if (k == 0)
        {
            wannaplay = false;
        }
        else
        {
            System.out.println("Error. Tippe 1 für Spiel oder 0 für nicht spielen");
            k = sca.nextInt();
        }
        return k;
    }


    //Was will Player spielen, wenn er spielen will
    public Mode.MODE_TYPE play()
    {

        //darf nur gemacht werden, wenn wannaplay true ist
        if (wannaplay) {
            System.out.println("Was spielst du?");
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
                default : System.err.println("Error"); play();
            }
        }
        else
        {
            return null;
        }
        return null;
    }

    //mögliche karten werden gezeigt, eine ausgewählt und gelegt
    public Card kartelegen()
    {
        Card playingCard = null;
        Mode m = game.getMode();
        LinkedList<Card> temp = m.showPlayableCards(hand, game.getDump(), game.getCallingColor(), game.getMode().getModeType());
        System.out.println("Du hast die Karten : ");
        for (Card karte: temp)
        {
            System.out.println(karte.getColor() + " mit Wert " + karte.getRank());
            //Gibt spielbare Karten aus
        }
        System.out.println("Gib eine Karte ein");
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        System.out.println(hand.size());
        if (k > laenge || k < 1){
            System.out.println("Error. Wert nicht möglich! Neue Eingabe");
            k = sc.nextInt();
        }
        else {
            playingCard = hand.get(k);
            hand.remove(k);
            game.addgespielteKarte(playingCard);
            laenge = laenge - 1;
        }
        return playingCard;
        //habe ich jetzt mal in loop() in Game gemacht -Ulli
    }

    // sagt an ob er ein Bot-Game oder ein Online-Game spielen will
    public void onlineSpiel()
    {
        online = true;
        //Problem: muss irgendwie in MainMenu vom Player gewählt werden
    }
    //Methode, die sagt, dass der Spieler kein Bot, sondern ein Human ist
    public boolean isBot()
    {
        return false;
    }
    //Methode, die vollständigkeithalber dem Human übergeben werden muss (bleibt leer)
    public void setMatrix(Card[][] matrix)
    {}

    public void giveMode(Mode.MODE_TYPE m) {
        System.out.println("Der gespielte Mode ist: " +m);
    }

    public void giveSpielender(int p) {
        System.out.println("Der Spieler " +p+1 +" spielt");
    }

    public void giveNumber(int n) {
        System.out.println("Du bis Spieler Nummer " +n+1);
    }
}