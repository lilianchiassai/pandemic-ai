package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import game.LightGameStatus;
import objects.City;
import objects.card.Card;
import objects.card.CityCard;
import util.GameUtil;

public class DirectFlight extends MoveAction {
	
	public DirectFlight(City origin, City destination) {
		super(origin, destination);
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
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.hand.remove(destination.getCityCard());
		lightGameStatus.position = destination;
		lightGameStatus.actionCount-=this.actionCost;
	}

	public boolean cancel(GameStatus gameStatus) {
		if(super.cancel(gameStatus)) {
			gameStatus.getCurrentHand().drawBack(gameStatus, destination.getCityCard());
			gameStatus.setCharacterPosition(gameStatus.getCurrentPlayer(), origin);
		}
		return true;
	}

	public static Set<MoveAction> getValidGameActionSet(GameStatus gameStatus) {
		Set<MoveAction> directFlightSet = new HashSet<MoveAction>();
		for(Card cityCard : gameStatus.getCurrentHand().getCityCardSet()) {
			if(gameStatus.getCurrentCharacterPosition().getDirectFlightAction(((CityCard)cityCard).getCity()) != null)
			directFlightSet.add(gameStatus.getCurrentCharacterPosition().getDirectFlightAction(((CityCard)cityCard).getCity()));
		}
		return directFlightSet;
	}
}