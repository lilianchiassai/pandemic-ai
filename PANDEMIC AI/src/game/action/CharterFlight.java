package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameProperties;
import game.GameStatus;
import objects.City;
import objects.card.CityCard;
import util.GameUtil;

public class CharterFlight extends MoveAction {
	
	
	public CharterFlight(City destination) {
		super(destination);
	}
	
	@Override
	public boolean perform(GameStatus gameStatus) {
		CityCard cityCard = gameStatus.getCurrentHand().getCityCard(gameStatus.getCurrentCharacterPosition());
		if(cityCard != null && super.perform(gameStatus)) {
			gameStatus.getCurrentHand().removeAndDiscard(cityCard);
			GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" takes a charter flight from "+gameStatus.getCurrentCharacterPosition().getName()+" to "+ destination.getName());			
			return gameStatus.setCharacterPosition(gameStatus.getCurrentPlayer(), destination);
		}
		return false;
	}
	

	public static Set<MoveAction> getValidGameActionSet(GameStatus gameStatus) {
		Set<MoveAction> charterFlightSet = new HashSet<MoveAction>();
		if(gameStatus.getCurrentHand().getCityCard(gameStatus.getCurrentCharacterPosition()) != null) {
			for(City city : GameProperties.map) {
				charterFlightSet.add(city.getCharterFlightAction());
			}
		}
		return charterFlightSet;
	}
}