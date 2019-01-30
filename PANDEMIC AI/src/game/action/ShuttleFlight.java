package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameProperties;
import game.GameStatus;
import game.LightGameStatus;
import objects.City;
import util.GameUtil;

public class ShuttleFlight extends MoveAction {
	
	public ShuttleFlight(City destination) {
		super(destination);
	}
	
	@Override
	public boolean perform(GameStatus gameStatus) {
		if(gameStatus.hasResearchCenter(gameStatus.getCurrentCharacterPosition()) && gameStatus.hasResearchCenter(destination) && super.perform(gameStatus)) {
			GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" flies in a shuttle from "+gameStatus.getCurrentCharacterPosition().getName()+" to "+ destination.getName());
			return gameStatus.setCharacterPosition(gameStatus.getCurrentPlayer(), destination);
		}
		return false;
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.position = this.destination;
		lightGameStatus.actionCount-=this.actionCost;
	}

	public static Set<MoveAction> getValidGameActionSet(GameStatus gameStatus) {
		Set<MoveAction> shuttleFlightSet = new HashSet<MoveAction>();
		if(gameStatus.hasResearchCenter(gameStatus.getCurrentCharacterPosition())) {
			for(City city : gameStatus.getCityResearchCenterMap().keySet()) {
				shuttleFlightSet.add(city.getShuttleFlightAction());
			}
		}
		return shuttleFlightSet;
	}
	
	public static Set<MoveAction> getDefaultGameActionSet() {
		Set<MoveAction> shuttleFlightSet = new HashSet<MoveAction>();
		for(City city : GameProperties.map) {
			shuttleFlightSet.add(city.getShuttleFlightAction());
		}
		return shuttleFlightSet;
	}
}