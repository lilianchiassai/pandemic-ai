package pandemic.material.card;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import pandemic.Reserve;

public class PlayerDeck extends Deck<PlayerCard> {

  /**
   * 
   */
  private static final long serialVersionUID = -1556748585608292649L;
  int epidemicCounter;
  int[] subdeckSize;

  public PlayerDeck() {
    super();
    this.epidemicCounter = 0;
  }

  public PlayerDeck(Set<PlayerCard> playerCardReserve) {
    super(playerCardReserve);
    this.epidemicCounter = 0;
  }

  public LinkedList<Deck<PlayerCard>> split(int numberOfSubdecks) {
    this.subdeckSize = new int[numberOfSubdecks];
    LinkedList<Deck<PlayerCard>> partitions = new LinkedList<Deck<PlayerCard>>();
    int subDeckSize = this.size() / numberOfSubdecks;
    int rest = this.size() % numberOfSubdecks;

    for (int i = 0; i < this.size(); i += subDeckSize) {
      Deck<PlayerCard> deck;
      if (rest > 0) {
        subdeckSize[i / subDeckSize] = subDeckSize + 1;
        i++;
        deck =
            new Deck<PlayerCard>(new LinkedList<PlayerCard>(this.subList(i, i + subDeckSize + 1)));
        rest--;
      } else {
        subdeckSize[i / subDeckSize] = subDeckSize;
        deck = new Deck<PlayerCard>(new LinkedList<PlayerCard>(this.subList(i, i + subDeckSize)));
      }

      partitions.add(deck);
    }

    this.clear();
    return partitions;
  }

  public PlayerDeck duplicate() {
    PlayerDeck clone = new PlayerDeck();
    clone.epidemicCounter = this.epidemicCounter;
    clone.subdeckSize = this.subdeckSize;
    for (PlayerCard card : this) {
      if (!(card instanceof EpidemicCard)) {
        clone.add(card);
      }
    }
    Collections.shuffle(clone);

    int epidemicToAdd = subdeckSize.length - epidemicCounter;
    int remainingCards = clone.size();
    int min = 0;
    int max = 0;
    int nextEpidemicIndex;

    for (int i = 0; i < epidemicToAdd; i++) {
      min = max;
      max = remainingCards < subdeckSize[i] ? remainingCards : subdeckSize[i];
      nextEpidemicIndex = (int) (min + Math.random() * (1 + max - min) * 10 / 10);
      clone.add(nextEpidemicIndex, Reserve.getInstance().getEpidemicCardReserve());
      remainingCards -= max;

    }


    clone.discardPile = this.discardPile != null ? this.discardPile.duplicate() : null;
    return clone;
  }

  public int getEpidemicCounter() {
    return this.epidemicCounter;
  }

  public void increaseEpidemicCounter() {
    this.epidemicCounter++;
  }

  public int getTotalEpidemic() {
    return this.subdeckSize.length;
  }

  public int getSubdeckSize(int index) {
    return this.subdeckSize[index];
  }
}
