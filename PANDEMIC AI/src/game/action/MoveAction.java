package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import objects.City;

public abstract class MoveAction extends GameAction {
City destination;
	
	public MoveAction(City destination) {
		super();
		this.destination=destination;
	}

	public City getDestination() {
		return this.destination;
	}
}
