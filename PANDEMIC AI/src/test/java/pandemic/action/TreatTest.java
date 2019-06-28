package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.util.GameUtil;

class TreatTest extends GameActionTest {

  GameAction gameAction1;
  GameAction gameAction2;
  GameAction gameAction3;

  @Override
  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = new Treat(origin, origin.getDesease());
    gameAction1 = new Treat(origin, GameUtil.getCity("Miami").getDesease());
    gameAction2 = new Treat(origin, origin.getDesease(), 2);
    gameAction3 = new Treat(origin, origin.getDesease(), 2, 1);
  }

  @Override
  @Test
  void testPerform() {

    assertNotPerform(gameAction);
    assertNotPerform(gameAction1);
    assertNotPerform(gameAction2);
    assertNotPerform(gameAction3);

    pandemic.infect(origin, 3);
    assertNotPerform(gameAction1);
    assertNotPerform(gameAction3);
    assertPerform(gameAction);
    assertEquals(2, clone.gameState.getCityCubeCount(origin, origin.getDesease()));
    assertPerform(gameAction2);
    assertEquals(1, clone.gameState.getCityCubeCount(origin, origin.getDesease()));

    pandemic.findCure(origin.getDesease());
    assertNotPerform(gameAction);
    assertNotPerform(gameAction1);
    assertNotPerform(gameAction2);
    assertNotPerform(gameAction3);

    pandemic.gameState.removeCube(origin, origin.getDesease());
    assertNotPerform(gameAction);
    assertNotPerform(gameAction1);
    assertNotPerform(gameAction2);
    assertPerform(gameAction3);
    assertEquals(0, clone.gameState.getCityCubeCount(origin, origin.getDesease()));

    pandemic.infect(origin, 2, GameUtil.getDesease("Yellow"));
    assertNotPerform(gameAction);
    assertNotPerform(gameAction2);
    assertPerform(gameAction3);
    assertEquals(0, clone.gameState.getCityCubeCount(origin, origin.getDesease()));
    assertPerform(gameAction1);
    assertEquals(1, clone.gameState.getCityCubeCount(origin, GameUtil.getDesease("Yellow")));
  }

  @Test
  void testCanPerform() {
    clone = pandemic.duplicate();
    assertEquals(gameAction.canPerform(clone), gameAction.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction1.canPerform(clone), gameAction1.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction2.canPerform(clone), gameAction2.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction3.canPerform(clone), gameAction3.perform(clone));

    pandemic.infect(origin, 3);
    clone = pandemic.duplicate();
    assertEquals(gameAction.canPerform(clone), gameAction.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction1.canPerform(clone), gameAction1.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction2.canPerform(clone), gameAction2.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction3.canPerform(clone), gameAction3.perform(clone));

    pandemic.findCure(origin.getDesease());
    clone = pandemic.duplicate();
    assertEquals(gameAction.canPerform(clone), gameAction.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction1.canPerform(clone), gameAction1.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction2.canPerform(clone), gameAction2.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction3.canPerform(clone), gameAction3.perform(clone));

    pandemic.gameState.removeCube(origin, origin.getDesease());
    clone = pandemic.duplicate();
    assertEquals(gameAction.canPerform(clone), gameAction.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction1.canPerform(clone), gameAction1.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction2.canPerform(clone), gameAction2.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction3.canPerform(clone), gameAction3.perform(clone));

    pandemic.infect(origin, 2, GameUtil.getDesease("Yellow"));
    clone = pandemic.duplicate();
    assertEquals(gameAction.canPerform(clone), gameAction.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction1.canPerform(clone), gameAction1.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction2.canPerform(clone), gameAction2.perform(clone));
    clone = pandemic.duplicate();
    assertEquals(gameAction3.canPerform(clone), gameAction3.perform(clone));

  }

  @Test
  void testCancel() {
    pandemic.infect(origin, 3);
    clone = pandemic.duplicate();
    gameAction.perform(clone);
    gameAction.cancel(clone);
    assertTrue(pandemic.equivalent(clone));

    clone = pandemic.duplicate();
    gameAction2.perform(clone);
    gameAction2.cancel(clone);
    assertTrue(pandemic.equivalent(clone));

    pandemic.infect(origin, 1, GameUtil.getDesease("Yellow"));
    clone = pandemic.duplicate();
    gameAction1.perform(clone);
    gameAction1.cancel(clone);
    assertTrue(pandemic.equivalent(clone));

    pandemic.gameState.removeCube(origin, origin.getDesease());
    pandemic.findCure(origin.getDesease());
    clone = pandemic.duplicate();
    gameAction3.perform(clone);
    gameAction3.cancel(clone);
    assertTrue(pandemic.equivalent(clone));
  }

  void assertNotPerform(GameAction gameAction) {
    clone = pandemic.duplicate();
    assertTrue(!gameAction.perform(clone));
    assertTrue(pandemic.equivalent(clone));
  }

  void assertPerform(GameAction gameAction) {
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
