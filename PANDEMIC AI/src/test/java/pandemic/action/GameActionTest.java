package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.Pandemic;
import pandemic.Properties;
import pandemic.State;
import pandemic.material.City;

public abstract class GameActionTest {

  Pandemic clone;
  Pandemic pandemic;
  GameAction gameAction;
  City origin;

  @BeforeEach
  void setUp() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(4, 5)).build());
    origin = pandemic.gameState.getCurrentPlayerPosition();
  }

  @Test
  void testCanPerform() {
    clone = pandemic.duplicate();
    assertEquals(gameAction.canPerform(clone), gameAction.perform(clone));
  }

  @Test
  abstract void testPerform();


  @Test
  void testCancel() {
    clone = pandemic.duplicate();
    gameAction.perform(clone);
    gameAction.cancel(clone);
    assertTrue(pandemic.equivalent(clone));
  }

  void assertNotPerform() {
    clone = pandemic.duplicate();
    assertTrue(!gameAction.perform(clone));
    assertTrue(pandemic.equivalent(clone));
  }

  void assertPerform() {
    pandemic.gameState.decreaseCurrentActionCount(pandemic.gameState.getCurrentActionCount());
    pandemic.gameState.increaseCurrentActionCount(gameAction.actionCost - 1);
    clone = pandemic.duplicate();
    assertTrue(!gameAction.perform(clone));
    assertTrue(pandemic.equivalent(clone));

    pandemic.gameState.decreaseCurrentActionCount(pandemic.gameState.getCurrentActionCount());
    pandemic.gameState.increaseCurrentActionCount(4);
    clone = pandemic.duplicate();
    assertTrue(gameAction.perform(clone));
    assertEquals(pandemic.gameState.getCurrentActionCount() - gameAction.actionCost,
        clone.gameState.getCurrentActionCount());
    assertTrue(!pandemic.equivalent(clone));
  }

}
