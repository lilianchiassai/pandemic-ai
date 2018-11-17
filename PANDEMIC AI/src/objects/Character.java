package objects;

import java.io.Serializable;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameProperties;
import objects.card.CityCard;
import objects.card.Hand;
import objects.card.PlayerCard;

public class Character implements Serializable{
	
	private static Logger logger = LogManager.getLogger(Character.class.getName());
	private static Map<String, Object> notifyMap;
	City position;
	Hand hand;
	int currentActionCount;
	String name;
	
	
	
	public Character(City city, String name) {
		this.position = city;
		this.name = name;
		this.currentActionCount = GameProperties.actionCount;
		this.hand= new Hand();
	}
	
	public Character(String name) {
		this.name = name;
		this.currentActionCount = GameProperties.actionCount;
		this.hand= new Hand();
	}

	public void log(String message) {
		if(gameStatus.isSimulated()) {
			logger.debug(message);
		} else {
			logger.info(message);
		}
	}
	
	public City getPosition() {
		return this.position;
	}

	public void hand(PlayerCard card) {
		log(this.name +" draws "+card.getTitle());
		hand.addOnTop(card);
	}
	
	public void newTurn() {
		this.currentActionCount = GameProperties.actionCount;
	}
	
	public boolean giveCard(Character character, CityCard card) {
		this.hand.removeCard(card);
		character.getHand().addOnTop(card);
		return true;
	}
	
	public boolean takeCard(Character character, CityCard card) {
		character.getHand().removeCard(card);
		this.hand.addOnTop(card);
		return true;
	}
	
	
	public Hand getHand() {
		return this.hand;
	}

	public String getName() {
		return this.name;
	}
	
	public int getCurrentActionCount() {
		return this.currentActionCount;
	}

	public void setCurrentActionCount(int i) {
		this.currentActionCount = i;
		
	}

	public boolean canPlay() {
		return this.currentActionCount>0;
	}

	public boolean setPosition(City destination) {
		this.position=destination;
		return true;
	}
	
	

}
