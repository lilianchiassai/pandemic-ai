package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import game.LightGameStatus;
import objects.City;

public abstract class MoveAction extends GameAction {

	City origin;
	City destination;
	
	public MoveAction(City origin, City destination) {
		super();
		this.destination=destination;
		this.origin=origin;
	}
	
	public boolean perform(GameStatus gameStatus) {
		return origin == gameStatus.getCurrentCharacterPosition() && super.perform(gameStatus);
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.actionCount-=this.actionCost;
		lightGameStatus.position=this.destination;
	}
	
	public boolean cancel(GameStatus gameStatus) {
		return destination == gameStatus.getCurrentCharacterPosition() && super.cancel(gameStatus);
	}

	public City getDestination() {
		return this.destination;
	}
	
	public City getOrigin() {
		return this.origin;
	}
}
