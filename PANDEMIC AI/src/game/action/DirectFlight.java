package game.action;

import java.util.HashSet;
import java.util.Set;

import game.Game;
import objects.City;
import objects.card.Card;
import objects.card.CityCard;

public class DirectFlight extends MoveAction {
	
	public DirectFlight(Game game, City destination) {
		super(game,destination);
	}
	
	@Override
	public boolean perform() {
		CityCard cityCard = game.getCurrentPlayer().getHand().getCityCard(destination);
		if(cityCard != null && super.perform()) {
			game.getCurrentPlayer().getHand().discard(cityCard);
			return game.getCurrentPlayer().move(destination);
		}
		return false;
	}

	@Override
	public boolean isValid() {
		CityCard cityCard = game.getCurrentPlayer().getHand().getCityCard(destination);
		if(cityCard != null) {
			return true;
		}
		return false;
	}

	public static Set<MoveAction> getValidGameActionSet(Game game) {
		Set<MoveAction> directFlightSet = new HashSet<MoveAction>();
		for(Card cityCard : game.getCurrentPlayer().getHand().getCityCardSet()) {
			directFlightSet.add(new DirectFlight(game, ((CityCard)cityCard).getCity()));
		}
		return directFlightSet;
	}
}