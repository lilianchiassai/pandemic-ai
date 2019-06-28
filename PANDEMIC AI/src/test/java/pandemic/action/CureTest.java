package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.material.Desease;
import pandemic.material.card.CityCard;
import pandemic.util.GameUtil;

class CureTest extends GameActionTest {

  HashSet<CityCard> cardSet;
  Desease desease;

  @BeforeEach
  void setUp() {
    super.setUp();
    desease = origin.getDesease();
    cardSet = new HashSet<CityCard>();
    cardSet.add(GameUtil.getCity("Atlanta").getCityCard());
    cardSet.add(GameUtil.getCity("Chicago").getCityCard());
    cardSet.add(GameUtil.getCity("London").getCityCard());
    cardSet.add(GameUtil.getCity("Paris").getCityCard());
    cardSet.add(GameUtil.getCity("Madrid").getCityCard());
    gameAction = new Cure(origin, desease, cardSet);
    pandemic.gameState.addResearchCenter(origin);
    pandemic.gameState.getCurrentHand().addAll(cardSet);
  }

  @Override
  @Test
  void testPerform() {

    pandemic.gameState.removeResearchCenter(origin);
    pandemic.gameState.getCurrentHand().clear();
    assertNotPerform();
    assertTrue(!clone.gameState.isCured(desease));

    pandemic.gameState.getCurrentHand().addAll(cardSet);
    assertNotPerform();
    assertTrue(!clone.gameState.isCured(desease));

    pandemic.gameState.getCurrentHand().remove(GameUtil.getCity("Paris").getCityCard());
    pandemic.gameState.addResearchCenter(origin);
    assertNotPerform();
    assertTrue(!clone.gameState.isCured(desease));
    assertEquals(4, clone.gameState.getCurrentHand().size());

    pandemic.gameState.getCurrentHand().add(GameUtil.getCity("Paris").getCityCard());
    pandemic.infect(GameUtil.getCity("Paris"), 1);
    assertPerform();
    assertEquals(0, clone.gameState.getCurrentHand().size());
    assertTrue(clone.gameState.isCured(desease));
    assertTrue(!clone.gameState.isEradicated(desease));

    pandemic.gameState.unCureDesease(desease);
    pandemic.gameState.getCurrentHand().addAll(cardSet);
    pandemic.gameState.removeCube(GameUtil.getCity("Paris"),
        GameUtil.getCity("Paris").getDesease());
    assertPerform();
    assertEquals(0, clone.gameState.getCurrentHand().size());
    assertTrue(clone.gameState.isCured(desease));
    assertTrue(clone.gameState.isEradicated(desease));

  }

}
