package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.material.City;
import pandemic.util.GameUtil;

class DirectFlightTest extends GameActionTest {

  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = origin.getDirectFlight(GameUtil.getCity("Moscow"));
    pandemic.gameState.getCurrentHand().add(GameUtil.getCity("Moscow").getCityCard());
  }

  @Test
  @Override
  void testPerform() {

    City destination = GameUtil.getCity("Moscow");

    pandemic.gameState.getCurrentHand().clear();
    assertNotPerform();
    assertEquals(origin, clone.gameState.getCurrentPlayerPosition());

    pandemic.gameState.getCurrentHand().add(destination.getCityCard());
    assertPerform();
    assertEquals(GameUtil.getCity("Moscow"), clone.gameState.getCurrentPlayerPosition());
    assertTrue(!clone.gameState.getCurrentHand().contains(destination.getCityCard()));

  }

}
