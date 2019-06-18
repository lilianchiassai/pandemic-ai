package pandemic.material.card;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import pandemic.util.GameUtil;

public class Deck<T extends Card> extends LinkedList<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2127816624527387650L;
	protected Deck<T> discardPile;
	
	public Deck() {
		super();
		this.discardPile = new Deck<T>(new LinkedList<T>(), null);
	}
	
	public Deck(LinkedList<T> list) {
		super(list);
		this.discardPile = new Deck<T>(new LinkedList<T>(), null);
	}
	
	public Deck(LinkedList<T> cardDeck, Deck<T> discardPile) {
		super(cardDeck);
		this.discardPile = discardPile;
	}
	
	public Deck(Set<T> cards) {
		super();
		this.discardPile = new Deck<T>(new LinkedList<T>(), null);
		addAll(cards);
		shuffle();
	}
	
	public Deck<T> shuffle() {
		Collections.shuffle(this);
		return this;
	}
	
	public boolean addOnTop(T card) {
		if(card != null) {
			this.add(card);
			return true;
		}
		return false;
	}
	
	public boolean addOnTop(Deck<T> deck) {
		if(deck.isDiscardPileEmpty()) {
			this.addAll(deck);
			deck.clear();
			return true;
		}
		return false;
	}
	
	public boolean addOnTop(Set<T> cardSet) {
		this.addAll(cardSet);	
		cardSet.clear();
		return true;
	}
	


	
	public T draw() {
		if(this.size()>0) {
			T card = this.getLast();
			this.removeLast();
			return card;
		} else {
			return null;
		}
	}
	
	public T drawBottomCard() {
		if(this.size()>0) {
			T card = this.getFirst();
			this.removeFirst();
			return card;
		} else {
			return null;
		}
	}
	
	public boolean discard(T card) {
		return this.discardPile.addOnTop(card);
	}
	
	public boolean removeAndDiscard(T card) {
		this.removeLastOccurrence(card);
		return this.discardPile.addOnTop(card);
	}
	
	public boolean isDiscardPileEmpty() {
		return discardPile == null || discardPile.isEmpty();
	}

	public Deck<T> getDiscardPile() {
		return this.discardPile;
	}
	
	public Card getCard(String title) {
		Set<Card> cardSet = this.stream().filter(GameUtil.getCardTitlePredicate(title)).collect(Collectors.toSet());
		if(cardSet != null) {
			Iterator<Card> it = cardSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}
	
	public Deck<T> duplicate() {
		Deck<T> clone = new Deck<T>();
		clone.addAll(this);
		Collections.shuffle(clone);
		
		clone.discardPile = this.discardPile != null ? this.discardPile.duplicate() : null;
		return clone;
	}
}
