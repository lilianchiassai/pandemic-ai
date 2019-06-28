package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.util.GameUtil;

class GiveKnowledgeTest extends GameActionTest {

  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = origin.getGiveKnowledge(pandemic.gameState.getNextPlayer());
    pandemic.gameState.setCharacterPosition(pandemic.gameState.getNextPlayer(), origin);
    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
  }

  @Override
  @Test
  void testPerform() {

    pandemic.gameState.getCurrentHand().clear();
    assertNotPerform();

    pandemic.gameState.getCharacterHand(pandemic.gameState.getNextPlayer())
        .add(origin.getCityCard());
    assertNotPerform();
    assertTrue(pandemic.gameState.getCharacterHand(pandemic.gameState.getNextPlayer())
        .contains(origin.getCityCard()));

    Drive atlantaMiami = origin.getMultiDrive(GameUtil.getCity("Miami"));
    pandemic.perform(atlantaMiami);
    assertNotPerform();
    assertTrue(pandemic.gameState.getCharacterHand(pandemic.gameState.getNextPlayer())
        .contains(origin.getCityCard()));
    atlantaMiami.cancel(pandemic);

    pandemic.gameState.getCharacterHand(pandemic.gameState.getNextPlayer())
        .remove(origin.getCityCard());
    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
    assertPerform();
    assertTrue(!pandemic.gameState.getCharacterHand(pandemic.gameState.getNextPlayer())
        .contains(origin.getCityCard()));
    assertTrue(pandemic.gameState.getCurrentHand().contains(origin.getCityCard()));
  }

}
