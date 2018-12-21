package game;

import java.util.HashSet;
import java.util.Set;

import objects.City;
import objects.Desease;
import objects.card.Card;

public class LightGameStatus {
	public GameStatus gameStatus;
	
	public Set<Card> hand;
	public Set<City> researchCenterCity;
	public Set<Desease> curedDeseaseSet;
	public City position;
	public int actionCount;
	
	
	public LightGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
		this.hand = new HashSet<Card>(gameStatus.getCurrentHand().getCardDeck());
		this.researchCenterCity = new HashSet<City>(gameStatus.getCityResearchCenterMap().keySet());
		this.position = gameStatus.getCurrentCharacterPosition();
		this.actionCount = gameStatus.getCurrentActionCount();
		this.curedDeseaseSet = new HashSet<Desease>(gameStatus.getCuredDeseaseSet());
	}
	
	public LightGameStatus(LightGameStatus lightGameStatus) {
		this.gameStatus = lightGameStatus.gameStatus;
		this.hand = new HashSet<Card>(lightGameStatus.hand);
		this.researchCenterCity = new HashSet<City>(lightGameStatus.researchCenterCity);
		this.position = lightGameStatus.position;
		this.actionCount = lightGameStatus.actionCount;
		this.curedDeseaseSet = new HashSet<Desease>(gameStatus.getCuredDeseaseSet());
	}

	public LightGameStatus lightClone() {
		return new LightGameStatus(this);
	}
}
