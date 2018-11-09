package objects.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Deck {
	protected LinkedList<Card> cardDeck;
	private Deck discardPile;
	private Class cardClass = null;
	
	public Deck(Class cardClass) {
		this.cardDeck = new LinkedList<Card>();
		this.discardPile = new Deck(cardClass,new LinkedList<Card>(), null);
		this.cardClass = cardClass;
	}
	
	public Deck(Class cardClass, LinkedList<Card> list) {
		this.cardDeck = list;
		this.discardPile = new Deck(cardClass,new LinkedList<Card>(), null);
		this.cardClass = cardClass;
	}
	
	public Deck(Class cardClass, LinkedList<Card> cardDeck, Deck discardPile) {
		this.cardDeck = cardDeck;
		this.discardPile = discardPile;
		this.cardClass = cardClass;
	}
	
	public void shuffle() {
		Collections.shuffle(cardDeck);
	}
	
	public boolean addOnTop(Card card) {
		if(card != null) {
			if(this.cardClass.isAssignableFrom (card.getClass())) {
				this.cardDeck.add(card);
				return true;
			}
		}
		return false;
	}
	
	public boolean addOnTop(Deck deck) {
		if(this.cardClass.isAssignableFrom(deck.getCardClass()) && deck.isDiscardPileEmpty()) {
			this.cardDeck.addAll(deck.cardDeck);
			deck.cardDeck.clear();
			return true;
		}
		return false;
	}
	
	public boolean addOnTop(Set<Card> cardSet) {
		this.cardDeck.addAll(cardSet);	
		cardSet.clear();
		return true;
	}
	
	public LinkedList<Deck> split(int numberOfSubdecks) {
		LinkedList<Deck> partitions = new LinkedList<Deck>();
		int subDeckSize = cardDeck.size() / numberOfSubdecks;
		int rest = cardDeck.size() % numberOfSubdecks;
		
		for (int i = 0; i < cardDeck.size(); i += subDeckSize) {
			if((numberOfSubdecks - rest) * subDeckSize == i) {
				subDeckSize++;
			}
			Deck deck = new Deck(cardClass, new LinkedList<>(cardDeck.subList(i, i+subDeckSize)));
		    partitions.add(deck);
		}
		
		cardDeck.clear();
		return partitions;
	}
	
	public Card draw() {
		Card card = cardDeck.getLast();
		cardDeck.remove(card);
		return card;
	}
	
	public Card drawBottomCard() {
		Card card = cardDeck.getFirst();
		cardDeck.remove(card);
		return card;
	}
	
	public boolean discard(Card card) {
		this.cardDeck.remove(card);
		return this.discardPile.addOnTop(card);
	}
	
	public Class getCardClass() {
		return this.cardClass;
	}
	
	public boolean isDiscardPileEmpty() {
		return discardPile == null || discardPile.isEmpty();
	}
	
	public boolean isEmpty() {
		return cardDeck.isEmpty();
	}

	public Deck getDiscardPile() {
		return this.discardPile;
	}

	
}
