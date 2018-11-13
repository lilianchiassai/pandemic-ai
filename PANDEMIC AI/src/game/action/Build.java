package game.action;

import java.util.HashSet;
import java.util.Set;

import game.Game;
import objects.City;
import objects.ResearchCenter;
import objects.card.Card;
import objects.card.CityCard;

public class Build extends GameAction {
	Game game;
	
	public Build(Game game) {
		super(game);
	}

	public boolean perform() {
		CityCard cityCard = game.getCurrentPlayer().getHand().getCityCard(game.getCurrentPlayer().getPosition());
		if(cityCard != null && !game.getCurrentPlayer().getPosition().hasResearchCenter()) {
			game.getCurrentPlayer().getHand().discard(cityCard);
			ResearchCenter researchCenter = game.getReserve().getResearchCenter();
			if(researchCenter != null && super.perform()) {
				researchCenter.build(game.getCurrentPlayer().getPosition());
				return true;
			}
		}
		return false;
	}

	public boolean isValid() {
		return game.getCurrentPlayer().getHand().getCityCard(game.getCurrentPlayer().getPosition()) != null && !game.getCurrentPlayer().getPosition().hasResearchCenter();
	}

	public static Set<Build> getValidGameActionSet(Game game) {
		Set<Build> buildFlightSet = new HashSet<Build>();
		for(Card cityCard : game.getCurrentPlayer().getHand().getCityCardSet()) {
			if(!game.getCurrentPlayer().getPosition().hasResearchCenter()) {
				buildFlightSet.add(new Build(game));
			}
			
		}
		return buildFlightSet;
	}
}