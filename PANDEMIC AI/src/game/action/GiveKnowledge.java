package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameRules;
import game.GameStatus;
import game.LightGameStatus;
import objects.Character;
import objects.card.CityCard;
import objects.card.Hand;
import util.GameUtil;

public class GiveKnowledge extends ShareKnowledge {
	
	public GiveKnowledge(Character character) {
		super(character);
	}

	public boolean perform(GameStatus gameStatus) {
		CityCard cityCard = gameStatus.getCurrentCharacterPosition().getCityCard();
		if(cityCard != null && gameStatus.getCurrentCharacterPosition() == gameStatus.getCharacterPosition(character)) {
			if(super.perform(gameStatus)) {
				GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" gives card "+cityCard.getTitle()+" to "+character.getName());
				return GameRules.giveCard(gameStatus, gameStatus.getCurrentPlayer(), character, cityCard);
			}
		}
		return false;
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.hand.remove(lightGameStatus.position.getCityCard());
		lightGameStatus.actionCount-=this.actionCost;
	}
}
