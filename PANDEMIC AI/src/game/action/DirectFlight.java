package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import objects.City;
import objects.card.Card;
import objects.card.CityCard;

public class DirectFlight extends MoveAction {
	
	public DirectFlight(GameStatus gameStatus, City destination) {
		super(destination);
	}
	
	@Override
	public boolean perform(GameStatus gameStatus) {
		CityCard cityCard = gameStatus.getCurrentPlayer().getHand().getCityCard(destination);
		if(cityCard != null && super.perform(gameStatus)) {
			gameStatus.getCurrentPlayer().getHand().discard(cityCard);
			return gameStatus.getCurrentPlayer().setPosition(destination);
		}
		return false;
	}


	public static Set<MoveAction> getValidGameActionSet(GameStatus gameStatus) {
		Set<MoveAction> directFlightSet = new HashSet<MoveAction>();
		for(Card cityCard : gameStatus.getCurrentPlayer().getHand().getCityCardSet()) {
			directFlightSet.add(new DirectFlight(gameStatus, ((CityCard)cityCard).getCity()));
		}
		return directFlightSet;
	}
}