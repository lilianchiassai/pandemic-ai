package objects;

import java.io.Serializable;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameProperties;
import game.GameStatus;
import objects.card.CityCard;
import objects.card.Hand;

public class Character implements Serializable{
	
	private static Logger logger = LogManager.getLogger(Character.class.getName());
	String name;
	
	public Character(String name) {
		this.name = name;
	}
	
	public City getPosition(GameStatus gameStatus) {
		return gameStatus.getCharacterPosition(this);
	}

	public String getName() {
		return this.name;
	}
}
