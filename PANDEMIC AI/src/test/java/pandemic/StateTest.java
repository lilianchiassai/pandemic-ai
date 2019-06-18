/**
 * 
 */
package pandemic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.material.card.Hand;

public class StateTest extends BenchmarkTest {

	
	private State state;
	
	@BeforeEach
	protected void setUp() throws Exception {
		state = State.Builder.randomStateBuilder().build();
	}
	
	@AfterEach
	protected void tearDown() throws Exception {
		state=null;
	}

	@RepeatedTest(10)
	public void testState() throws Exception {
		assertTrue(state!=null);
		assertBetween(1,24,state.getTurnCount());
		assertBetween(0,state.gameProperties.maxEclosionCounter,state.eclosionCount);
		assertBetween(0,4,state.getCurrentActionCount());
		assertBetween(1, state.gameProperties.maxResearchCenterCounter, state.getResearchCenterCount());
		assertBetween(0,4, state.curedDeseaseCount);
		int k =0;
		int j=0;
		for(Desease desease : state.gameProperties.deseaseList) {
			if(state.isCured(desease)) k++;
			if(state.isEradicated(desease)) j++;
		}
		assertEquals(k,state.curedDeseaseCount);
		assertEquals(j, state.eradicatedDeseaseCount);
		assertBetween(0,state.curedDeseaseCount, state.eradicatedDeseaseCount);
		for(Hand hand : state.getAllCharacterHand()) {
			assertBetween(0,state.gameProperties.maxHandSize, hand.size());
		}
		for(City city:state.gameProperties.map) {
			for(Desease desease : state.gameProperties.deseaseList) {
				assertBetween(0,3,state.getCityCubeCount(city, desease));
			}
		}
		k=0;
		for(City city:state.gameProperties.map) {
			if(state.hasResearchCenter(city)) k++;
		}
		assertEquals(k, state.getResearchCenterCount());
		for(Desease desease:state.gameProperties.deseaseList) {
			assertBetween(0,24,state.deseaseCubeReserve[desease.id]);
		}
	}
	
	@RepeatedTest(20)
	public void testDuplicate() throws Exception {
		State duplicate = state.duplicate();
		assertEquals(state.gameProperties,duplicate.gameProperties);
		assertSame(state.gameProperties,duplicate.gameProperties);
		//assertEquals(state.getPropagationDeck(),duplicate.getPropagationDeck());
		Assertions.assertNotSame(state.propagationDeck,duplicate.propagationDeck);
		
		//assertEquals(state.getPropagationDeck(),duplicate.getPropagationDeck());
		assertNotSame(state.getPlayerDeck(),duplicate.getPlayerDeck());
	
		assertEquals(state.turnCount, duplicate.getTurnCount());
		assertEquals(state.eclosionCount, duplicate.eclosionCount);
		assertEquals(state.getCurrentActionCount(), duplicate.getCurrentActionCount());
		assertEquals(state.getGameStep(), duplicate.getGameStep());
		assertEquals(state.getResearchCenterCount(), duplicate.getResearchCenterCount());
		assertEquals(state.curedDeseaseCount, duplicate.curedDeseaseCount);
		assertEquals(state.eradicatedDeseaseCount, duplicate.eradicatedDeseaseCount);
		
		assertNotSame(state.getCurrentHand(), duplicate.getCurrentHand());
		
		assertArrayEquals(state.characterPositionMap, duplicate.characterPositionMap);
		assertNotSame(state.characterPositionMap, duplicate.characterPositionMap);
		
		assertEquals(state.cityCubeQuantity.length, duplicate.cityCubeQuantity.length);
		assertNotSame(state.cityCubeQuantity, duplicate.cityCubeQuantity);
		for(int i=0; i<state.cityCubeQuantity.length; i++) {
			assertArrayEquals(state.cityCubeQuantity[i], duplicate.cityCubeQuantity[i]);
			assertNotSame(state.cityCubeQuantity[i], duplicate.cityCubeQuantity[i]);
		}
		
		assertArrayEquals(state.cityBuilt, duplicate.cityBuilt);
		assertNotSame(state.cityBuilt, duplicate.cityBuilt);
		
		assertArrayEquals(state.curedDeseases, duplicate.curedDeseases);
		assertNotSame(state.curedDeseases, duplicate.curedDeseases);
		
		assertArrayEquals(state.deseaseCubeReserve, duplicate.deseaseCubeReserve);
		assertNotSame(state.deseaseCubeReserve, duplicate.deseaseCubeReserve);
		
		assertArrayEquals(state.eradicatedDeseases, duplicate.eradicatedDeseases);
		assertNotSame(state.eradicatedDeseases, duplicate.eradicatedDeseases);		
		
		assertEquals(state.getPreviousActionList(), duplicate.getPreviousActionList());
		assertNotSame(state.getPreviousActionList(), duplicate.getPreviousActionList());
	}
	
	public void testCureDesease() {
		Desease desease = state.gameProperties.deseaseList.get(0);
		boolean isCured = state.isCured(desease);
		int curedCount = state.curedDeseaseCount;
		state.cureDesease(desease);
		if(isCured) {
			assertEquals(curedCount, state.curedDeseaseCount);
		} else {
			assertEquals(curedCount+1, state.curedDeseaseCount);
		}
		assertTrue(state.isCured(desease));
	}
	
	private void assertBetween(int min, int max, int value) {
		if(value<min||max<value) {
			throw new AssertionError("The value is not in the provided range");
		}
	}
}
