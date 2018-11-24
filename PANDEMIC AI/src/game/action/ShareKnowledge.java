package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameRules;
import game.GameStatus;
import objects.Character;
import objects.card.CityCard;
import objects.card.Hand;
import util.GameUtil;

public class ShareKnowledge extends GameAction {
	Character character;
	
	public ShareKnowledge(Character character) {
		super();
		this.character=character;
	}

	public boolean perform(GameStatus gameStatus) {
		CityCard cityCard = gameStatus.getCurrentHand().getCityCard(gameStatus.getCurrentCharacterPosition());
		if(cityCard != null && gameStatus.getCurrentCharacterPosition() == gameStatus.getCharacterPosition(character)) {
			if(super.perform(gameStatus)) {
				GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" gives card "+cityCard.getTitle()+" to "+character.getName());
				return GameRules.giveCard(gameStatus, gameStatus.getCurrentPlayer(), character, cityCard);
			}
		} else {
			cityCard = gameStatus.getCharacterHand(character).getCityCard(gameStatus.getCurrentCharacterPosition());
			if(cityCard != null && super.perform(gameStatus)) {
				GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" takes card "+cityCard.getTitle()+" from "+character.getName());
				return GameRules.giveCard(gameStatus, character, gameStatus.getCurrentPlayer(), cityCard);
			}
		}
		return false;
	}

	public static Set<ShareKnowledge> getValidGameActionSet(GameStatus gameStatus) {
		Set<ShareKnowledge> shareKnowledgeSet = new HashSet<ShareKnowledge>();
		for(Hand hand : gameStatus.getCharacterHandList()) {
			if(hand != gameStatus.getCurrentHand() && gameStatus.getCharacterPosition(hand.getCharacter()) == gameStatus.getCurrentCharacterPosition()) {
				CityCard cityCard = gameStatus.getCurrentHand().getCityCard(gameStatus.getCurrentCharacterPosition());
				if(cityCard == null) {
					cityCard = hand.getCityCard(gameStatus.getCharacterPosition(hand.getCharacter()));
				}
				if(cityCard != null) {
					shareKnowledgeSet.add(new ShareKnowledge(hand.getCharacter()));
				}
			}
		}
		return shareKnowledgeSet;
	}
}