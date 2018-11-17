package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import objects.Character;
import objects.card.CityCard;

public class ShareKnowledge extends GameAction {
	Character character;
	
	public ShareKnowledge(Character character) {
		super();
		this.character=character;
	}

	public boolean perform(GameStatus gameStatus) {
		CityCard cityCard = gameStatus.getCurrentPlayer().getHand().getCityCard(gameStatus.getCurrentPlayer().getPosition());
		if(cityCard != null && gameStatus.getCurrentPlayer().getPosition() == character.getPosition()) {
			if(super.perform(gameStatus)) {
				return gameStatus.getCurrentPlayer().giveCard(character, cityCard);
			}
		} else {
			cityCard = character.getHand().getCityCard(gameStatus.getCurrentPlayer().getPosition());
			if(cityCard != null && super.perform(gameStatus)) {
				return gameStatus.getCurrentPlayer().takeCard(character, cityCard);
			}
		}
		return false;
	}

	public static Set<ShareKnowledge> getValidGameActionSet(GameStatus gameStatus) {
		Set<ShareKnowledge> shareKnowledgeSet = new HashSet<ShareKnowledge>();
		for(Character character : gameStatus.getCharacterList()) {
			if(character.getPosition() == gameStatus.getCurrentPlayer().getPosition()) {
				CityCard cityCard = gameStatus.getCurrentPlayer().getHand().getCityCard(gameStatus.getCurrentPlayer().getPosition());
				if(cityCard == null) {
					cityCard = character.getHand().getCityCard(character.getPosition());
				}
				if(cityCard != null) {
					shareKnowledgeSet.add(new ShareKnowledge(character));
				}
			}
		}
		return shareKnowledgeSet;
	}
}