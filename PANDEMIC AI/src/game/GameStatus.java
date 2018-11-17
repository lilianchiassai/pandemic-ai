package game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameRules.GameStep;
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.ResearchCenter;
import objects.card.Card;
import objects.card.Deck;
import objects.card.EpidemicCard;
import objects.card.PlayerCard;
import objects.card.PropagationCard;

public class GameStatus implements Serializable {

	//Final
	private static Logger logger = LogManager.getLogger(GameStatus.class.getName());
	
	private Deck propagationDeck;
	private Deck playerDeck;
	
	private int turnCounter;
	private int eclosionCounter;
	private int epidemicCounter;
	private Character currentPlayer;
	private GameRules.GameStep gameStep;
	
	private LinkedList<Character> characterList;
	
	private Map<City, Set<Cube>> cityCubeMap;
	private Map<City, ResearchCenter> cityResearchCenterMap;
	private Set<ResearchCenter> researchCenterCurrentReserve;
	private Map<Desease, Set<Cube>> cubeCurrentReserve;
	
	public GameStatus(int numberOfPlayers, int difficulty, City start) {
		
		//Building reserves and positioning maps
		cityCubeMap = new HashMap<City, Set<Cube>>();
		for(City city : GameProperties.map.vertexSet()) {
			cityCubeMap.put(city, new HashSet<Cube>());
		}
		cityResearchCenterMap = new HashMap<City, ResearchCenter>();
		
		researchCenterCurrentReserve = new HashSet<ResearchCenter>();
		for(ResearchCenter researchCenter : GameProperties.researchCenterReserve) {
			researchCenterCurrentReserve.add(researchCenter);
		}
		cubeCurrentReserve = new HashMap<Desease, Set<Cube>>();
		for(Desease desease : GameProperties.cubeReserve.keySet()) {
			Set<Cube> cubeSet = new HashSet<Cube>();
			for(Cube cube : GameProperties.cubeReserve.get(desease)) {
				cubeSet.add(cube);
			}
			cubeCurrentReserve.put(desease, cubeSet);
		}
		
		//Create characters
		for(int i = 0; i<numberOfPlayers; i++) {
			characterList.add(new Character(start, "Player "+(i+1)));
		}
		
		//Game status
		turnCounter = 1;
		eclosionCounter=0;
		epidemicCounter=0;
		gameStep = GameRules.GameStep.play;
		currentPlayer = characterList.getFirst();
		
		// Add research center on atlanta
		logger.info("Building a new Research Center in Atlanta.");
		addResearchCenter(start);
		
		// Create decks
		logger.info("Shuffling decks");
		propagationDeck = new Deck(PropagationCard.class); 	
		for(Card card : GameProperties.propagationCardReserve) {
			propagationDeck.addOnTop(card);
		}
		propagationDeck.shuffle();
		playerDeck = new Deck(PlayerCard.class); 
		for(Card card : GameProperties.playerCardReserve) {
			playerDeck.addOnTop(card);
		}
		
		logger.info("Dealing cards to each player.");
		playerDeck.shuffle();
		for(Character player : characterList) {
			for(int i = 0; i<6-numberOfPlayers; i++) {
				player.hand((PlayerCard) playerDeck.draw());
			}
		}
		
		// Add Epidemic cards and rebuild deck
		logger.info("Splitting player deck to add Epidemics.");
		List<Deck> deckList = playerDeck.split(difficulty);
		for(Deck subdeck : deckList) {
			subdeck.addOnTop(new EpidemicCard());
			subdeck.shuffle();
			playerDeck.addOnTop(subdeck);
		}
		
		// Draw 9 propagation card
		for (int i = 3; i>0; i--) {
			for (int k = 0; k<3; k++) {
				PropagationCard card = (PropagationCard) propagationDeck.draw();
				GameRules.infect(this, card.getCity(), i);
				propagationDeck.discard(card);
			}
		}
		
	}
	
	//Getters
	
	public List<Character> getCharacterList() {
		return this.characterList;
	}

	public Deck getPlayerDeck() {
		return this.playerDeck;
	}
	
	public Deck getPropagationDeck() {
		return this.propagationDeck;
	}
	
	public int getEclosionCounter() {
		return eclosionCounter;
	}
	
	public int getEpidemicCounter() {
		return epidemicCounter;
	}
	
	public int getPropagationSpeed() {
		return GameProperties.getPropagationSpeed(epidemicCounter);
	}
	
	public GameRules.GameStep getGameStep() {
		return this.gameStep;
	}
	
	public Character getCurrentPlayer() {
		return this.currentPlayer;
	}
	

	public Set<ResearchCenter> getResearchCenterReserve() {
		return this.researchCenterCurrentReserve;
	}
	
	public Map<City, ResearchCenter> getCityResearchCenterMap() {
		return this.cityResearchCenterMap;
	}

	//Setters
	public void nextPlayer() {
		currentPlayer = characterList.get((characterList.indexOf(currentPlayer)+1) % characterList.size());
	}
	
	public void increaseEclosionCounter() {
		eclosionCounter++;
	}
	
	public void increaseEpidemicCounter() {
		epidemicCounter++;
	}

	public void setGameStep(GameRules.GameStep gameStep) {
		this.gameStep = gameStep;
	}

	public void increaseTurnCounter() {
		this.turnCounter++;
	}

	public int getTurnCounter() {
		return this.turnCounter;
	}
	
	public void undeterministic() {
		//TODO
		playerDeck.shuffle();
		propagationDeck.shuffle();
	}

	public boolean removeFromReserve(Object obj) {
		if(obj instanceof ResearchCenter) {
			return researchCenterCurrentReserve.remove((ResearchCenter) obj);
		} else if(obj instanceof Cube) {
			return cubeCurrentReserve.get(((Cube)obj).getDesease()).remove((Cube)obj);
		} else {
			return false;
		}
		
	}
	
	public boolean addResearchCenter(City city) {
		Iterator<ResearchCenter> it = researchCenterCurrentReserve.iterator();
		if(it.hasNext() && cityResearchCenterMap.get(city) == null) {
			ResearchCenter researchCenter = it.next();
			removeFromReserve(researchCenter);
			cityResearchCenterMap.put(city, researchCenter);
			return true;
		} else {
			return false;
		}
	}

	public boolean addCube(City city, Desease desease) {
		Iterator<Cube> it = cubeCurrentReserve.get(desease).iterator();
		if(it.hasNext()) {
			Cube cube = it.next();
			removeFromReserve(cube);
			cityCubeMap.get(city).add(cube);
			return true;
		} else {
			return false;
		}
	}

	public boolean removeAndReserveCubeSet(City city, Desease desease) {
		return reserveCubeSet(desease, removeCubeSet(city, desease));
	}
	
	private Set<Cube> removeCubeSet(City city, Desease desease) {
		Set<Cube> cubeSet = cityCubeMap.get(desease);
		cityCubeMap.get(desease).removeAll(cubeSet);
		return cubeSet;
	}
	
	private boolean reserveCubeSet(Desease desease, Set<Cube> cubeSet) {
		return cubeCurrentReserve.get(desease).addAll(cubeSet);
	}
	
	public boolean removeAndReserveCube(City city, Desease desease) {
		return reserveCube(removeCube(city, desease));
	}
	
	private Cube removeCube(City city, Desease desease) {
		Cube cube = cityCubeMap.get(desease).iterator().next();
		cityCubeMap.get(desease).remove(cube);
		return cube;
	}
	
	private boolean reserveCube(Cube cube) {
		return cubeCurrentReserve.get(cube.getDesease()).add(cube);
	}
	
	public boolean equals(GameStatus gameStatus) {
		//todo
		return false;
	}

	public void nextStep(GameStep turnstart) {
		if(!isOver()) {
			setGameStep(gameStep);
		}
	}
	
	public boolean isOver() {
		if(gameStep == GameStep.lose || isWin()) {
			return true;
		}
		return false;
	}

	public boolean isWin() {
		if(gameStep == GameStep.win) {
			return true;
		}
		return false;
	}
}
