package pandemic.material.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.Reserve;

class PropagationDeckTest {

  PropagationDeck propagationDeck;

  @BeforeEach
  void setUp() {
    propagationDeck = new PropagationDeck(3, Reserve.getInstance().getPropagationCardReserve());
  }


  @Test
  void testPropagationDeck() {
    assertNotNull(propagationDeck);
    assertEquals(0, propagationDeck.bottom);
    assertEquals(47, propagationDeck.top);
    assertNull(propagationDeck.propagationDeck[48]);
    assertNull(propagationDeck.propagationDeck[49]);
    assertNotNull(propagationDeck.memoryIndexRange);
  }

  @Test
  void testDraw() {
    Card card = propagationDeck.draw();
    assertEquals(47, propagationDeck.size());
    assertNotSame(card, propagationDeck.draw());
  }

  @Test
  void testDrawBottomAndIntensify() {
    Card card = propagationDeck.drawBottomAndIntensify();
    assertEquals(0, propagationDeck.currentMemory);
    assertEquals(1, propagationDeck.bottom);
    assertEquals(48, propagationDeck.top);
    assertEquals(48, propagationDeck.memoryIndexRange[propagationDeck.currentMemory]);
    Card draw = propagationDeck.draw();
    assertEquals(card, draw);
    assertEquals(-1, propagationDeck.currentMemory);
  }

  @Test
  void testDrawBottomAndIntensify2() {

    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.drawBottomAndIntensify();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.drawBottomAndIntensify();
    propagationDeck.draw();
    propagationDeck.draw();
    assertEquals(1, propagationDeck.currentMemory);
    assertEquals(2, propagationDeck.bottom);
    assertEquals(47, propagationDeck.top);
    assertEquals(46, propagationDeck.memoryIndexRange[propagationDeck.currentMemory]);
    assertEquals(42, propagationDeck.memoryIndexRange[propagationDeck.currentMemory - 1]);
  }

  @Test
  void testDrawBottomAndIntensify3() {

    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.drawBottomAndIntensify();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.draw();
    propagationDeck.drawBottomAndIntensify();
    propagationDeck.draw();
    propagationDeck.draw();

  }

  @Test
  void testShuffle() {
    HashSet<PropagationCard> cardSet1 = new HashSet<PropagationCard>();
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.draw());
    cardSet1.add(propagationDeck.drawBottomAndIntensify());
    HashSet<PropagationCard> cardSet2 = new HashSet<PropagationCard>();
    cardSet2.add(propagationDeck.draw());
    cardSet2.add(propagationDeck.draw());
    cardSet2.add(propagationDeck.draw());
    cardSet2.add(propagationDeck.draw());
    cardSet2.add(propagationDeck.draw());
    cardSet2.add(propagationDeck.drawBottomAndIntensify());
    HashSet<PropagationCard> cardSet3 = new HashSet<PropagationCard>();
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.draw());
    cardSet3.add(propagationDeck.drawBottomAndIntensify());
    propagationDeck.draw();

    propagationDeck.shuffle();
    assertEquals(47, propagationDeck.size());

    for (int i = 34; i < 40; i++) {
      assertTrue(cardSet1.contains(propagationDeck.propagationDeck[i]));
    }
    for (int i = 40; i < 47; i++) {
      assertTrue(cardSet3.contains(propagationDeck.propagationDeck[i]));
    }


    assertEquals(15, cardSet1.size());
    assertEquals(6, cardSet2.size());
    assertEquals(11, cardSet3.size());
  }

  @Test
  void testDuplicate() {
    PropagationDeck other = propagationDeck.duplicate();
    assertTrue(propagationDeck.equivalent(other));
    assertNotSame(propagationDeck, other);
    assertNotSame(propagationDeck.memoryIndexRange, other.memoryIndexRange);
  }

  @Test
  void testSize() {
    propagationDeck.draw();
    propagationDeck.drawBottomAndIntensify();
    propagationDeck.draw();
    propagationDeck.draw();
    assertEquals(46, propagationDeck.size());

  }

}
