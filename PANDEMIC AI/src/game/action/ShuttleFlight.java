package game.action;

import java.util.HashSet;
import java.util.Set;

import gameStatus.Game;
import objects.City;

public class ShuttleFlight extends MoveAction {
	
	public ShuttleFlight(Game game, City destination) {
		super(game,destination);
	}
	
	@Override
	public boolean perform() {
		if(isValid() && super.perform()) {
			return game.getCurrentPlayer().move(destination);
		}
		return false;
	}

	@Override
	public boolean isValid() {
		return game.getCurrentPlayer().getPosition().hasResearchCenter() && destination.hasResearchCenter();
	}

	public static Set<MoveAction> getValidGameActionSet(Game game) {
		Set<MoveAction> shuttleFlightSet = new HashSet<MoveAction>();
		if(game.getCurrentPlayer().getPosition().hasResearchCenter()) {
			for(City city : game.getMap().vertexSet()) {
				if(city != game.getCurrentPlayer().getPosition() && city.hasResearchCenter()) {
					shuttleFlightSet.add(new ShuttleFlight(game, city));
				}
			}
		}
		return null;
	}
}