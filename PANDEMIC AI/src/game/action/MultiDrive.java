package game.action;

import game.GameStatus;
import objects.City;
import util.GameUtil;

public class MultiDrive extends MoveAction {

	
	public MultiDrive(City origin, City destination) {
		super(origin, destination);
		this.actionCost = origin.getDistance(destination);
	}
	
	public String toString() {
		return origin+" -> "+destination+" ("+actionCost+")";
	}
	
	public boolean perform(GameStatus gameStatus) {
		if(super.perform(gameStatus)) {
			GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" drives from "+gameStatus.getCurrentCharacterPosition().getName()+" to "+ destination.getName()+" in "+this.actionCost+" hours");
			
			return gameStatus.setCharacterPosition(gameStatus.getCurrentPlayer(), destination);
		}
		return false;
	}

}
