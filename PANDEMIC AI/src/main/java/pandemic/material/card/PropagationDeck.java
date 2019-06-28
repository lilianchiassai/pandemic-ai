package pandemic.material.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import pandemic.util.GameUtil;

public class PropagationDeck implements Deck<PropagationCard> {

  int currentMemory;
  int[] memoryIndexRange;
  PropagationCard[] propagationDeck;
  int top;
  int bottom;
  int topLimit;

  private PropagationDeck(PropagationDeck other) {
    this.currentMemory = other.currentMemory;
    this.top = other.top;
    this.topLimit = other.topLimit;
    this.bottom = other.bottom;
    this.memoryIndexRange = other.memoryIndexRange.clone();
    this.propagationDeck = other.propagationDeck.clone();
    shuffle();
  }

  public PropagationDeck(int difficulty, ArrayList<PropagationCard> propagationCardReserve) {
    propagationDeck = propagationCardReserve
        .toArray(new PropagationCard[propagationCardReserve.size() + difficulty]);
    top = propagationCardReserve.size() - 1;
    bottom = 0;
    topLimit = top - bottom;
    memoryIndexRange = new int[difficulty];
    currentMemory = -1;
  }

  @Override
  public PropagationCard draw() {
    if (currentMemory >= 0 && memoryIndexRange[currentMemory] == top) {
      currentMemory--;
    }
    return propagationDeck[top--];
  }

  public PropagationCard drawBottomAndIntensify() {
    PropagationCard card = propagationDeck[bottom];

    bottom++;// remove card from the bottom of the deck
    topLimit++;
    propagationDeck[topLimit] = card;// add card to discard pile
    currentMemory++;// Discard becomes the new memory
    memoryIndexRange[currentMemory] = top + 1;// memoryIndexRange[currentMemory] contains the
                                              // inferior limit of the memoery

    top = topLimit;// refill deck

    GameUtil.randomizeArray(propagationDeck, memoryIndexRange[currentMemory], topLimit);
    return card;
  }

  @Override
  public void shuffle() {
    for (int i = 0; i < currentMemory + 2; i++) {
      GameUtil.randomizeArray(propagationDeck, i > 0 ? memoryIndexRange[i - 1] : bottom,
          i >= memoryIndexRange.length ? topLimit : (memoryIndexRange[i] - 1));
    }
  }

  @Override
  public PropagationDeck duplicate() {
    return new PropagationDeck(this);
  }

  @Override
  public boolean equivalent(Deck<PropagationCard> deck) {
    if (deck instanceof PropagationDeck) {
      PropagationDeck other = (PropagationDeck) deck;
      if (this.currentMemory != other.currentMemory) {
        return false;
      }
      if (this.top != other.top) {
        return false;
      }
      if (this.bottom != other.bottom) {
        return false;
      }
      if (!Arrays.equals(this.memoryIndexRange, other.memoryIndexRange)) {
        return false;
      }
      for (int i = 0; i <= currentMemory; i++) {
        int minRange = i > 0 ? other.memoryIndexRange[i - 1] : bottom;
        int maxRange = other.memoryIndexRange[i] - 1;
        Set<PropagationCard> set1 = new HashSet<PropagationCard>(
            Arrays.asList(Arrays.copyOfRange(this.propagationDeck, minRange, maxRange)));
        Set<PropagationCard> set2 = new HashSet<PropagationCard>(
            Arrays.asList(Arrays.copyOfRange(this.propagationDeck, minRange, maxRange)));
        if (!set1.equals(set2)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public int size() {
    return top - bottom + 1;
  }

}
