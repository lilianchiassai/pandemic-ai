package game.action;

import java.util.HashSet;
import java.util.Set;

import game.Game;
import objects.City;
import objects.card.Card;
import objects.card.CityCard;

public class CharterFlight extends MoveAction {
	
	
	public CharterFlight(Game game, City destination) {
		super(game,destination);
	}
	
	@Override
	public boolean perform() {
		CityCard cityCard = game.getCurrentPlayer().getHand().getCityCard(game.getCurrentPlayer().getPosition());
		if(cityCard != null && super.perform()) {
			game.getCurrentPlayer().getHand().discard(cityCard);
			return game.getCurrentPlayer().move(destination);
		}
		return false;
	}
	
	@Override
	public boolean isValid() {
		return game.getCurrentPlayer().getHand().getCityCard(game.getCurrentPlayer().getPosition()) != null;
	}

	public static Set<MoveAction> getValidGameActionSet(Game game) {
		Set<MoveAction> charterFlightSet = new HashSet<MoveAction>();
		if(game.getCurrentPlayer().getHand().getCityCard(game.getCurrentPlayer().getPosition()) != null) {
			for(City city : game.getMap().vertexSet()) {
				charterFlightSet.add(new CharterFlight(game, city));
			}
		}
		return charterFlightSet;
	}
}