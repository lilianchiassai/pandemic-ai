package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.util.GameUtil;

class CharterFlightTest extends GameActionTest {

  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = origin.getCharterFlight(GameUtil.getCity("Moscow"));
    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
  }

  @Override
  @Test
  void testPerform() {

    pandemic.gameState.getCurrentHand().clear();
    assertNotPerform();
    assertEquals(origin, clone.gameState.getCurrentPlayerPosition());

    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
    assertPerform();
    assertEquals(GameUtil.getCity("Moscow"), clone.gameState.getCurrentPlayerPosition());
    assertTrue(!clone.gameState.getCurrentHand().contains(origin.getCityCard()));

  }
}
