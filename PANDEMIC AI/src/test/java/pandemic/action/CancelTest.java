package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.util.GameUtil;

class CancelTest extends GameActionTest {

  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = new Cancel(origin);
    pandemic.gameState.removeResearchCenter(origin);
    origin.getMultiDrive(GameUtil.getCity("Miami")).perform(pandemic);
  }

  @Override
  @Test
  void testPerform() {
    assertTrue(gameAction.perform(pandemic));
    assertEquals(origin, pandemic.gameState.getCurrentPlayerPosition());
    assertTrue(!gameAction.perform(pandemic));
  }

  @Override
  @Test
  void testCancel() {
    assertThrows(IllegalArgumentException.class, () -> gameAction.cancel(pandemic));
  }
}
