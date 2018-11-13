package gameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;

import game.action.Build;
import game.action.CharterFlight;
import game.action.Cure;
import game.action.DirectFlight;
import game.action.Discard;
import game.action.Drive;
import game.action.GameAction;
import game.action.ShareKnowledge;
import game.action.ShuttleFlight;
import game.action.Treat;
import gameStatus.GameStatus.GameStep;
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.card.Deck;
import objects.card.EpidemicCard;
import objects.card.PlayerCard;
import objects.card.PropagationCard;
import util.GameUtil;

public class Game extends Observable{
	
	private static Logger logger = LogManager.getLogger(Game.class.getName());
	private static Map<String, Object> notifyMap;
	
	private GameStatus gameStatus;
	
	private int maxEclosionCounter = 7;
	private Set<City> eclosionCities;
	private City chainEclosion;
	
	private Set<Class<GameAction>> actionTypeSet;
	
	
	
	
	public Game(int numberOfPlayers, int difficulty) {
		gameStatus = new GameStatus(numberOfPlayers);
		eclosionCities = new HashSet<City>();
		prepareNewGame(numberOfPlayers,difficulty);
	}
	
	public void prepareNewGame(int numberOfPlayers, int difficulty) {
		// Deal cards to players
		logger.info("Dealing two cards to each player.");
		gameStatus.getPlayerDeck().shuffle();
		for(Character player : gameStatus.getCharacterList()) {
			for(int i = 0; i<6-numberOfPlayers; i++) {
				player.hand((PlayerCard) gameStatus.getPlayerDeck().draw());
			}
		}
		
		// Add Epidemic cards and rebuild deck
		logger.info("Splitting player deck.");
		List<Deck> deckList = gameStatus.getPlayerDeck().split(difficulty);
		for(Deck subdeck : deckList) {
			subdeck.addOnTop(new EpidemicCard());
			subdeck.shuffle();
			gameStatus.getPlayerDeck().addOnTop(subdeck);
		}
		
		// Add research center on atlanta
		logger.info("Building a new Research Center in Atlanta.");
		gameStatus.getReserve().getResearchCenter().build(GameUtil.getCity(this, "Atlanta"));
		
		// Draw 9 propagation card
		for (int i = 3; i>0; i--) {
			for (int k = 0; k<3; k++) {
				PropagationCard card = (PropagationCard) gameStatus.getPropagationDeck().draw();
				this.infect(card.getCity(), i);
				gameStatus.getPropagationDeck().discard(card);
			}
		}
	}
	
	
	
	
	private void infect(City city) {
		infect(city, 1, city.getDesease());
	}
	
	private void infect(City city, int strength) {
		infect(city, strength, city.getDesease());
	}
	
	private void infect(City city, int strength, Desease desease) {
		//Check if eclosion
		logger.info("The "+desease.getName()+" desease spreads in "+city.getName()+".");
		boolean eclosion = false;
		int cubeCounter = 0;
		while(cubeCounter<strength && !eclosion) {
			Set<Cube> cubeSet = city.getCubeSet(desease);
			if(cubeSet.size() == 3) {
				eclosion = true;
				eclosion(city, desease);
			} else {
				Cube cube = gameStatus.getReserve().getCube(desease);
				if(cube != null) {
					city.addCube(cube);
				} else {
					lose();
				}
			}
			cubeCounter++;
		}
	}
	
	private void eclosion(City city, Desease desease) {
		logger.info("Eclosion in "+city.getName());
		gameStatus.increaseEclosionCounter();
		if(gameStatus.getEclosionCounter() > maxEclosionCounter) {
			lose();
		} else {
			if(chainEclosion == null) {
				chainEclosion = city;
			}
			eclosionCities.add(city);
			for(DefaultEdge edge : gameStatus.getMap().outgoingEdgesOf(city)) {
				City target = gameStatus.getMap().getEdgeTarget(edge);
				if(chainEclosion == null || !eclosionCities.contains(target)) {
					logger.info("Desease is spreading "+city.getName());
					infect(target, 1, desease);
				}
			}
		}
		if(chainEclosion == city) {
			eclosionCities.clear();
		}
	}
	
	public void drawEndTurn() {
		//current Player draws
				for(int i = 0; i<2; i++) {
					setChanged();
					PlayerCard card = (PlayerCard) gameStatus.getPlayerDeck().draw();
					if(card == null) {
						lose();
					}
					if(card instanceof EpidemicCard) {
						//do Epidemy
						logger.info("New Epidemic... The world will soon come to an end !");
						PropagationCard infectedCard = (PropagationCard) gameStatus.getPropagationDeck().drawBottomCard();
						infect(infectedCard.getCity(), 3);
						gameStatus.increaseEpidemicCounter();
						if(gameStatus.getEpidemicCounter() == 3 || gameStatus.getEpidemicCounter() == 5) {
							gameStatus.increasePropagationSpeed();
						}
						gameStatus.getPropagationDeck().discard(infectedCard);
						Deck propagationDiscardPile = gameStatus.getPropagationDeck().getDiscardPile();
						propagationDiscardPile.shuffle();
						gameStatus.getPropagationDeck().addOnTop(propagationDiscardPile);
					} else {
						gameStatus.getCurrentPlayer().hand(card);
					}
				}
	}
	
	public void propagationEndTurn() {
		//propagation
		for(int i = 0; i<gameStatus.getPropagationSpeed(); i++) {
			PropagationCard card = (PropagationCard) gameStatus.getPropagationDeck().draw();
			this.infect(card.getCity());
			gameStatus.getPropagationDeck().discard(card);
		}
	}
	
	public void endTurn() {
		logger.info(gameStatus.getCurrentPlayer().getName()+" ends his turn.");
		drawEndTurn();
		propagationEndTurn();
	}

	
	private void lose() {
		gameStatus.setGameStep(GameStep.lose);
	}
	
	public boolean isOver() {
		return gameStatus.getGameStep() == GameStep.lose || gameStatus.getGameStep() == GameStep.win;
	}
	
	public boolean isWin() {
		return gameStatus.getGameStep() == GameStep.win;
	}
	
	

	

	public Character nextPlayer() {
		gameStatus.increaseCurrentPlayerIndex();
		logger.info(gameStatus.getCurrentPlayer().getName()+" starts a new turn.");
		gameStatus.getCurrentPlayer().newTurn();
		return gameStatus.getCurrentPlayer();
	}

	
	
	public void start() {
		while(!this.isOver()) {
			Character currentCharacter = this.nextPlayer();
			logger.info(currentCharacter.getName()+" starts his turn.");
			while(currentCharacter.canPlay()) {
				setChanged();
				notifyMap = new HashMap<String, Object>();
				notifyMap.put("action", currentCharacter);
				notifyObservers(notifyMap);
			}
			
			this.endTurn();
		}
		if(this.isWin()) {
			logger.info("You won.");
		} else {
			logger.info("You lost. Haha.");	
		}
		return;
	}

	public void updateStatus() {
		//TODO refacotring previous code to use a step and status manager
		switch(gameStatus.getGameStep()) {
			case play:
				if(!gameStatus.getCurrentPlayer().canPlay()) {
					gameStatus.setGameStep(GameStep.draw);
				}
			break;
			case draw:
				if(Discard.getValidGameActionSet(this, gameStatus.getCurrentPlayer()).size() == 0) {
					gameStatus.setGameStep(GameStep.propagate);
				}
			break;
			case propagate:
				propagationEndTurn();
				nextPlayer();
				gameStatus.setGameStep(GameStep.play);
			break;
			default:
				break;
		}
	}
	
	public ArrayList<Game> getAllPossibleNextGames() {
		ArrayList<Game> resultList = new ArrayList<Game>();
		try {
			this.updateStatus();
			Set<GameAction> actionSet = this.getAllPossibleActions();
			
			if(actionSet == null || actionSet.size() ==0) {
				return resultList;
			} else {
				for(GameAction action : actionSet) {
					Game nodeGame = (Game) this.clone();
					action.perform();
					resultList.add(nodeGame);
					resultList.addAll(nodeGame.getAllPossibleNextGames());	
		
				}
			}
			
			return resultList;
		} catch (CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
	}
	
	private Set<GameAction> getAllPossibleActions() {
		Set<GameAction> actionSet = new HashSet<GameAction>();
		for(Character character : gameStatus.getCharacterList()) {
			actionSet.addAll(Discard.getValidGameActionSet(this, character));
			if(actionSet.size() >= 0 ) {
				return actionSet;
			}
		}
		if(!gameStatus.getCurrentPlayer().canPlay()) {
			return null;
		}
		for(Class actionClass: this.actionTypeSet) {
			if (actionClass.isAssignableFrom(Drive.class)) {
				actionSet.addAll(Drive.getValidGameActionSet(this));
			} else if (actionClass.isAssignableFrom(DirectFlight.class)) {
				actionSet.addAll(DirectFlight.getValidGameActionSet(this));
			} else if (actionClass.isAssignableFrom(CharterFlight.class)) {
				actionSet.addAll(CharterFlight.getValidGameActionSet(this));
			} else if (actionClass.isAssignableFrom(ShuttleFlight.class)) {
				actionSet.addAll(ShuttleFlight.getValidGameActionSet(this));
			} else if (actionClass.isAssignableFrom(Treat.class)) {
				actionSet.addAll(Treat.getValidGameActionSet(this));
			} else if (actionClass.isAssignableFrom(ShareKnowledge.class)) {
				actionSet.addAll(ShareKnowledge.getValidGameActionSet(this));
			} else if (actionClass.isAssignableFrom(Cure.class)) {
				actionSet.addAll(Cure.getValidGameActionSet(this));
			}  else if (actionClass.isAssignableFrom(Build.class)) {
				actionSet.addAll(Build.getValidGameActionSet(this));
			}
		}
		return actionSet;
	}

	public boolean equals(Game game) {
		return false;
	}

	public GameStatus getGameStatus() {
		// TODO Auto-generated method stub
		return gameStatus;
	}

	
}
