package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import game.LightGameStatus;
import objects.card.Card;
import objects.card.PlayerCard;
import util.GameUtil;

public class Discard extends GameAction {
	Card card;
	objects.Character character;
	
	public Discard(objects.Character character, Card card) {
		super();
		this.actionCost=0;
		this.card=card;
		this.character=character;
	}

	@Override
	public boolean perform(GameStatus gameStatus) {
		if(isValid(gameStatus) && super.perform(gameStatus)) {
			GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" discards "+ card.getTitle() +".");
			return gameStatus.getCharacterHand(character).removeAndDiscard(card);
		}
		return false;
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.hand.remove(this.card);
	}
	
	public boolean isValid(GameStatus gameStatus) {
		return gameStatus.getCharacterHand(character).getCardDeck().contains(card);
	}
	
	public boolean cancel(GameStatus gameStatus) {
		if(super.cancel(gameStatus)) {
			gameStatus.getCharacterHand(character).drawBack(gameStatus, (PlayerCard) card);
		}
		return true;
	}

	public static Set<GameAction> getValidGameActionSet(GameStatus gameStatus, objects.Character character) {
		Set<GameAction> discardSet = new HashSet<GameAction>();
		if(gameStatus.getCharacterHand(character).getCardDeck().size() > 7) {
			for(Card card : gameStatus.getCharacterHand(character).getCardDeck()) {
				discardSet.add(((PlayerCard)card).getCharacterDiscard(character));
			}
		}
		return discardSet;
	}
	
	public Card getCard() {
		return this.card;
	}
}
