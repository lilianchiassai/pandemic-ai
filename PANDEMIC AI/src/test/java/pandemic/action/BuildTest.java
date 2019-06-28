package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuildTest extends GameActionTest {

  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = pandemic.gameState.getCurrentPlayerPosition().getBuildAction();
    pandemic.gameState.removeResearchCenter(origin);
    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
  }

  @Override
  @Test
  void testPerform() {
    pandemic.gameState.getCurrentHand().remove(origin.getCityCard());
    pandemic.gameState.removeResearchCenter(origin);
    assertNotPerform();
    assertTrue(!clone.gameState.hasResearchCenter(origin));

    pandemic.gameState.addResearchCenter(origin);
    assertNotPerform();
    assertTrue(clone.gameState.hasResearchCenter(origin));

    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
    assertNotPerform();
    assertTrue(clone.gameState.getCurrentHand().contains(origin.getCityCard()));

    pandemic.gameState.removeResearchCenter(origin);
    assertPerform();
    assertTrue(clone.gameState.hasResearchCenter(origin));
    assertTrue(!clone.gameState.getCurrentHand().contains(origin.getCityCard()));

  }
}
