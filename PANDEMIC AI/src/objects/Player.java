package objects;

import game.Game;
import objects.card.Hand;

public class Player {
	City position;
	Hand hand;
	int actionCount;
	
	public Player(City city) {
		this.position = city;
		this.actionCount = 4;
	}
	
	public void draw() {
		
	}
	
	public City getPosition() {
		return this.position;
	}
}
