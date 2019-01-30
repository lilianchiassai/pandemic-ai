package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import game.LightGameStatus;
import objects.City;

public abstract class MoveAction extends GameAction {

	City destination;
	
	public MoveAction(City destination) {
		super();
		this.destination=destination;
	}
	
	public boolean perform(GameStatus gameStatus) {
		return super.perform(gameStatus);
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.actionCount-=this.actionCost;
		lightGameStatus.position=this.destination;
	}

	public City getDestination() {
		return this.destination;
	}
}
