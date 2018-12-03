package game.action;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import game.GameProperties;
import game.GameStatus;
import game.LightGameStatus;
import objects.ResearchCenter;
import objects.card.Card;
import objects.card.CityCard;
import util.GameUtil;

public class Build extends GameAction {
	
	public Build() {
		super();
	}

	public boolean perform(GameStatus gameStatus) {
		CityCard cityCard = gameStatus.getCurrentHand().getCityCard(gameStatus.getCurrentCharacterPosition());
		if(cityCard != null ) {
			if(canPerform(gameStatus)) {
				if(gameStatus.addResearchCenter(gameStatus.getCurrentCharacterPosition())) {
					GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" builds a new Research Center in "+gameStatus.getCurrentCharacterPosition().getName()+".");
					gameStatus.getCurrentHand().removeAndDiscard(cityCard);
					return super.perform(gameStatus);
				}
			}
		}
		return false;
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.hand.remove(lightGameStatus.position.getCityCard());
		lightGameStatus.researchCenterCity.add(lightGameStatus.position);
		lightGameStatus.actionCount-=this.actionCost;
	}
	
	public boolean canPerform(GameStatus gameStatus) {
		if(gameStatus.getCurrentActionCount() >= this.actionCost) {
			if(!gameStatus.hasResearchCenter(gameStatus.getCurrentCharacterPosition()) && gameStatus.getResearchCenterCurrentReserve().size()>0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean cancel(GameStatus gameStatus) {
		if(super.cancel(gameStatus)) {
			gameStatus.removeResearchCenter(gameStatus.getCurrentCharacterPosition());
			gameStatus.getCurrentHand().drawBack(gameStatus, gameStatus.getCurrentCharacterPosition().getCityCard());
		}
		return true;
	}

	public static Set<Build> getValidGameActionSet(GameStatus gameStatus) {
		Set<Build> buildFlightSet = new HashSet<Build>();
		CityCard cityCard = gameStatus.getCurrentHand().getCityCard(gameStatus.getCurrentCharacterPosition());
		if(cityCard != null && !gameStatus.hasResearchCenter(gameStatus.getCurrentCharacterPosition()) && gameStatus.getResearchCenterCurrentReserve().size()>0) {
			buildFlightSet.add(GameProperties.buildAction);
		}	
		return buildFlightSet;
	}
}