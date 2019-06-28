package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.util.GameUtil;

class DriveTest extends GameActionTest {

  @Override
  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = origin.getMultiDrive(GameUtil.getCity("Bogota"));
  }

  @Override
  @Test
  void testPerform() {

    assertPerform();
    assertEquals(GameUtil.getCity("Bogota"), clone.gameState.getCurrentPlayerPosition());

    pandemic.gameState.decreaseCurrentActionCount(3);
    assertNotPerform();
    assertNotEquals(GameUtil.getCity("Moscow"), clone.gameState.getCurrentPlayerPosition());

  }

}
