package pandemic.action;

import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.material.PlayedCharacter;
import pandemic.material.card.CityCard;
import pandemic.util.GameUtil;

public class GiveKnowledge extends ShareKnowledge {
	
	public static int priority = 0;
	
	public GiveKnowledge(City origin, PlayedCharacter playedCharacter) {
		super(origin, playedCharacter);
	}
	
	public boolean canPerform(Pandemic pandemic) {
		if(pandemic.gameState.getCurrentHand().getCharacter() != this.playedCharacter) {
			if(pandemic.gameState.getCurrentActionCount() >= this.actionCost) {
				CityCard cityCard = pandemic.gameState.getCurrentPlayerPosition().getCityCard();
				if(pandemic.gameState.getCurrentPlayerPosition() == pandemic.gameState.getCharacterPosition(playedCharacter) && pandemic.gameState.getCurrentHand().contains(cityCard)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean perform(Pandemic pandemic) {
		if(pandemic.gameState.getCurrentHand().getCharacter() != this.playedCharacter) {
			CityCard cityCard = pandemic.gameState.getCurrentPlayerPosition().getCityCard();
			if(cityCard != null && pandemic.gameState.getCurrentPlayerPosition() == pandemic.gameState.getCharacterPosition(playedCharacter)  && pandemic.gameState.getCurrentHand().contains(cityCard)) {
				if(super.perform(pandemic)) {
					GameUtil.log(pandemic, GameAction.logger, pandemic.gameState.getCurrentPlayer().getName()+" gives card "+cityCard.getTitle()+" to "+playedCharacter.getName());
					return pandemic.giveCard(pandemic.gameState.getCurrentPlayer(), playedCharacter, cityCard);
				}
			}
		}
		return false;
	}
	
	public void cancel(Pandemic pandemic) {
		super.cancel(pandemic);
		pandemic.giveCard(playedCharacter, pandemic.gameState.getCurrentPlayer(), origin.getCityCard());
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.hand.remove(lightGameStatus.position.getCityCard());
		lightGameStatus.actionCount-=this.actionCost;
	}
	
	public int getPriority() {
		return GiveKnowledge.priority;
	}
}
