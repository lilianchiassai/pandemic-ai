package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiscardTest extends GameActionTest {

  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = new Discard(origin, pandemic.gameState.getCurrentPlayer(), origin.getCityCard());
    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
  }

  @Override
  @Test
  void testPerform() {
    assertPerform();
    assertEquals(0, clone.gameState.getCurrentHand().size());
  }

}
