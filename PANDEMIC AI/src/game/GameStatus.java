package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameRules.GameStep;
import game.action.GameAction;
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.ResearchCenter;
import objects.card.Card;
import objects.card.Deck;
import objects.card.Hand;
import objects.card.PlayerCard;
import objects.card.PlayerDeck;
import objects.card.PropagationCard;
import objects.card.PropagationDeck;
import util.GameUtil;

public class GameStatus implements Serializable {

	//Final
	private static Logger logger = LogManager.getLogger(GameStatus.class.getName());
	
	private PropagationDeck propagationDeck;
	private PlayerDeck playerDeck;
	
	private int turnCounter;
	private int eclosionCounter;
	private Hand currentHand;
	private int currentActionCount;
	private GameRules.GameStep gameStep;
	
	private LinkedList<Hand> characterHandList;
	public Map<Character, Hand> characterHandMap;
	private Map<Character, City> characterPositionMap;
	private Map<City, Map<Desease, Set<Cube>>> cityCubeMap;
	private Map<City, ResearchCenter> cityResearchCenterMap;
	private Set<ResearchCenter> researchCenterCurrentReserve;
	private Map<Desease, Set<Cube>> cubeCurrentReserve;
	private Set<Desease> curedDeseaseSet;
	private Set<Desease> eradicatedDeseaseSet;

	private boolean simulation;
	
	//Rules material
	private Set<City> alreadyEcloded;
	private City eclosionStart;
	public LinkedList<GameAction> actionList;
	public LinkedList<GameAction> previousActionList;
	public int value;
	
	public GameStatus() {
		
	}
	
	public GameStatus(int numberOfPlayers, int difficulty, City start) {
		//MCTS material
		simulation = false;
		this.actionList = new LinkedList<GameAction>();
		this.previousActionList = new LinkedList<GameAction>();
		
		//Rules material
		this.alreadyEcloded = new HashSet<City>();
		
		
		//Building reserves and positioning maps
		cityCubeMap = new HashMap<City, Map<Desease, Set<Cube>>>();
		for(City city : GameProperties.map) {
			HashMap<Desease, Set<Cube>> deseaseCubeMap = new HashMap<Desease, Set<Cube>>();
			for(Desease desease : GameProperties.deseaseSet) {
				deseaseCubeMap.put(desease, new HashSet<Cube>());
			}
			cityCubeMap.put(city, deseaseCubeMap);
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
		curedDeseaseSet = new HashSet<Desease>();
		eradicatedDeseaseSet = new HashSet<Desease>();
		
		// Create players
		characterHandMap = new HashMap<Character, Hand>();
		characterPositionMap = new HashMap<Character, City>();
		characterHandList = new LinkedList<Hand>();
		for (int i = 0; i<numberOfPlayers; i++) {
			characterPositionMap.put(GameProperties.characterReserve.get(i), start);
			Hand hand = new Hand(GameProperties.characterReserve.get(i));
			characterHandList.add(hand);
			characterHandMap.put(GameProperties.characterReserve.get(i), hand);
		}
		
		//Game status
		turnCounter = 1;
		eclosionCounter=0;
		gameStep = GameRules.GameStep.play;
		currentHand = characterHandList.getFirst();
		currentActionCount =4;
		
		// Add research center on atlanta
		logger.info("Building a new Research Center in Atlanta.");
		addResearchCenter(start);
		
		// Create decks
		logger.info("Shuffling decks");
		propagationDeck = new PropagationDeck(); 	
		for(Card card : GameProperties.propagationCardReserve) {
			propagationDeck.addOnTop(card);
		}
		propagationDeck.shuffle();
		playerDeck = new PlayerDeck(); 
		for(Card card : GameProperties.playerCardReserve) {
			playerDeck.addOnTop(card);
		}
		
		logger.info("Dealing cards to each player.");
		playerDeck.shuffle();
		for(Hand hand : characterHandList) {
			for(int i = 0; i<6-numberOfPlayers; i++) {
				hand.addOnTop((PlayerCard) playerDeck.draw());
			}
		}
		
		// Add Epidemic cards and rebuild deck
		logger.info("Splitting player deck to add Epidemics.");
		List<Deck> deckList = playerDeck.split(difficulty);
		for(Deck subdeck : deckList) {
			subdeck.addOnTop(GameProperties.epidemicCardReserve);
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
		updateValue();
	}
	
	//Getters
	
	/*public List<Character> getCharacterList() {
		return this.characterList;
	}*/

	public PlayerDeck getPlayerDeck() {
		return this.playerDeck;
	}
	
	public PropagationDeck getPropagationDeck() {
		return this.propagationDeck;
	}
	
	public int getEclosionCounter() {
		return eclosionCounter;
	}
	
	public int getPropagationSpeed() {
		return GameProperties.getPropagationSpeed(this.playerDeck.getEpidemicCounter());
	}
	
	public GameRules.GameStep getGameStep() {
		return this.gameStep;
	}
	
	public Character getCurrentPlayer() {
		return this.currentHand.getCharacter();
	}
	
	public Hand getCurrentHand() {
		return this.currentHand;
	}
	
	public City getCurrentCharacterPosition() {
		return this.characterPositionMap.get(currentHand.getCharacter());
	}

	public Set<ResearchCenter> getResearchCenterReserve() {
		return this.researchCenterCurrentReserve;
	}
	
	public Map<City, ResearchCenter> getCityResearchCenterMap() {
		return this.cityResearchCenterMap;
	}
	
	public Hand getPlayerHand(Character character) {
		return this.characterHandMap.get(character);
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

	//Setters
	public void nextPlayer() {
		currentHand = characterHandList.get((characterHandList.indexOf(currentHand)+1) % characterHandList.size());
		currentActionCount = GameProperties.actionCount;
	}
	
	public void increaseEclosionCounter() {
		eclosionCounter++;
	}
	
	public void increaseEpidemicCounter() {
		this.playerDeck.increaseEpidemicCounter();
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
	
	public Map<Character, City> getCharacterPositionMap() {
		return this.characterPositionMap;
	}
	
	public City getCharacterPosition(Character character) {
		return this.characterPositionMap.get(character);
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
	
	public boolean addToReserve(Object obj) {
		if(obj instanceof ResearchCenter) {
			return researchCenterCurrentReserve.add((ResearchCenter) obj);
		} else if(obj instanceof Cube) {
			return cubeCurrentReserve.get(((Cube)obj).getDesease()).add((Cube)obj);
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
	
	public boolean removeResearchCenter(City city) {
		if(cityResearchCenterMap.get(city) != null) {
			addToReserve(cityResearchCenterMap.get(city));
			cityResearchCenterMap.remove(city);
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
			cityCubeMap.get(city).get(desease).add(cube);
			return true;
		} else {
			return false;
		}
	}

	public boolean removeAndReserveCubeSet(City city, Desease desease) {
		return reserveCubeSet(desease, removeCubeSet(city, desease));
	}
	
	private Set<Cube> removeCubeSet(City city, Desease desease) {
		Set<Cube> cubeSet = cityCubeMap.get(city).get(desease);
		cityCubeMap.get(city).get(desease).removeAll(cubeSet);
		return cubeSet;
	}
	
	private boolean reserveCubeSet(Desease desease, Set<Cube> cubeSet) {
		return cubeCurrentReserve.get(desease).addAll(cubeSet);
	}
	
	public boolean removeAndReserveCube(City city, Desease desease) {
		return reserveCube(removeCube(city, desease));
	}
	
	private Cube removeCube(City city, Desease desease) {
		Cube cube = cityCubeMap.get(city).get(desease).iterator().next();
		cityCubeMap.get(city).get(desease).remove(cube);
		return cube;
	}
	
	private boolean reserveCube(Cube cube) {
		return cubeCurrentReserve.get(cube.getDesease()).add(cube);
	}
	
	public boolean equals(GameStatus gameStatus) {
		//todo
		return false;
	}

	public void nextStep(GameStep nextStep) {
		if(!isOver()) {
			setGameStep(nextStep);
		}
	}
	
	public boolean setCharacterPosition(Character currentPlayer, City destination) {
		this.characterPositionMap.put(currentPlayer, destination);
		return true;
	}

	public Hand getCharacterHand(Character character) {
		return this.characterHandMap.get(character);
	}

	public List<Hand> getCharacterHandList() {
		return this.characterHandList;
	}

	public Set<Cube> getCityCubeSet(City city, Desease desease) {
		return cityCubeMap.get(city).get(desease);
	}

	public boolean hasResearchCenter(City city) {
		return this.cityResearchCenterMap.get(city) != null;
	}

	public Set<Desease> getCuredDeseaseSet() {
		return this.curedDeseaseSet;
	}

	public Set<Desease> getEradicatedDeseaseSet() {
		return this.eradicatedDeseaseSet;
	}

	public boolean addCuredDesease(Desease desease) {
		return this.curedDeseaseSet.add(desease);
	}
	

	public boolean removeCuredDesease(Desease desease) {
		return this.curedDeseaseSet.remove(desease);
	}

	public boolean isCubeReserveFull(Desease desease) {
		return this.cubeCurrentReserve.get(desease).size() == GameProperties.cubeReserve.get(desease).size();
		
	}
	
	public Map<Desease, Set<Cube>> getCubeCurrentReserve(Desease desease) {
		return this.cubeCurrentReserve;
		
	}

	public boolean addEradicatedDesease(Desease desease) {
		return this.eradicatedDeseaseSet.add(desease);
	}
	
	public boolean removeEradicatedDesease(Desease desease) {
		return this.eradicatedDeseaseSet.remove(desease);
	}


	public int getCurrentActionCount() {
		return this.currentActionCount;
	}

	public void decreaseCurrentActionCount(int i) {
		this.currentActionCount = currentActionCount - i;
	}
	
	public void increaseCurrentActionCount(int actionCost) {
		this.currentActionCount += actionCost;
	}
	
	public GameStatus clone() {
		GameStatus gameStatus = new GameStatus();
		gameStatus.simulation = true;
		gameStatus.actionList = new LinkedList<GameAction>(this.actionList);
		gameStatus.previousActionList = new LinkedList<GameAction>(this.previousActionList);
		gameStatus.value= this.value;
		
		gameStatus.alreadyEcloded = new HashSet<City>();
		gameStatus.turnCounter = this.turnCounter;
		gameStatus.eclosionCounter = this.eclosionCounter;
		gameStatus.currentActionCount = this.currentActionCount;
		gameStatus.gameStep = this.gameStep;
		
		
		gameStatus.propagationDeck = propagationDeck.clone();
		gameStatus.playerDeck = playerDeck.clone();
		
		gameStatus.characterHandList = new LinkedList<Hand>();	
		gameStatus.characterHandMap = new HashMap<Character, Hand>();
		for(Hand hand : this.characterHandList) {
			Hand handClone = (Hand) hand.clone();
			gameStatus.characterHandList.add(handClone);
			gameStatus.characterHandMap.put(handClone.getCharacter(), handClone);
			if(hand == currentHand) {
				gameStatus.currentHand = handClone;
			}
		}
		
		gameStatus.characterPositionMap = new HashMap<Character, City>();
		gameStatus.characterPositionMap.putAll(this.characterPositionMap);
		
		gameStatus.cityCubeMap = new HashMap<City, Map<Desease, Set<Cube>>>();
		for(City city : this.cityCubeMap.keySet()) {
			HashMap<Desease, Set<Cube>> deseaseCubeMap = new HashMap<Desease, Set<Cube>>();
			for(Desease desease : this.cityCubeMap.get(city).keySet()) {
				HashSet<Cube> cubeSet = new HashSet<Cube>();
				cubeSet.addAll(this.cityCubeMap.get(city).get(desease));
				deseaseCubeMap.put(desease, cubeSet);
			}
			gameStatus.cityCubeMap.put(city, deseaseCubeMap);
		}
		
		gameStatus.cityResearchCenterMap = new HashMap<City, ResearchCenter>();
		gameStatus.cityResearchCenterMap.putAll(this.cityResearchCenterMap);

		gameStatus.researchCenterCurrentReserve = new HashSet<ResearchCenter>();
		gameStatus.researchCenterCurrentReserve.addAll(this.researchCenterCurrentReserve);
		
		gameStatus.cubeCurrentReserve = new HashMap<Desease, Set<Cube>>();
		for(Desease desease : this.cubeCurrentReserve.keySet()) {
			HashSet<Cube> cubeSet = new HashSet<Cube>();
			cubeSet.addAll(this.cubeCurrentReserve.get(desease));
			gameStatus.cubeCurrentReserve.put(desease, cubeSet);
		}
		
		gameStatus.curedDeseaseSet = new HashSet<Desease>();
		gameStatus.curedDeseaseSet.addAll(this.curedDeseaseSet);
		
		gameStatus.eradicatedDeseaseSet = new HashSet<Desease>();
		gameStatus.eradicatedDeseaseSet.addAll(this.eradicatedDeseaseSet);
		return gameStatus;
	}
	
	public LightGameStatus lightClone() {
		return new LightGameStatus(this);
	}

	public boolean isSimulation() {
		return this.simulation;
	}

	public Set<City> getAlreadyEclodedCities() {
		return this.alreadyEcloded;
	}
	
	public void addEclodedCity(City city) {
		this.alreadyEcloded.add(city);
	}
	
	public void addEclodedCity(Collection<? extends City> citySet) {
		this.alreadyEcloded.addAll(citySet);
	}
	
	public void clearEclodedCity() {
		this.alreadyEcloded.clear();
	}

	public void setEclosionStart(City city) {
		this.eclosionStart = city;
	}

	public City getEclosionStart() {
		return this.eclosionStart;
	}

	public int getEpidemicCounter() {
		return this.playerDeck.getEpidemicCounter();
	}

	public Set<ResearchCenter> getResearchCenterCurrentReserve() {
		return this.researchCenterCurrentReserve;
	}
	
	public int updateValue() {
		this.value = 0;
		this.value += (GameProperties.maxEclosionCounter - this.getEclosionCounter()) * 500;
		this.value += 300*GameProperties.map.size();
		for(Card propagationCard : this.propagationDeck.getDiscardPile().getCardDeck()) {
			switch(this.getCityCubeSet(((PropagationCard)propagationCard).getCity(), ((PropagationCard)propagationCard).getCity().getDesease()).size()) {
			case 0:
				break;
			case 1:
				this.value -=40;
				break;
			case 2:
				this.value -=80;
				break;
			case 3 :
				this.value -=160;
				break;
			default: 
				this.value -=300;
				break;
			}
		}
		
		for(LinkedList<PropagationCard> memory : this.propagationDeck.getMemories()) {
			for(PropagationCard memoryCard : memory) {
				switch(this.getCityCubeSet(((PropagationCard)memoryCard).getCity(), ((PropagationCard)memoryCard).getCity().getDesease()).size()) {
				case 0:
					break;
				case 1:
					this.value -=50;
					break;
				case 2:
					this.value -=100;
					break;
				case 3 :
					this.value -=300;
					break;
				default: 
					this.value -=300;
					break;
				}
			}
		}
		
		this.value +=1000;
		for(Desease desease : GameProperties.deseaseSet) {
			this.value -=Math.max(this.getCubeCurrentReserve(desease).size()*50,250);
		}
		this.value +=1000;
		int neighbourResearchCenter = 0;
		for(City city : GameProperties.map) {
			if(this.cityResearchCenterMap.get(city) != null) {
				neighbourResearchCenter = 0;
				for(City neighbour : city.getNeighbourSet()) {
					if(this.cityResearchCenterMap.get(city) != null) {
						neighbourResearchCenter++;
					}
				}
				this.value+=200/(neighbourResearchCenter+1);
			}
		}
		this.value -=this.getResearchCenterCurrentReserve().size()*200;
		
		this.value += this.getCuredDeseaseSet().size()*1000;
		this.value += this.getEradicatedDeseaseSet().size()*250;
		
		for(Desease desease : GameProperties.deseaseSet) {
			int max = 0;
			if(!this.getCuredDeseaseSet().contains(desease)) {
				for(Hand hand : this.getCharacterHandList()) {
					int current = hand.getCardDeck().stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(desease)).collect(Collectors.toSet()).size();
					max = current>max ? current:max;
				}
			}
			this.value += 300*max;
		}
		this.value+= this.getCurrentCharacterPositionValue()*10/10;
		return this.value;
	}

	private double getCurrentCharacterPositionValue() {
		double value =0;
		for(City city : this.getCurrentCharacterPosition().getNeighbourSet()) {
			if(this.getCityCubeSet(city, city.getDesease()).size()>0) {
				value++;
			} else {
				for(City neighbour : city.getNeighbourSet()) {
					if(this.getCityCubeSet(neighbour, neighbour.getDesease()).size()>0) {
						value+=0.5;
					} else {
						for(City neighbour2 : neighbour.getNeighbourSet()) {
							if(this.getCityCubeSet(neighbour, neighbour.getDesease()).size()>0) {
								value+=0.2;
							}
						}
					}
				}
			}
			
		}
		return 0;
	}

	public void addToActionList(GameAction gameAction) {
		this.previousActionList.add(gameAction);
	}

	public boolean removeFromActionList(GameAction gameAction) {
		if(this.previousActionList.getLast() == gameAction) {
			return this.previousActionList.removeLast() != null;
		}
		return false;
	}

	public boolean rollBack() {
		return this.previousActionList.getLast().cancel(this);
	}
	


}
