package pandemic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import pandemic.Pandemic.GameStep;
import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.material.card.CityCard;
import pandemic.material.card.Hand;
import pandemic.material.card.PlayerCard;
import pandemic.util.GameUtil;

public class PandemicTest {

  private Pandemic pandemic;

  @BeforeEach
  protected void setUp() throws Exception {
    pandemic = new Pandemic(State.Builder.randomStateBuilder().build());
  }

  @AfterEach
  protected void tearDown() throws Exception {
    pandemic = null;
  }

  @RepeatedTest(100)
  public void testPandemic() {
    assertTrue(pandemic != null);
    pandemic = new Pandemic(4, 5);
    assertTrue(pandemic != null);
    pandemic = new Pandemic(State.Builder.randomStateBuilder().build());
  }

  @Test
  public void testInfect() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2, 3)).build());
    City atlanta = Reserve.getInstance().getMap().get("Atlanta");
    City washington = Reserve.getInstance().getMap().get("Washington");
    City miami = Reserve.getInstance().getMap().get("Miami");
    assertEquals(0, pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
    pandemic.infect(atlanta, 2);
    assertEquals(2, pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
    assertEquals(0, pandemic.gameState.eclosionCount);
    pandemic.infect(washington, 4);
    assertEquals(1, pandemic.gameState.eclosionCount);
    assertEquals(3, pandemic.gameState.getCityCubeCount(washington, atlanta.getDesease()));
    assertEquals(3, pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
    assertEquals(1, pandemic.gameState.getCityCubeCount(miami, atlanta.getDesease()));
    pandemic.infect(atlanta, 1);
    assertEquals(3, pandemic.gameState.eclosionCount);
    assertEquals(3, pandemic.gameState.getCityCubeCount(washington, atlanta.getDesease()));
    assertEquals(3, pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
    assertEquals(3, pandemic.gameState.getCityCubeCount(miami, atlanta.getDesease()));
    assertEquals(1, pandemic.gameState
        .getCityCubeCount(Reserve.getInstance().getMap().get("Chicago"), atlanta.getDesease()));
    assertEquals(2, pandemic.gameState
        .getCityCubeCount(Reserve.getInstance().getMap().get("Montreal"), atlanta.getDesease()));
    pandemic.infect(miami, 2);
    assertEquals(3, pandemic.gameState.eclosionCount);
    assertEquals(3, pandemic.gameState.getCityCubeCount(washington, atlanta.getDesease()));
    assertEquals(3, pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
    assertEquals(2, pandemic.gameState.getCityCubeCount(miami, miami.getDesease()));

  }

  @Test
  public void testNextPlayer() {
    int turnCount = pandemic.gameState.getTurnCount();
    Hand currentHand = pandemic.gameState.getCurrentHand();
    pandemic.nextPlayer();
    assertEquals(turnCount + 1, pandemic.gameState.getTurnCount());
    assertEquals(
        (currentHand.getCharacter().id + 1) % pandemic.gameState.gameProperties.numberOfPlayers,
        pandemic.gameState.getCurrentHand().getCharacter().id);
  }

  @RepeatedTest(200)
  public void testDrawEndTurn() {
    int playerDeckSize = pandemic.gameState.getPlayerDeck().size();
    int epidemicCount = pandemic.gameState.getEpidemicCount();
    int currentHandSize = pandemic.gameState.getCurrentHand().size();
    pandemic.drawEndTurn();
    if (playerDeckSize > 2)
      assertEquals(playerDeckSize - 2, pandemic.gameState.getPlayerDeck().size());
    assertTrue(currentHandSize + playerDeckSize - pandemic.gameState.getPlayerDeck().size()
        - pandemic.gameState.getEpidemicCount()
        + epidemicCount == pandemic.gameState.getCurrentHand().size());
    assertTrue(pandemic.gameState.getEpidemicCount() == epidemicCount
        || pandemic.gameState.propagationDeck.size() == 48);
  }

  @Test
  public void testPropagationEndTurn() {
    int propagationDeckSize = pandemic.gameState.propagationDeck.size();
    pandemic.propagationEndTurn();
    if (propagationDeckSize > pandemic.gameState.getPropagationSpeed())
      assertEquals(propagationDeckSize - pandemic.gameState.getPropagationSpeed(),
          pandemic.gameState.propagationDeck.size());
    else
      assertEquals(0, pandemic.gameState.propagationDeck.size());
  }

  @Test
  public void testGiveCard() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2, 3)).build());
    pandemic.gameState.getAllCharacterHand()[0].add(GameUtil.getCity("Atlanta").getCityCard());
    pandemic.giveCard(pandemic.gameState.getAllCharacterHand()[0].getCharacter(),
        pandemic.gameState.getAllCharacterHand()[1].getCharacter(),
        GameUtil.getCity("Atlanta").getCityCard());
    assertTrue(pandemic.gameState.getAllCharacterHand()[1]
        .contains(GameUtil.getCity("Atlanta").getCityCard()));
  }

  @Test
  public void testCancelCure() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2, 3)).build());
    pandemic.findCure(GameUtil.getCity("Atlanta").getDesease());
    pandemic.cancelCure(GameUtil.getCity("Atlanta").getDesease());
    assertTrue(!pandemic.gameState.isCured(GameUtil.getCity("Atlanta").getDesease()));
  }

  @Test
  public void testCheckEradicated() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2, 3)).build());
    pandemic.infect(GameUtil.getCity("Miami"), 1);
    pandemic.gameState.cureDesease(GameUtil.getCity("Atlanta").getDesease());
    pandemic.gameState.cureDesease(GameUtil.getCity("Miami").getDesease());
    assertTrue(pandemic.checkEradicated(GameUtil.getCity("Atlanta").getDesease()));
    assertTrue(!pandemic.checkEradicated(GameUtil.getCity("Miami").getDesease()));
  }

  @Test
  public void testCanPlay() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2, 3)).build());
    assertTrue(pandemic.canPlay());
    pandemic.perform(GameUtil.getCity("Atlanta").getMultiDrive(GameUtil.getCity("Miami")));
    assertTrue(pandemic.canPlay());
    pandemic.perform(GameUtil.getCity("Miami").getMultiDrive(GameUtil.getCity("Atlanta")));
    pandemic.perform(GameUtil.getCity("Atlanta").getMultiDrive(GameUtil.getCity("Miami")));
    pandemic.perform(GameUtil.getCity("Miami").getMultiDrive(GameUtil.getCity("Atlanta")));
    assertTrue(!pandemic.canPlay());
  }

  @Test
  public void testMustDiscard1() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2, 3)).build());
    pandemic.setStep(GameStep.discard);
    while (pandemic.gameState.getCurrentHand()
        .size() < pandemic.gameState.gameProperties.maxHandSize) {
      PlayerCard card = pandemic.gameState.getPlayerDeck().draw();
      if (card instanceof CityCard) {
        pandemic.gameState.getCurrentHand().add((CityCard) card);
      }
    }
    assertTrue(!pandemic.mustDiscard());
    while (pandemic.gameState.getCurrentHand().size() < 1
        + pandemic.gameState.gameProperties.maxHandSize) {
      PlayerCard card = pandemic.gameState.getPlayerDeck().draw();
      if (card instanceof CityCard) {
        pandemic.gameState.getCurrentHand().add((CityCard) card);
      }
    }
    assertTrue(pandemic.mustDiscard());
  }

  @Test
  public void testAllActions() {
    // TODO
  }

  @Test
  public void testGetMoveActions() {
    // TODO
  }

  @Test
  public void testGetStaticActions() {
    // TODO
  }

  @Test
  public void testDuplicate() {
    Pandemic clone = pandemic.duplicate();
    assertNotSame(pandemic, clone);
    assertNotSame(pandemic.gameState, clone.gameState);
  }

  @Test
  public void testUpdate() {
    // TODO
  }

  @Test
  public void testIsWin() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2, 3)).build());
    for (Desease desease : pandemic.gameState.gameProperties.deseaseList) {
      pandemic.gameState.cureDesease(desease);
    }
    pandemic.update();
    assertTrue(pandemic.isOver());
    assertTrue(pandemic.isWin());
  }

  @Test
  public void testIsOver() {
    pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2, 3)).build());
    pandemic.infect(GameUtil.getCity("Atlanta"), 3);
    pandemic.infect(GameUtil.getCity("Atlanta"), 1);
    pandemic.infect(GameUtil.getCity("Atlanta"), 1);
    pandemic.infect(GameUtil.getCity("Atlanta"), 1);
    pandemic.infect(GameUtil.getCity("Atlanta"), 1);
    pandemic.infect(GameUtil.getCity("Atlanta"), 1);
    pandemic.infect(GameUtil.getCity("Atlanta"), 1);
    assertTrue(pandemic.isOver());
    assertTrue(!pandemic.isWin());
  }
}
