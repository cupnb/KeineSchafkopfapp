package de.emghaar;
import de.emghaar.game.Game;
import de.emghaar.game.Human;

public class Main {
private static Game game;
    public static void main(String[] args) {
	// write your code here
    game = new Game(new Human("Alex"), new Human("Noah"), new Human("Sebi"), new Human("Niggo"));
    }
}
