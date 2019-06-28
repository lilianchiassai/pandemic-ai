package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.material.City;
import pandemic.util.GameUtil;

class ShuttleFlightTest extends GameActionTest {

  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = origin.getShuttleFlight(GameUtil.getCity("Moscow"));
    pandemic.gameState.addResearchCenter(origin);
    pandemic.gameState.addResearchCenter(GameUtil.getCity("Moscow"));
  }

  @Override
  @Test
  void testPerform() {

    City destination = GameUtil.getCity("Moscow");

    pandemic.gameState.removeResearchCenter(origin);
    pandemic.gameState.removeResearchCenter(destination);
    assertNotPerform();
    assertEquals(origin, clone.gameState.getCurrentPlayerPosition());

    pandemic.gameState.addResearchCenter(origin);
    pandemic.gameState.removeResearchCenter(destination);
    assertNotPerform();
    assertEquals(origin, clone.gameState.getCurrentPlayerPosition());

    pandemic.gameState.removeResearchCenter(origin);
    pandemic.gameState.addResearchCenter(destination);
    assertNotPerform();
    assertEquals(origin, clone.gameState.getCurrentPlayerPosition());

    pandemic.gameState.addResearchCenter(origin);
    pandemic.gameState.addResearchCenter(destination);
    assertPerform();
    assertEquals(destination, clone.gameState.getCurrentPlayerPosition());

  }

}
