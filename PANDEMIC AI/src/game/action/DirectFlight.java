package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import objects.City;
import objects.card.Card;
import objects.card.CityCard;
import util.GameUtil;

public class DirectFlight extends MoveAction {
	
	public DirectFlight(City destination) {
		super(destination);
	}
	
	@Override
	public boolean perform(GameStatus gameStatus) {
		CityCard cityCard = gameStatus.getCurrentHand().getCityCard(destination);
		if(cityCard != null && super.perform(gameStatus)) {
			gameStatus.getCurrentHand().removeAndDiscard(cityCard);
			GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" flies directly from "+gameStatus.getCurrentCharacterPosition().getName()+" to "+ destination.getName());
			return gameStatus.setCharacterPosition(gameStatus.getCurrentPlayer(), destination);
		}
		return false;
	}


	public static Set<MoveAction> getValidGameActionSet(GameStatus gameStatus) {
		Set<MoveAction> directFlightSet = new HashSet<MoveAction>();
		for(Card cityCard : gameStatus.getCurrentHand().getCityCardSet()) {
			directFlightSet.add(((CityCard)cityCard).getCity().getDirectFlightAction());
		}
		return directFlightSet;
	}
}