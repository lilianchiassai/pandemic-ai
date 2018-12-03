package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameProperties;
import game.GameStatus;
import game.LightGameStatus;
import objects.City;
import objects.card.CityCard;
import util.GameUtil;

public class CharterFlight extends MoveAction {
	
	
	public CharterFlight(City origin, City destination) {
		super(origin, destination);
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
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.hand.remove(lightGameStatus.position.getCityCard());
		lightGameStatus.position = destination;
	}

	public boolean cancel(GameStatus gameStatus) {
		if(super.cancel(gameStatus)) {
			gameStatus.getCurrentHand().drawBack(gameStatus, origin.getCityCard());
			gameStatus.setCharacterPosition(gameStatus.getCurrentPlayer(), origin);
		}
		return true;
	}
	
	public static Set<CharterFlight> getValidGameActionSet(GameStatus gameStatus) {
		HashSet<CharterFlight> resultSet = new HashSet<CharterFlight>();
		if(gameStatus.getCurrentHand().getCityCard(gameStatus.getCurrentCharacterPosition()) != null) {
				resultSet.addAll(gameStatus.getCurrentCharacterPosition().getCharterFlightActionSet());
		}
		return resultSet;
	}
}