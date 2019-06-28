package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.Pandemic;
import pandemic.Properties;
import pandemic.State;
import pandemic.util.GameUtil;

class ActionSerieTest {

  Pandemic clone;
  Pandemic pandemic;
  ActionSerie gameAction;

  @BeforeEach
  void setUp() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(4, 5)).build());
    gameAction = new ActionSerie();
    gameAction.add(
        pandemic.gameState.getCurrentPlayerPosition().getMultiDrive(GameUtil.getCity("Miami")));
    gameAction.add(GameUtil.getCity("Miami").getMultiDrive(GameUtil.getCity("Bogota")));
  }

  @Test
  void testPerform() {
    assertPerform();
    assertEquals(GameUtil.getCity("Bogota"), clone.gameState.getCurrentPlayerPosition());
    pandemic.perform(gameAction);
    assertNotPerform();

  }

  @Test
  void testCancel() {
    Pandemic clone = pandemic.duplicate();
    gameAction.perform(clone);
    gameAction.cancel(clone);
    assertTrue(pandemic.equivalent(clone));
  }

  void assertNotPerform() {
    clone = pandemic.duplicate();
    assertThrows(IllegalArgumentException.class, () -> gameAction.perform(clone));
  }

  void assertPerform() {
    pandemic.gameState.decreaseCurrentActionCount(pandemic.gameState.getCurrentActionCount());
    pandemic.gameState.increaseCurrentActionCount(gameAction.actionCost - 1);
    clone = pandemic.duplicate();
    assertThrows(IllegalArgumentException.class, () -> gameAction.perform(clone));

    pandemic.gameState.decreaseCurrentActionCount(pandemic.gameState.getCurrentActionCount());
    pandemic.gameState.increaseCurrentActionCount(4);
    clone = pandemic.duplicate();
    assertTrue(gameAction.perform(clone));
    assertEquals(pandemic.gameState.getCurrentActionCount() - gameAction.actionCost,
        clone.gameState.getCurrentActionCount());
    assertTrue(!pandemic.equivalent(clone));
  }



}
