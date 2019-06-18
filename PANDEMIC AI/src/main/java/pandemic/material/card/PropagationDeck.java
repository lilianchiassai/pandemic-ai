package pandemic.material.card;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import pandemic.material.City;

public class PropagationDeck extends Deck<PropagationCard> {

  /**
   * 
   */
  private static final long serialVersionUID = -8275244158719522842L;
  public LinkedList<LinkedList<PropagationCard>> memories;
  PropagationCard draw;

  public PropagationDeck() {
    super();
    memories = new LinkedList<LinkedList<PropagationCard>>();
  }

  public PropagationDeck(Set<PropagationCard> propagationCards) {
    super(propagationCards);
    memories = new LinkedList<LinkedList<PropagationCard>>();
  }

  public boolean addOnTop(Deck<PropagationCard> deck) {
    if (deck.isDiscardPileEmpty()) {
      Collections.shuffle(deck);
      memories.add(new LinkedList<PropagationCard>((LinkedList<? extends PropagationCard>) deck));
      deck.clear();
      return true;
    }
    return false;
  }

  public PropagationCard draw() {
    if (memories.size() > 0) {
      draw = memories.getLast().getLast();
      memories.getLast().remove(draw);
      if (memories.getLast().isEmpty()) {
        memories.removeLast();
      }
      return draw;
    }

    return super.draw();
  }

  public PropagationDeck duplicate() {
    PropagationDeck clone = new PropagationDeck();
    clone.addAll(this);
    clone.memories = new LinkedList<LinkedList<PropagationCard>>();
    for (LinkedList<PropagationCard> memory : this.memories) {
      LinkedList<PropagationCard> memoryClone = new LinkedList<PropagationCard>(memory);
      Collections.shuffle(memoryClone);
      clone.memories.add(memoryClone);
    }
    clone.discardPile = this.discardPile != null ? this.discardPile.duplicate() : null;
    return clone;
  }

  public boolean isInMemory(City city) {
    for (LinkedList<PropagationCard> memory : memories) {
      for (PropagationCard memoryCard : memory) {
        if (memoryCard.getCity() == city) {
          return true;
        }
      }
    }
    return false;
  }

  public LinkedList<LinkedList<PropagationCard>> getMemories() {
    return this.memories;
  }
}
