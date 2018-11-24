package objects.card;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import game.GameProperties;

public class PlayerDeck extends Deck {

	int epidemicCounter;
	int[] subdeckSize;
	
	public PlayerDeck() {
		super(PlayerCard.class);
		this.epidemicCounter = 0;
	}
	
	public LinkedList<Deck> split(int numberOfSubdecks) {
		this.subdeckSize = new int[numberOfSubdecks];
		LinkedList<Deck> partitions = new LinkedList<Deck>();
		int subDeckSize = cardDeck.size() / numberOfSubdecks;
		int rest = cardDeck.size() % numberOfSubdecks;
		
		for (int i = 0; i < cardDeck.size(); i += (subDeckSize+1)) {
			if(rest* (subDeckSize+1) == i) {
				subDeckSize--;
			}
			subdeckSize[i/subDeckSize] = subDeckSize;
			Deck deck = new Deck(cardClass, new LinkedList<>(cardDeck.subList(i, i+subDeckSize)));
		    partitions.add(deck);
		}
		
		cardDeck.clear();
		return partitions;
	}
	
	public PlayerDeck clone() {
		PlayerDeck clone = new PlayerDeck();
		clone.epidemicCounter = this.epidemicCounter;
		clone.subdeckSize = this.subdeckSize;
		clone.cardDeck = new LinkedList<Card>();
		for(Card card : this.cardDeck) {
			if(!(card instanceof EpidemicCard)) {
				clone.cardDeck.add(card);
			}
		}
		Collections.shuffle(clone.cardDeck);
		
		int epidemicToAdd = subdeckSize.length - epidemicCounter;		
		int remainingCards = clone.cardDeck.size();
		int min = 0;
		int max = 0;
		int nextEpidemicIndex;
		
		for(int i = 0; i<epidemicToAdd; i++) {
			min = max;
			max = remainingCards<subdeckSize[i] ? remainingCards : subdeckSize[i];
			nextEpidemicIndex = (int) (min + Math.random() * (1+max-min) *10/10);
			clone.cardDeck.add(nextEpidemicIndex, GameProperties.epidemicCardReserve);
			remainingCards-= max;
			
		}
		
		
		clone.discardPile = this.discardPile != null ? this.discardPile.clone() : null;
		return clone;
	}
	
	public int getEpidemicCounter() {
		return this.epidemicCounter;
	}
	
	public void increaseEpidemicCounter() {
		this.epidemicCounter++;
	}

}
