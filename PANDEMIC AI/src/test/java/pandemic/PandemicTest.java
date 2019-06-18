package pandemic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import pandemic.material.City;
import pandemic.material.PlayedCharacter;
import pandemic.material.card.Hand;
import pandemic.util.GameUtil;

public class PandemicTest extends BenchmarkTest {

	private Pandemic pandemic;
	
	@BeforeEach
	protected void setUp() throws Exception {
		pandemic = new Pandemic(State.Builder.randomStateBuilder().build());
	}
	
	@AfterEach
	protected void tearDown() throws Exception {
		pandemic=null;
	}
	
	@Test
	public void testPandemic() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testInfect() {
		pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2,3)).build());
		City atlanta = Reserve.getInstance().getMap().get("Atlanta");
		City washington = Reserve.getInstance().getMap().get("Washington");
		City miami = Reserve.getInstance().getMap().get("Miami");
		assertEquals(0,pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
		pandemic.infect(atlanta,2);
		assertEquals(2,pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
		assertEquals(0,pandemic.gameState.eclosionCount);
		pandemic.infect(washington, 4);
		assertEquals(1,pandemic.gameState.eclosionCount);
		assertEquals(3,pandemic.gameState.getCityCubeCount(washington, atlanta.getDesease()));
		assertEquals(3,pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
		assertEquals(1,pandemic.gameState.getCityCubeCount(miami, atlanta.getDesease()));
		pandemic.infect(atlanta, 1);
		assertEquals(3,pandemic.gameState.eclosionCount);
		assertEquals(3,pandemic.gameState.getCityCubeCount(washington, atlanta.getDesease()));
		assertEquals(3,pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
		assertEquals(3,pandemic.gameState.getCityCubeCount(miami, atlanta.getDesease()));
		assertEquals(1,pandemic.gameState.getCityCubeCount(Reserve.getInstance().getMap().get("Chicago"), atlanta.getDesease()));
		assertEquals(2,pandemic.gameState.getCityCubeCount(Reserve.getInstance().getMap().get("Montreal"), atlanta.getDesease()));
		pandemic.infect(miami, 2);
		assertEquals(3,pandemic.gameState.eclosionCount);
		assertEquals(3,pandemic.gameState.getCityCubeCount(washington, atlanta.getDesease()));
		assertEquals(3,pandemic.gameState.getCityCubeCount(atlanta, atlanta.getDesease()));
		assertEquals(2,pandemic.gameState.getCityCubeCount(miami, miami.getDesease()));
		
	}
	
	@Test
	public void testNextPlayer() {
		int turnCount = pandemic.gameState.getTurnCount();
		Hand currentHand = pandemic.gameState.getCurrentHand();
		pandemic.nextPlayer();
		assertEquals(turnCount+1, pandemic.gameState.getTurnCount());
		assertEquals((currentHand.getCharacter().id +1) % pandemic.gameState.gameProperties.numberOfPlayers, pandemic.gameState.getCurrentHand().getCharacter().id);
	}
	
	@RepeatedTest(20)
	public void testDrawEndTurn() {
		int playerDeckSize = pandemic.gameState.getPlayerDeck().size();
		int epidemicCount = pandemic.gameState.getEpidemicCount();
		int currentHandSize = pandemic.gameState.getCurrentHand().size();
		pandemic.drawEndTurn();
		if(playerDeckSize>2) assertEquals(playerDeckSize-2, pandemic.gameState.getPlayerDeck().size());
		assertTrue(currentHandSize+playerDeckSize-pandemic.gameState.getPlayerDeck().size()-pandemic.gameState.getEpidemicCount()+epidemicCount==pandemic.gameState.getCurrentHand().size());
		assertTrue(pandemic.gameState.getEpidemicCount()==epidemicCount || pandemic.gameState.propagationDeck.getDiscardPile().isEmpty());
	}
	
	@Test
	public void testPropagationEndTurn() {
		int propagationDeckSize = pandemic.gameState.propagationDeck.size();
		pandemic.propagationEndTurn();
		if (propagationDeckSize>pandemic.gameState.getPropagationSpeed()) assertEquals(propagationDeckSize-pandemic.gameState.getPropagationSpeed(), pandemic.gameState.propagationDeck.size());
		else assertEquals(0,pandemic.gameState.propagationDeck.size());
	}
	
	@Test
	public void testGiveCard() {
		pandemic = new Pandemic(State.Builder.blankStateBuilder(new Properties(2,3)).build());
		PlayedCharacter character1 = Reserve.getInstance().getCharacterList(2).get(0);
		PlayedCharacter character2 = Reserve.getInstance().getCharacterList(2).get(1);
		pandemic.gameState.getAllCharacterHand()[0].add(GameUtil.getCity("Atlanta").getCityCard());
		pandemic.giveCard(pandemic.gameState.getAllCharacterHand()[0].getCharacter(),pandemic.gameState.getAllCharacterHand()[1].getCharacter(),GameUtil.getCity("Atlanta").getCityCard());
		assertTrue(pandemic.gameState.getAllCharacterHand()[1].contains(GameUtil.getCity("Atlanta").getCityCard()));
	}
	
	@Test
	public void testUpdateStatus() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCancelCure() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCheckEradicated() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCanPlay() {
		fail("Not yet implemented");
	}

	@Test
	public void testMustDiscard() {
		fail("Not yet implemented");
	}
	@Test
	public void testAllActions() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetMoveActions() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetStaticActions() {
		fail("Not yet implemented");
	}
	@Test
	public void testDuplicate() {
		fail("Not yet implemented");
	}
	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}
	@Test
	public void testIsWin() {
		fail("Not yet implemented");
	}
	@Test
	public void testIsOver() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetGameStep() {
		fail("Not yet implemented");
	}
}
