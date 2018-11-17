package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import objects.ResearchCenter;
import objects.card.Card;
import objects.card.CityCard;

public class Build extends GameAction {
	
	public Build() {
		super();
	}

	public boolean perform(GameStatus gameStatus) {
		CityCard cityCard = gameStatus.getCurrentPlayer().getHand().getCityCard(gameStatus.getCurrentPlayer().getPosition());
		if(cityCard != null ) {
			if(super.canPerform(gameStatus)) {
				return gameStatus.addResearchCenter(gameStatus.getCurrentPlayer().getPosition()) && super.perform(gameStatus);
			}
		}
		return false;
	}

	public static Set<Build> getValidGameActionSet(GameStatus gameStatus) {
		Set<Build> buildFlightSet = new HashSet<Build>();
		for(Card cityCard : gameStatus.getCurrentPlayer().getHand().getCityCardSet()) {
			if(!gameStatus.getCurrentPlayer().getPosition().hasResearchCenter()) {
				buildFlightSet.add(new Build());
			}		
		}
		return buildFlightSet;
	}
}