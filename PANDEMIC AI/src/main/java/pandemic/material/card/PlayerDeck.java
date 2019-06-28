package pandemic.material.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import pandemic.Reserve;
import pandemic.util.GameUtil;

public class PlayerDeck implements Deck<PlayerCard> {

  final int[] epidemicIndexRange;

  final int[] epidemicIndex;
  final CityCard[] playerDeck;

  int epidemicCount;
  int top;

  private PlayerDeck(PlayerDeck other) {
    this.epidemicIndexRange = other.epidemicIndexRange;
    this.epidemicIndex = new int[other.epidemicIndex.length];
    this.epidemicCount = other.epidemicCount;
    this.top = other.top;
    this.playerDeck = this.top > 0 ? Arrays.copyOfRange(other.playerDeck, 0, other.top) : null;
    this.shuffle();
  }

  public PlayerDeck(int difficulty, ArrayList<CityCard> cityCardReserve) {
    this.epidemicCount = 0;
    this.playerDeck = cityCardReserve.toArray(new CityCard[cityCardReserve.size()]);
    this.top = this.playerDeck.length - 1;
    this.epidemicIndexRange = new int[difficulty];
    int subDeckSize = this.playerDeck.length / difficulty;
    int biggerDeckQuantity = this.playerDeck.length % difficulty;
    for (int i = 0; i < difficulty; i++) {
      if (i == difficulty - biggerDeckQuantity) {
        subDeckSize++;
      }
      this.epidemicIndexRange[i] =
          (i > 0 ? this.epidemicIndexRange[i - 1] : this.playerDeck.length) - subDeckSize;
    }
    this.epidemicIndex = new int[difficulty];
    randomizeEpidemicIndex(0);
  }

  @Override
  public PlayerCard draw() {
    if (isNextCardEpidemic()) {
      epidemicCount++;
      return Reserve.getInstance().getEpidemicCardReserve();
    } else if (this.top < 0) {
      return null;
    } else {
      return playerDeck[top--];
    }
  }

  private boolean isNextCardEpidemic() {
    return this.epidemicCount < this.epidemicIndex.length
        && this.top + 1 == this.epidemicIndex[this.epidemicCount];
  }

  private void randomizeEpidemicIndex(int subDeckIndexStart) {
    for (int i = subDeckIndexStart; i < this.epidemicIndexRange.length - this.epidemicCount; i++) {
      int minRange = this.epidemicIndexRange[i];
      int maxRange = i > 0 ? this.epidemicIndexRange[i - 1] : this.top + 1;
      this.epidemicIndex[i] = minRange + GameUtil.random.nextInt(1 + maxRange - minRange);
    }
  }

  @Override
  public void shuffle() {
    if (this.playerDeck != null)
      GameUtil.randomizeArray(this.playerDeck);
    randomizeEpidemicIndex(this.epidemicCount);
  }

  @Override
  public PlayerDeck duplicate() {
    return new PlayerDeck(this);
  }

  @Override
  public boolean equivalent(Deck<PlayerCard> deck) {
    if (deck instanceof PlayerDeck) {
      PlayerDeck other = (PlayerDeck) deck;
      if (!Arrays.equals(this.epidemicIndexRange, other.epidemicIndexRange)) {
        return false;
      }
      if (this.epidemicIndex.length != other.epidemicIndex.length) {
        return false;
      }
      if (this.top != other.top) {
        return false;
      }
      if (this.epidemicCount != other.epidemicCount) {
        return false;
      }
      Set<CityCard> set1 =
          new HashSet<CityCard>(Arrays.asList(Arrays.copyOfRange(this.playerDeck, 0, this.top)));
      Set<CityCard> set2 =
          new HashSet<CityCard>(Arrays.asList(Arrays.copyOfRange(this.playerDeck, 0, this.top)));
      if (!set1.equals(set2)) {
        return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public int size() {
    return 1 + this.top + this.epidemicIndexRange.length - this.epidemicCount;
  }

  public int getEpidemicCount() {
    return this.epidemicCount;
  }
}
