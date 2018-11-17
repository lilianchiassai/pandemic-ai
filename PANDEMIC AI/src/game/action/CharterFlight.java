package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameProperties;
import game.GameStatus;
import objects.City;
import objects.card.CityCard;

public class CharterFlight extends MoveAction {
	
	
	public CharterFlight(GameStatus gameStatus, City destination) {
		super(destination);
	}
	
	@Override
	public boolean perform(GameStatus gameStatus) {
		CityCard cityCard = gameStatus.getCurrentPlayer().getHand().getCityCard(gameStatus.getCurrentPlayer().getPosition());
		if(cityCard != null && super.perform(gameStatus)) {
			gameStatus.getCurrentPlayer().getHand().discard(cityCard);
			return gameStatus.getCurrentPlayer().setPosition(destination);
		}
		return false;
	}
	

	public static Set<MoveAction> getValidGameActionSet(GameStatus gameStatus) {
		Set<MoveAction> charterFlightSet = new HashSet<MoveAction>();
		if(gameStatus.getCurrentPlayer().getHand().getCityCard(gameStatus.getCurrentPlayer().getPosition()) != null) {
			for(City city : GameProperties.map.vertexSet()) {
				charterFlightSet.add(new CharterFlight(gameStatus, city));
			}
		}
		return charterFlightSet;
	}
}