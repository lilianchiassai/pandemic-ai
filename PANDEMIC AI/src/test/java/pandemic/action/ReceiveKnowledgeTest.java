package pandemic.action;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pandemic.util.GameUtil;

class ReceiveKnowledgeTest extends GameActionTest {

  @BeforeEach
  void setUp() {
    super.setUp();
    gameAction = origin.getReceiveKnowledge(pandemic.gameState.getNextPlayer());
    pandemic.gameState.setCharacterPosition(pandemic.gameState.getNextPlayer(), origin);
    pandemic.gameState.getCharacterHand(pandemic.gameState.getNextPlayer())
        .add(origin.getCityCard());
  }

  @Override
  @Test
  void testPerform() {

    pandemic.gameState.getCharacterHand(pandemic.gameState.getNextPlayer()).clear();
    assertNotPerform();


    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
    assertNotPerform();
    assertTrue(pandemic.gameState.getCurrentHand().contains(origin.getCityCard()));

    Drive atlantaMiami = origin.getMultiDrive(GameUtil.getCity("Miami"));
    pandemic.perform(atlantaMiami);
    assertNotPerform();
    assertTrue(pandemic.gameState.getCurrentHand().contains(origin.getCityCard()));
    atlantaMiami.cancel(pandemic);

    pandemic.gameState.getCurrentHand().remove(origin.getCityCard());
    pandemic.gameState.getCharacterHand(pandemic.gameState.getNextPlayer())
        .add(origin.getCityCard());
    assertPerform();
    assertTrue(!clone.gameState.getCharacterHand(clone.gameState.getNextPlayer())
        .contains(origin.getCityCard()));
    assertTrue(clone.gameState.getCurrentHand().contains(origin.getCityCard()));
  }

}
