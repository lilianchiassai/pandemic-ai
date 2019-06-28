package pandemic.material.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.Properties;
import pandemic.Reserve;
import pandemic.State;
import pandemic.util.GameUtil;

class HandTest {
  State state;
  Hand hand;

  @BeforeEach
  void setUp() {
    state = State.Builder.blankStateBuilder(new Properties(4, 5)).build();
    hand = new Hand(Reserve.getInstance().getCharacterReserve().get(0));
  }

  @Test
  void testHand() {
    assertNotNull(hand);
  }

  @Test
  void testGetDeseaseCards() {
    Set<CityCard> cardSet = new HashSet<CityCard>();
    cardSet.add(GameUtil.getCity("Miami").getCityCard());
    cardSet.add(GameUtil.getCity("Moscow").getCityCard());
    cardSet.add(GameUtil.getCity("Teheran").getCityCard());
    cardSet.add(GameUtil.getCity("Tokyo").getCityCard());
    hand.addAll(cardSet);
    Set<CityCard> resultSet = new HashSet<CityCard>();
    resultSet.add(GameUtil.getCity("Moscow").getCityCard());
    resultSet.add(GameUtil.getCity("Teheran").getCityCard());
    assertTrue(resultSet.containsAll(hand.getDeseaseCards(GameUtil.getDesease("Black"))));
    assertTrue(hand.getDeseaseCards(GameUtil.getDesease("Black")).containsAll(resultSet));
  }

  @Test
  void testGetCityCard() {
    hand.add(GameUtil.getCity("Miami").getCityCard());
    assertNull(hand.getCityCard(GameUtil.getCity("Atlanta")));
    assertEquals(GameUtil.getCity("Miami").getCityCard(),
        hand.getCityCard(GameUtil.getCity("Miami")));
  }

  @Test
  void testAddAndContains() {
    hand.add(GameUtil.getCity("Miami").getCityCard());
    assertTrue(hand.contains(GameUtil.getCity("Miami").getCityCard()));
  }

  @Test
  void testAddAllAndContainsAll() {
    Set<CityCard> cardSet = new HashSet<CityCard>();
    cardSet.add(GameUtil.getCity("Miami").getCityCard());
    cardSet.add(GameUtil.getCity("Moscow").getCityCard());
    cardSet.add(GameUtil.getCity("Teheran").getCityCard());
    cardSet.add(GameUtil.getCity("Tokyo").getCityCard());
    hand.addAll(cardSet);
    assertTrue(hand.containsAll(cardSet));
    assertEquals(4, hand.size());
  }

  @Test
  void testClear() {
    Set<CityCard> cardSet = new HashSet<CityCard>();
    cardSet.add(GameUtil.getCity("Miami").getCityCard());
    cardSet.add(GameUtil.getCity("Moscow").getCityCard());
    cardSet.add(GameUtil.getCity("Teheran").getCityCard());
    cardSet.add(GameUtil.getCity("Tokyo").getCityCard());
    hand.addAll(cardSet);
    hand.clear();
    assertEquals(0, hand.size());
    assertTrue(!hand.contains(GameUtil.getCity("Miami").getCityCard()));
  }

  @Test
  void testRemove() {
    hand.add(GameUtil.getCity("Miami").getCityCard());
    hand.remove(GameUtil.getCity("Miami").getCityCard());
    assertTrue(!hand.contains(GameUtil.getCity("Miami").getCityCard()));
  }

  @Test
  void testRemoveAll() {
    Set<CityCard> cardSet = new HashSet<CityCard>();
    cardSet.add(GameUtil.getCity("Miami").getCityCard());
    cardSet.add(GameUtil.getCity("Moscow").getCityCard());
    cardSet.add(GameUtil.getCity("Teheran").getCityCard());
    cardSet.add(GameUtil.getCity("Tokyo").getCityCard());
    hand.addAll(cardSet);
    Set<CityCard> removeSet = new HashSet<CityCard>();
    removeSet.add(GameUtil.getCity("Atlanta").getCityCard());
    removeSet.add(GameUtil.getCity("Moscow").getCityCard());
    removeSet.add(GameUtil.getCity("Teheran").getCityCard());
    removeSet.add(GameUtil.getCity("Tokyo").getCityCard());
    assertTrue(hand.removeAll(removeSet));
    assertTrue(hand.contains(GameUtil.getCity("Miami").getCityCard()));
    assertTrue(!hand.contains(GameUtil.getCity("Moscow").getCityCard()));
  }

  @Test
  void testIsEmpty() {
    hand.add(GameUtil.getCity("Miami").getCityCard());
    hand.remove(GameUtil.getCity("Miami").getCityCard());
    assertTrue(hand.isEmpty());
  }

  @Test
  void testSize() {
    Set<CityCard> cardSet = new HashSet<CityCard>();
    cardSet.add(GameUtil.getCity("Miami").getCityCard());
    cardSet.add(GameUtil.getCity("Moscow").getCityCard());
    cardSet.add(GameUtil.getCity("Teheran").getCityCard());
    cardSet.add(GameUtil.getCity("Tokyo").getCityCard());
    hand.addAll(cardSet);
    assertEquals(4, hand.size());
    hand.remove(GameUtil.getCity("Moscow").getCityCard());
    assertEquals(3, hand.size());
  }

  @Test
  void testDuplicate() {
    Hand duplicate = hand.duplicate();
    assertTrue(duplicate.equals(hand));
  }

  @Test
  void testHashCodeAndEquals() {
    Hand duplicate = hand.duplicate();
    assertTrue(duplicate.equals(hand));
    assertEquals(duplicate.hashCode(), hand.hashCode());
  }
}
