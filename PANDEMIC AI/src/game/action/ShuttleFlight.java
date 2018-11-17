package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameProperties;
import game.GameStatus;
import objects.City;

public class ShuttleFlight extends MoveAction {
	
	public ShuttleFlight(GameStatus gameStatus, City destination) {
		super(destination);
	}
	
	@Override
	public boolean perform(GameStatus gameStatus) {
		if(gameStatus.getCurrentPlayer().getPosition().hasResearchCenter() && destination.hasResearchCenter() && super.perform(gameStatus)) {
			return gameStatus.getCurrentPlayer().setPosition(destination);
		}
		return false;
	}


	public static Set<MoveAction> getValidGameActionSet(GameStatus gameStatus) {
		Set<MoveAction> shuttleFlightSet = new HashSet<MoveAction>();
		if(gameStatus.getCurrentPlayer().getPosition().hasResearchCenter()) {
			for(City city : GameProperties.map.vertexSet()) {
				if(city != gameStatus.getCurrentPlayer().getPosition() && city.hasResearchCenter()) {
					shuttleFlightSet.add(new ShuttleFlight(gameStatus, city));
				}
			}
		}
		return shuttleFlightSet;
	}
}