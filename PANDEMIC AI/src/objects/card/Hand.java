package objects.card;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.GameStatus;
import objects.Character;
import objects.City;
import util.GameUtil;

public class Hand extends Deck {
	
	objects.Character character;
	
	public Hand(Character character) {
		super(PlayerCard.class, new LinkedList<Card>(), new Deck(PlayerCard.class,new LinkedList<Card>(), null));
		this.character = character;
	}
	
	public Character getCharacter() {
		return this.character;
	}

	public Set<Card> getCityCardSet() {
		return  ((Stream<Card>)cardDeck.stream().filter(GameUtil.getCityCardPredicate())).collect(Collectors.toSet());
	}
	
	public CityCard getCityCard(City city) {

		Set<Card> cityCardSet = (Set<Card>) cardDeck.stream().filter(GameUtil.getCityCardPredicate(city)).collect(Collectors.toSet());
		Iterator<Card> it = cityCardSet.iterator();
		if(it.hasNext()) {
			return (CityCard) it.next();
		}
		return null;
	}
	
	public void removeAnddiscard(GameStatus gameStatus, PlayerCard card) {
		gameStatus.getPlayerDeck().getDiscardPile().addOnTop(card);
		this.cardDeck.remove(card);
	}
	
	public void drawBack(GameStatus gameStatus, PlayerCard card) {
		gameStatus.getPlayerDeck().getDiscardPile().removeCard(card);
		addOnTop(card);
	}
	
	public void removeAndDiscard(GameStatus gameStatus, Set<Card> cardSetDesease) {
		this.cardDeck.removeAll(cardSetDesease);
		gameStatus.getPlayerDeck().getDiscardPile().addOnTop(cardSetDesease);
	}
	
	public void drawBack(GameStatus gameStatus, Set<Card> cardSet) {
		gameStatus.getPlayerDeck().getDiscardPile().removeAll(cardSet);
		this.cardDeck.addAll(cardSet);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Card card : cardDeck) {
			sb.append(", ");
			sb.append(card.getTitle());
			
		}
		String result = sb.toString();
		return result != null & result.length()>2 ? result.substring(2) : "";
	}
	
	public Hand clone() {
		Hand clone = new Hand(this.character);
		clone.cardDeck = new LinkedList<Card>();
		clone.cardDeck.addAll(this.cardDeck);
		Collections.shuffle(clone.cardDeck);
		
		clone.discardPile = new Deck(PlayerCard.class,new LinkedList<Card>(), null);
		return clone;
	}

	
}
