package game.action;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import game.Game;
import objects.City;
import objects.card.Card;

public class Discard extends GameAction {
	Card card;
	objects.Character character;
	
	public Discard(Game game, objects.Character character, Card card) {
		super(game);
		this.actionCost=0;
		this.character=character;
		this.card=card;
	}
	
	@Override
	public boolean perform() {
		if(isValid() && super.perform()) {
			return character.getHand().discard(card);
		}
		return false;
	}
	
	public boolean isValid() {
		return character.getHand().getCardDeck().contains(card);
	}

	public static Set<GameAction> getValidGameActionSet(Game game, objects.Character character) {
		Set<GameAction> discardSet = new HashSet<GameAction>();
		if(character.getHand().getCardDeck().size() > 7) {
			for(Card card : character.getHand().getCardDeck()) {
				discardSet.add(new Discard(game,character,card));
			}
		}
		return discardSet;
	}
}
