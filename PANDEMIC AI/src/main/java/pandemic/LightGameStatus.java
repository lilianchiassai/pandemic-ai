package pandemic;

import java.util.HashSet;
import java.util.Set;

import pandemic.material.City;
import pandemic.material.card.Card;

public class LightGameStatus {
	public State state;
	
	public Set<Card> hand;
	public boolean[] researchCenterCity;
	public boolean[] curedDeseaseSet;
	public City position;
	public int actionCount;
	
	
	public LightGameStatus(State state) {
		this.state = state;
		this.hand = new HashSet<Card>(state.getCurrentHand());
		this.researchCenterCity = state.cityBuilt.clone();
		this.position = state.getCurrentPlayerPosition();
		this.actionCount = state.getCurrentActionCount();
		this.curedDeseaseSet = state.curedDeseases.clone();
	}
	
	public LightGameStatus(LightGameStatus lightGameStatus) {
		this.state = lightGameStatus.state;
		this.hand = new HashSet<Card>(lightGameStatus.hand);
		this.researchCenterCity = state.cityBuilt.clone();
		this.position = lightGameStatus.position;
		this.actionCount = lightGameStatus.actionCount;
		this.curedDeseaseSet = state.curedDeseases.clone();
	}

	public LightGameStatus lightClone() {
		return new LightGameStatus(this);
	}
}
