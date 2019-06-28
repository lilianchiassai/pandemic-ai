package pandemic.material.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import pandemic.Reserve;
import pandemic.util.GameUtil;

class PlayerDeckTest {

  PlayerDeck playerDeck;

  @BeforeEach
  void setUp() {
    playerDeck = new PlayerDeck(2 + GameUtil.random.nextInt(4),
        Reserve.getInstance().getPlayerCardReserve());
  }

  @Test
  void testPlayerDeck() {
    assertNotNull(playerDeck);
    for (int i = 0; i < playerDeck.epidemicIndexRange.length; i++) {
      int deckSize = i == 0 ? playerDeck.playerDeck.length - playerDeck.epidemicIndexRange[i]
          : playerDeck.epidemicIndexRange[i - 1] - playerDeck.epidemicIndexRange[i];
      for (int k = i; k < playerDeck.epidemicIndexRange.length; k++) {
        int deckSize2 = k == 0 ? playerDeck.playerDeck.length - playerDeck.epidemicIndexRange[k]
            : playerDeck.epidemicIndexRange[k - 1] - playerDeck.epidemicIndexRange[k];
        // Biggest subdecks should be at the end of the playerDeck
        assertTrue(deckSize2 - deckSize >= 0);
        // size difference between subdecks should be max 1
        assertTrue(deckSize2 - deckSize <= 1);
      }
    }
    assertEquals(0, playerDeck.epidemicIndexRange[playerDeck.epidemicIndexRange.length - 1]);
    for (int i = 0; i < playerDeck.epidemicIndex.length; i++) {
      int minRange = playerDeck.epidemicIndexRange[i] - 1;
      int maxRange = i > 0 ? playerDeck.epidemicIndexRange[i - 1] : playerDeck.playerDeck.length;
      assertTrue(playerDeck.epidemicIndex[i] >= minRange);
      assertTrue(playerDeck.epidemicIndex[i] < maxRange);
    }
  }

  @Test
  void testDuplicate() {
    PlayerDeck other = playerDeck.duplicate();
    assertTrue(playerDeck.equivalent(other));
    assertNotSame(playerDeck, other);
    assertNotSame(playerDeck.epidemicIndex, other.epidemicIndex);
    assertSame(playerDeck.epidemicIndexRange, playerDeck.epidemicIndexRange);
  }

  @RepeatedTest(20)
  void testDraw() {
    for (int i = 0; i < 1 + playerDeck.playerDeck.length / playerDeck.epidemicIndex.length; i++) {
      int sizeBefore = playerDeck.size();
      playerDeck.draw();
      assertEquals(sizeBefore - 1, playerDeck.size());
    }
    assertEquals(1, playerDeck.getEpidemicCount());
  }

  @Test
  void testDraw2() {
    int deckSize = playerDeck.size();
    for (int i = 0; i < deckSize; i++) {
      assertNotNull(playerDeck.draw());
    }
    assertNull(playerDeck.draw());
    assertEquals(playerDeck.epidemicIndex.length, playerDeck.epidemicCount);
  }

  @Test
  void testShuffle() {
    playerDeck.shuffle();
    assertEquals(48, playerDeck.playerDeck.length);
    assertEquals(playerDeck.epidemicIndexRange.length, playerDeck.epidemicIndex.length);

    HashSet<Card> cards = new HashSet<Card>();
    int playerDeckSize = playerDeck.size();
    for (int i = 0; i < playerDeckSize; i++) {
      Card card = playerDeck.draw();
      cards.add(card);
    }
    assertEquals(49, cards.size());
  }
}
