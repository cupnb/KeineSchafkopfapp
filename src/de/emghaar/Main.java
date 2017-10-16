package de.emghaar;
import de.emghaar.game.Game;
import de.emghaar.game.Human;

public class Main {
private static Game game;
    public static void main(String[] args) {
	// write your code here
    game = new Game(new Human(args[0]), new Human(args[1]), new Human(args[2]), new Human(args[3]));
    }
}