package de.emghaar.game.Bots;
import java.util.LinkedList;
import java.util.Stack;

import de.emghaar.game.*;
import de.emghaar.game.card.Card;
import de.emghaar.game.card.CardColor;
import de.emghaar.game.card.CardRank;


public class Bot implements Player
{
    private LinkedList<Card> hand;
    private String name;
    private Game game;
    private int points;
    private boolean player;
    private boolean wannaplay;
    private Card [][] botMatrix;
    private Stack<Card> stiche;
    private int stichanzahl;
    private boolean[] mitspieler;
    private Mode.MODE_TYPE gamemode;
    private int spielender;
    private int playernumber;

    Bot(Game game1)
    {
        name = "Fuzzy";
        wannaplay = false;
        game = game1;
        points = 0;
        player = false;
        hand = new LinkedList<>();
        botMatrix = new Card[4][8];
        stiche = new Stack<>();
        stichanzahl = 0;
        mitspieler = new boolean[4];
        gamemode = Mode.MODE_TYPE.NICHTS;
        spielender = 0;
        playernumber = 0;
    }

    //------------------------Anfang Default Methoden------------------
    //Methode, die die Stichpunktanzahl des Players um 1 erhoeht
    public void stichpunkterhoehen(){
        stichanzahl = stichanzahl + 1;
    }

    public String getName() {
        return name;
    }

    public boolean getPlayer() {
        return player;
    }

    public void giveNumber(int n)
    {
        mitspieler[n] = true;
        playernumber = n;
    }

    //Gibt die Punkte des Spielers wieder
    public int getPunkte(){

        for (int y = stichanzahl * 4; y > 0; y--){
            points = points + stiche.pop().getPoints();
        }
        return points;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setPlayer(boolean p) {
        player = p;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isWannaplay() {
        return wannaplay;
    }

    public boolean isBot()
    {
        return true;
    }

    //1 heißt ja; 0 heißt nein
    public int setWannaplay()
    {
        return 0;
    }

    public Mode.MODE_TYPE play() {
        return null;
    }

    public Card kartelegen() {
        return null;
    }

    public void onlineSpiel()
    {}

    public Card[][] getBotMatrix() {
        return botMatrix;
    }

    public void setMatrix(Card[][] botMatrix) {
        this.botMatrix = botMatrix;
    }

    public void giveMode(Mode.MODE_TYPE m) {
        gamemode = m;
    }

    public void giveSpielender(int p)
    {
        spielender = p;
    }

    public LinkedList<Card> getHand() {
        return hand;
    }

    public void addCards(Stack<Card> c)
    {
        hand.add(c.pop());
        hand.add(c.pop());
        hand.add(c.pop());
        hand.add(c.pop());
    }

    //Stich von einem übergebenen Stack wird in stiche übertragen
    public void addStich(Stack<Card> s){
        for (int l = 0; l < 5; l++){
            stiche.push(s.pop());
        }
    }

    public int scannerForInt(LinkedList<Card> c1)
    {
        return 74154;
    }
    //------------------------Ende Default Methoden--------------------

    public void bestimmeMitspielerAnfang()
    {

        if(spielender != playernumber)
        {
            if(gamemode == Mode.MODE_TYPE.SOLOEICHEL || gamemode == Mode.MODE_TYPE.SOLOGRAS|| gamemode == Mode.MODE_TYPE.SOLOHERZ || gamemode == Mode.MODE_TYPE.SOLOSCHELLEN || gamemode == Mode.MODE_TYPE.WENZ)
            {
                for(int i = 0; i<4; i++)
                {
                    mitspieler[i] = i != spielender;
                }
            }
            else if(gamemode == Mode.MODE_TYPE.SAUSPIELEICHEL)
            {
                for(Card karte:hand)
                {
                    if(karte.getRank() == CardRank.ASS && karte.getColor() == CardColor.EICHEL)
                    {
                        mitspieler[spielender] = true;
                    }
                }
            }
            else if(gamemode == Mode.MODE_TYPE.SAUSPIELGRAS)
            {
                for(Card karte:hand)
                {
                    if(karte.getRank() == CardRank.ASS && karte.getColor() == CardColor.LAUB)
                    {
                        mitspieler[spielender] = true;
                    }
                }
            }
            else if(gamemode == Mode.MODE_TYPE.SAUSPIELSCHELLEN)
            {
                for(Card karte:hand)
                {
                    if(karte.getRank() == CardRank.ASS && karte.getColor() == CardColor.SCHELLEN)
                    {
                        mitspieler[spielender] = true;
                    }
                }
            }
        }
    }


    //public Card legeKarte()
    //{

    //}
    //---------------------------Anfang Fuzzy Methoden-------------------------------------------
    //Fuzzyfiziert die Punktzahl.
    //Die Rueckgabe ist ein Array mit der Zugehoerigkeit der Punktzahl zu fuenf verschiedenen linguistischen Variablen.
    //Genannte Variablen sind in Ihrer "Positivitaet" aufsteigend angeordnet.

    private double[] fuzzyPunkte(int punkte, int maximalePunktzahlImStich, boolean stichGehoertUns)
    {
        //Wenn mir der Stich nicht gehoert, dann gibt es keine Punkte zu holen:
        double[] toReturn = new double[5];
        if(!stichGehoertUns)
        {
            //Alle linguistischen Variablen sind auf 0 bis auf die schlechteste, die auf 1 ist.
            toReturn[0] = 1;
            toReturn[1] = 0;
            toReturn[2] = 0;
            toReturn[3] = 0;
            toReturn[4] = 0;
            return toReturn;
            //Wenn es weniger als 20 Punkte im Stich gibt, sind Punkte, die eigentlich mehr wert:
        }
        if (maximalePunktzahlImStich < 20) {
            //In diesem Fall haben wir eine gestauchte Fuzzy-Akkordeonsfunktion:
            return fuzzyAkkordeonsfunktion((double) punkte, (double) maximalePunktzahlImStich);
        }
        else
        {//In diesem Fall haben wir eine ungestauchteFuzzy-Akkordeonsfunktion über das Intervall [0, 20]:
            if (punkte< 20) {
                return fuzzyAkkordeonsfunktion((double) punkte, 20.00);
            }
            else
            {
                toReturn[0] = 0;
                toReturn[1] = 0;
                toReturn[2] = 0;
                toReturn[3] = 0;
                toReturn[4] = 1;
                return toReturn;
            }
        }
        //Ist die Punktzahl groesser 20, so liegen eindeutig viele Punkte im Stich:
    }

    /**
     * Fuzzyfiziert die Staerke einer Karte.
     * Die Rueckgabe ist ein Array mit der Zugehoerigkeit der Karte zu fuenf verschiedenen linguistischen Variablen.
     * Genannte Variablen sind in Ihrer "Positivitaet" aufsteigend angeordnet.
     * Es ist anzumerken, dass einer Karte eine Zahl zugeordnet werden muss, die bewertet werden soll.
     */
    private double[] fuzzyStaerke(double staerke, double anzahlUnterschiedlicheKarten) {
        return fuzzyAkkordeonsfunktion(staerke, anzahlUnterschiedlicheKarten);
    }

    /**
     *  Allgemeine Fuzzy-Funktion, die abhaengig von einem Maximalwert ist und wie ein Akkordeon aussieht.
     * Es werden fuenf lingusitische Variablen auf das Intervall [0, maximalwert] gestaucht.
     * Die Rueckgabe ist ein Array mit der Zugehoerigkeit der Zahl x zu den fuenf verschiedenen linguistischen Variablen.
     */
    private double[] fuzzyAkkordeonsfunktion(double x, double maximalwert) {
        //Vorbereitung des Rueckgabe-Arrays
        double[] Zugehoerigkeiten = new double[5];
        //Wir unterteilen das Intervall [0, maximalwert] in vier kleine Abschnitte
        double abschnittslaenge = maximalwert / 4;
        //Wir schauen uns den Peak aller fuenf linguistischen Variablen an und defenieren and diesen eine Dreiecksfunktion
        //Anschließend werfen wir unsere Punktzahl(x - Wert) gleich in genannte Funktion und speichern das Ergebnis.
        for(int i = 0; i<5; i++) {
            double peak = abschnittslaenge * (double) i;

            Zugehoerigkeiten[i] = dreiecksfunktion(x, peak - abschnittslaenge, peak, peak + abschnittslaenge);
        }
        //Rueckgabe
        return Zugehoerigkeiten;
    }

    /**
     * Allgemeine Dreiecksfunktion.
     * Die Rueckgabe ist der y-Wert der Dreiecksfunktion
     * ( fuer Duca: s. DSA Skript S. 16 )
     */


    private double dreiecksfunktion(double x, double anfangspunkt, double peak, double endpunkt)
    {
        if (x<anfangspunkt) {
            return 0.0;
        }
        else if (anfangspunkt <=x && x <= peak)
        {
            return (x - anfangspunkt) / (peak - anfangspunkt);
        }
        else if (peak <x && x <= endpunkt)
        {
            return (endpunkt - x) / (endpunkt - peak);
        }
        else
        {
            return 0.0;
        }
    }

    /**
     * Allgemeine R-Funktion, erinnert an einen Hockey-Schlaeger.
     * Die Rueckgabe ist der y-Wert der R-Funktion.
     * ( fuer Duca: s. DSA Skript S. 18 )
     */

    private double  R_Funktion(double x, double anfangswert, double peak) {

        if (x<anfangswert)
        {
            return 0.0;
        }
        else if (anfangswert <=x && x<peak)
        {
            return (x - anfangswert) / (peak - anfangswert);
        }
        else
        {
            return 1.0;
        }
    }

    /**
     * Allgemeine L-Funktion, erinnert auch an einen Hockey-Schlaeger.
     * Die Rueckgabe ist der y-Wert der L-Funktion.
     * ( fuer Duca: s. DSA Skript S. 20 )
     */

    private double L_Funktion(double x, double peak, double endwert) {

        if (x<peak) {
            return 1.0;
        }
        else if (peak <=x && x <= endwert) {
            return (endwert - x) / (endwert - peak);
        }
        else
        {
            return 0.0;
        }
    }

    /**
     * s- und t-Norm:
     * Gibt die kleinere der zwei Zahlen a und b zurueck.
     */


    private double min(double a, double b) {
        if (a<b)
        {
            return a;
        }
        else
        {
            return b;
        }
    }

    /**
     * Gibt die groessere der zwei Zahlen a und b zurueck.
     */
    private double max(double a, double b) {
        if (a > b)
        {
            return a;
        }
        else
        {
            return b;
        }
    }

    /**
     * Nach Mini-Max-Prinzip soll nun das Regelsystem aufgestellt werden.
     * Die Rueckgabe ist ein Array mit den zwei Fuzzywerten "Ja" und "Nein"
     */
    private double[] regelsystem(double[] fuzzyPunkte, double[] fuzzyStaerke) {
        //Das Regelsystem ist eine Einheitsmatrix die bewertet, ob eine Karte gelegt werden sollte.
        //( fuer Duca: Die Digonale sagt "Ja" und alle anderen Werte sagen "Nein". )
        double[] toReturn = new double[2];
        //x- und y-"Koordinaten" der Matrix
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                //Diagonale:
                if (x == y) {
                    toReturn[0] = max(toReturn[0], min(fuzzyPunkte[x], fuzzyStaerke[y]));
                }
                //Alles andere:
                else
                {
                    toReturn[1] = max(toReturn[1], min(fuzzyPunkte[x], fuzzyStaerke[y]));
                }
            }
        }

        return toReturn;
    }

    /**
     * Defuzzyfizierung:
     * Wir definieren zunächst die Flaeche, deren Schwerpunkt wir berechnen wollen:
     */

    private double defuzzyfunktion(double x, double Ja, double Nein) {
        return max(min(Ja, R_Funktion(x, 0, 1)), min(Nein, L_Funktion(x, 0, 1)));
    }

    /**
     * Gibt den x-Wert des Schwerpunkts zurueck. (Integralannaeherung)
     */

    private double berechneSchwerpunktDerDefuzzyfunktion(double Ja, double Nein) {
        double o = 0.0;
        double u = 0.0;
        double w = 1000.0;
        for (int d = 0; d<w; d++) {
            double x = (double )d / w;
            o = o + x * defuzzyfunktion(x, Ja, Nein);
            u = u + defuzzyfunktion(x, Ja, Nein);
        }
        return o / u;
    }

    public double fuzzy(int punkte, double staerke, int maximalePunktzahlImStich, boolean stichGehoertUns, double anzahlUnterschiedlicheKarten) {
        double[] L;
        L = regelsystem(fuzzyPunkte(punkte, maximalePunktzahlImStich, stichGehoertUns), fuzzyStaerke(staerke, anzahlUnterschiedlicheKarten));
        return berechneSchwerpunktDerDefuzzyfunktion(L[0], L[1]);
    }
    //---------------------------Ende Fuzzy Methoden---------------------------------------------
    //---------------------------Anfang Spielmechanik Methoden-----------------------------------
    //public boolean stichGehoertUns()
    //{

    //}


    //---------------------------Ende Spielmechanik Methoden-------------------------------------


    /*
    public void setWannaplay() {
        wannaplay = MethodeDieSagtObManSpielenWillVomBotAus;
    }
    */

}
