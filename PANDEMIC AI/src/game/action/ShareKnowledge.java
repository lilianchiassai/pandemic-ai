package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameRules;
import game.GameStatus;
import objects.Character;
import objects.card.CityCard;
import objects.card.Hand;
import util.GameUtil;

public abstract class ShareKnowledge extends GameAction {
	Character character;
	
	public ShareKnowledge(Character character) {
		super();
		this.character=character;
	}

	public static Set<ShareKnowledge> getValidGameActionSet(GameStatus gameStatus) {
		Set<ShareKnowledge> shareKnowledgeSet = new HashSet<ShareKnowledge>();
		for(Hand hand : gameStatus.getCharacterHandList()) {
			if(hand != gameStatus.getCurrentHand() && gameStatus.getCharacterPosition(hand.getCharacter()) == gameStatus.getCurrentCharacterPosition()) {
				CityCard cityCard = gameStatus.getCurrentHand().getCityCard(gameStatus.getCurrentCharacterPosition());
				if(cityCard != null) {
					shareKnowledgeSet.add(new GiveKnowledge(hand.getCharacter()));
				} else {
					cityCard = hand.getCityCard(gameStatus.getCharacterPosition(hand.getCharacter()));
					if(cityCard != null) {
						shareKnowledgeSet.add(new ReceiveKnowledge(hand.getCharacter()));
					}
				}
				
			}
		}
		return shareKnowledgeSet;
	}

	public Character getCharacter() {
		return this.character;
	}
}