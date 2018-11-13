package gameStatus;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import objects.City;
import objects.Desease;
import objects.Character;

public class GameEngine {
	
	private static Logger logger = LogManager.getLogger(GameEngine.class.getName());
	
	public static void main(String[] args) {
		//Starting game
		Player player = new Player();
		Game game = new Game(1,5);
		game.addObserver(player);
		game.start();
		
		
		
	}
}
