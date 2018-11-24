package objects.card;

import java.util.Collections;
import java.util.LinkedList;

import objects.City;

public class PropagationDeck extends Deck {

	LinkedList<LinkedList<PropagationCard>> memories;
	Card draw;
	
	public PropagationDeck() {
		super(PropagationCard.class);
		memories = new LinkedList<LinkedList<PropagationCard>>();
	}
	
	public boolean addOnTop(Deck deck) {
		if(this.cardClass.isAssignableFrom(deck.getCardClass()) && deck.isDiscardPileEmpty()) {
			Collections.shuffle(deck.cardDeck);
			memories.add(new LinkedList<PropagationCard>((LinkedList<? extends PropagationCard>) deck.cardDeck));
			deck.cardDeck.clear();
			return true;
		}
		return false;
	}
	
	public Card draw() {
		if(memories.size()>0) {
			draw = memories.getLast().getLast();
			memories.getLast().remove(draw);
			if(memories.getLast().isEmpty()) {
				memories.removeLast();
			}
			return draw;
		} 
		
		return super.draw();
	}
	
	public PropagationDeck clone() {
		PropagationDeck clone = new PropagationDeck();
		clone.cardDeck = new LinkedList<Card>();
		clone.cardDeck.addAll(this.cardDeck);
		clone.memories = new LinkedList<LinkedList<PropagationCard>>();
		for(LinkedList<PropagationCard> memory : this.memories) {
			LinkedList<PropagationCard> memoryClone = new LinkedList<PropagationCard>(memory);
			Collections.shuffle(memoryClone);
			clone.memories.add(memoryClone);
		}
		clone.discardPile = this.discardPile != null ? this.discardPile.clone() : null;
		return clone;
	}

	public boolean isInMemory(City city) {
		for(LinkedList<PropagationCard> memory : memories) {
			for(PropagationCard memoryCard : memory) {
				if(memoryCard.getCity() == city) {
					return true;
				}
			}
		}
		return false;
	}
}
