package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PassTest extends GameActionTest {
  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = new Pass(origin, 4);
  }

  @Override
  @Test
  void testPerform() {
    assertPerform();
    assertEquals(0, clone.gameState.getCurrentActionCount());
  }
}
