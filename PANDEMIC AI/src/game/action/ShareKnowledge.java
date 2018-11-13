package game.action;

import java.util.HashSet;
import java.util.Set;

import game.Game;
import objects.Character;
import objects.card.CityCard;

public class ShareKnowledge extends GameAction {
	Character character;
	
	public ShareKnowledge(Game game, Character character) {
		super(game);
		this.character=character;
	}

	public boolean perform() {
		CityCard cityCard = game.getCurrentPlayer().getHand().getCityCard(game.getCurrentPlayer().getPosition());
		if(cityCard != null && game.getCurrentPlayer().getPosition() == character.getPosition()) {
			if(super.perform()) {
				return game.getCurrentPlayer().giveCard(character, cityCard);
			}
		} else {
			cityCard = character.getHand().getCityCard(game.getCurrentPlayer().getPosition());
			if(cityCard != null && super.perform()) {
				return game.getCurrentPlayer().takeCard(character, cityCard);
			}
		}
		return false;
	}

	public boolean isValid() {
		CityCard cityCard = game.getCurrentPlayer().getHand().getCityCard(game.getCurrentPlayer().getPosition());
		if(cityCard != null && game.getCurrentPlayer().getPosition() == character.getPosition()) {
			return true;
		} else {
			cityCard = character.getHand().getCityCard(game.getCurrentPlayer().getPosition());
			if(cityCard != null) {
				return true;
			}
		}
		return false;
	}

	public static Set<ShareKnowledge> getValidGameActionSet(Game game) {
		Set<ShareKnowledge> shareKnowledgeSet = new HashSet<ShareKnowledge>();
		for(Character character : game.getCharacterList()) {
			if(character.getPosition() == game.getCurrentPlayer().getPosition()) {
				CityCard cityCard = game.getCurrentPlayer().getHand().getCityCard(game.getCurrentPlayer().getPosition());
				if(cityCard == null) {
					cityCard = character.getHand().getCityCard(character.getPosition());
				}
				if(cityCard != null) {
					shareKnowledgeSet.add(new ShareKnowledge(game, character));
				}
			}
		}
		return shareKnowledgeSet;
	}
}