package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import objects.card.Card;

public class Discard extends GameAction {
	Card card;
	objects.Character character;
	
	public Discard( objects.Character character, Card card) {
		super();
		this.actionCost=0;
		this.character=character;
		this.card=card;
	}
	
	@Override
	public boolean perform(GameStatus gameStatus) {
		if(isValid() && super.perform(gameStatus)) {
			return character.getHand().discard(card);
		}
		return false;
	}
	
	public boolean isValid() {
		return character.getHand().getCardDeck().contains(card);
	}

	public static Set<GameAction> getValidGameActionSet(GameStatus gameStatus, objects.Character character) {
		Set<GameAction> discardSet = new HashSet<GameAction>();
		if(character.getHand().getCardDeck().size() > 7) {
			for(Card card : character.getHand().getCardDeck()) {
				discardSet.add(new Discard(character,card));
			}
		}
		return discardSet;
	}
}
