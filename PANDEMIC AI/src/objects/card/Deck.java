package objects.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import objects.Desease;
import util.GameUtil;

public class Deck {
	protected LinkedList<Card> cardDeck;
	protected Deck discardPile;
	protected Class cardClass = null;
	
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
	
	public Deck shuffle() {
		Collections.shuffle(cardDeck);
		return this;
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
	


	
	public Card draw() {
		if(cardDeck.size()>0) {
			Card card = cardDeck.getLast();
			cardDeck.removeLast();
			return card;
		} else {
			return null;
		}
	}
	
	public Card drawBottomCard() {
		if(cardDeck.size()>0) {
			Card card = cardDeck.getFirst();
			cardDeck.removeFirst();
			return card;
		} else {
			return null;
		}
	}
	
	public boolean discard(Card card) {
		return this.discardPile.addOnTop(card);
	}
	
	public boolean removeAndDiscard(Card card) {
		this.cardDeck.removeLastOccurrence(card);
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

	public List<Card> getCardDeck() {
		return this.cardDeck;
	}
	
	public void removeCard(Card card) {
		cardDeck.remove(card);
	}
	
	public Card getCard(String title) {
		Set<Card> cardSet = (Set<Card>) cardDeck.stream().filter(GameUtil.getCardTitlePredicate(title)).collect(Collectors.toSet());
		if(cardSet != null) {
			Iterator<Card> it = cardSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}
	
	public Deck clone() {
		Deck clone = new Deck(this.cardClass);
		clone.cardDeck = new LinkedList<Card>();
		clone.cardDeck.addAll(this.cardDeck);
		Collections.shuffle(clone.cardDeck);
		
		clone.discardPile = this.discardPile != null ? this.discardPile.clone() : null;
		return clone;
	}

	public void removeAll(Set<Card> cardSet) {
		this.cardDeck.removeAll(cardSet);
	}


}
