package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;

import game.GameRules.GameStep;
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
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.ResearchCenter;
import objects.card.Deck;
import objects.card.EpidemicCard;
import objects.card.PlayerCard;
import objects.card.PropagationCard;

public class GameRules {
	
	public enum GameStep {
		turnStart,
		play,
		draw,
		propagate,
		win,
		lose, 
		discard,
		event
	}

	private static Logger logger = LogManager.getLogger(GameRules.class.getName());

	public static void infect(GameStatus gameStatus, City city) {
		infect(gameStatus, city, 1, city.getDesease(), null);
	}
	
	public static void infect(GameStatus gameStatus, City city, int strength) {
		infect(gameStatus, city, strength, city.getDesease(), null);
	}
	
	public static void infect(GameStatus gameStatus, City city, int strength, Desease desease, Set<City> alreadyEcloded) {
		//Check if eclosion
		logger.info("The "+desease.getName()+" desease spreads in "+city.getName()+".");
		boolean eclosion = false;
		int cubeCounter = 0;
		while(cubeCounter<strength && !eclosion) {
			Set<Cube> cubeSet = city.getCubeSet(desease);
			if(cubeSet.size() == 3) {
				eclosion = true;
				eclosion(gameStatus, city, desease, alreadyEcloded);
			} else {
				if(!gameStatus.addCube(city, desease)) {
					lose(gameStatus);
				} 
			}
			cubeCounter++;
		}
	}

	
	public static void eclosion(GameStatus gameStatus, City city, Desease desease, Set<City> alreadyEcloded) {
		logger.info("Eclosion in "+city.getName());
		gameStatus.increaseEclosionCounter();
		if(gameStatus.getEclosionCounter() > GameProperties.maxEclosionCounter) {
			lose(gameStatus);
		} else {
			HashSet<City> eclosionCities = new HashSet<City>();
			eclosionCities.addAll(alreadyEcloded);
			eclosionCities.add(city);
			for(DefaultEdge edge : GameProperties.map.outgoingEdgesOf(city)) {
				City target = GameProperties.map.getEdgeTarget(edge);
				if(!eclosionCities.contains(target)) {
					infect(gameStatus, target, 1, desease, alreadyEcloded);
				}
			}
		}

	}
	

	public static Character nextPlayer(GameStatus gameStatus) {
		gameStatus.increaseTurnCounter();
		gameStatus.nextPlayer();
		logger.info(gameStatus.getCurrentPlayer().getName()+" starts a new turn.");
		gameStatus.getCurrentPlayer().newTurn();
		return gameStatus.getCurrentPlayer();
	}
	
	public static void drawEndTurn(GameStatus gameStatus) {
		//current Player draws
		for(int i = 0; i<2; i++) {
			PlayerCard card = (PlayerCard) gameStatus.getPlayerDeck().draw();
			if(card == null) {
				lose(gameStatus);
			} else if(card instanceof EpidemicCard) {
				//do Epidemy
				logger.info("New Epidemic... The world will soon come to an end !");
				PropagationCard infectedCard = (PropagationCard) gameStatus.getPropagationDeck().drawBottomCard();
				gameStatus.getPropagationDeck().discard(infectedCard);
				infect(gameStatus, infectedCard.getCity(), 3);
				gameStatus.increaseEpidemicCounter();
				Deck propagationDiscardPile = gameStatus.getPropagationDeck().getDiscardPile();
				propagationDiscardPile.shuffle();
				gameStatus.getPropagationDeck().addOnTop(propagationDiscardPile);
			} else {
				gameStatus.getCurrentPlayer().hand(card);
			}
		}
	}
	
	public static void propagationEndTurn(GameStatus gameStatus) {
		//propagation
		for(int i = 0; i<gameStatus.getPropagationSpeed(); i++) {
			PropagationCard card = (PropagationCard) gameStatus.getPropagationDeck().draw();
			infect(gameStatus, card.getCity());
			gameStatus.getPropagationDeck().discard(card);
		}
	}
	
	public static void lose(GameStatus gameStatus) {
		gameStatus.setGameStep(GameRules.GameStep.lose);
	}
	
	public static Set<GameAction> getAllPossibleActions(GameStatus gameStatus) {
		Set<GameAction> actionSet = new HashSet<GameAction>();
		for(Character character : gameStatus.getCharacterList()) {
			actionSet.addAll(Discard.getValidGameActionSet(gameStatus, character));
		}
		if(actionSet.size() > 0 ) {
			return actionSet;
		}
		if(!gameStatus.getCurrentPlayer().canPlay()) {
			return null;
		}
		for(Class actionClass: GameProperties.actionTypeSet) {
			if (actionClass.isAssignableFrom(Drive.class)) {
				actionSet.addAll(Drive.getValidGameActionSet(gameStatus));
			} else if (actionClass.isAssignableFrom(DirectFlight.class)) {
				actionSet.addAll(DirectFlight.getValidGameActionSet(gameStatus));
			} else if (actionClass.isAssignableFrom(CharterFlight.class)) {
				actionSet.addAll(CharterFlight.getValidGameActionSet(gameStatus));
			} else if (actionClass.isAssignableFrom(ShuttleFlight.class)) {
				actionSet.addAll(ShuttleFlight.getValidGameActionSet(gameStatus));
			} else if (actionClass.isAssignableFrom(Treat.class)) {
				actionSet.addAll(Treat.getValidGameActionSet(gameStatus));
			} else if (actionClass.isAssignableFrom(ShareKnowledge.class)) {
				actionSet.addAll(ShareKnowledge.getValidGameActionSet(gameStatus));
			} else if (actionClass.isAssignableFrom(Cure.class)) {
				actionSet.addAll(Cure.getValidGameActionSet(gameStatus));
			}  else if (actionClass.isAssignableFrom(Build.class)) {
				actionSet.addAll(Build.getValidGameActionSet(gameStatus));
			}
		}
		return actionSet;
	}
	
	
	public static void updateStatus(GameStatus gameStatus) {
		//TODO refactoring previous code to use a step and status manager
		switch(gameStatus.getGameStep()) {
			case turnStart:
				GameRules.nextPlayer(gameStatus);
				gameStatus.nextStep(GameRules.GameStep.play);
			break;
			case play:
				if(!gameStatus.getCurrentPlayer().canPlay()) {
					gameStatus.nextStep(GameRules.GameStep.draw);
				}
			break;
			case draw:
				GameRules.drawEndTurn(gameStatus);
				if(Discard.getValidGameActionSet(gameStatus, gameStatus.getCurrentPlayer()).size() == 0) {
					gameStatus.nextStep(GameRules.GameStep.propagate);
				}
			break;
			case propagate:
				GameRules.propagationEndTurn(gameStatus);
				gameStatus.nextStep(GameRules.GameStep.turnStart);
			break;
			default:
				break;
		}
	}

	public static void playStatus(GameEngine gameEngine) {
		//TODO refactoring previous code to use a step and status manager
		switch(gameEngine.getGameStatus().getGameStep()) {
			case play:
				gameEngine.notifyGameStep(GameStep.play);
			break;
			case draw:
				gameEngine.notifyGameStep(GameStep.discard);
			break;
			default:
				break;
		}
	}
	
	

	public ArrayList<GameStatus> getAllPossibleNextGames(GameStatus gameStatus) {
		if(gameStatus.isOver()) {
			return new ArrayList<GameStatus>();
		}
		ArrayList<GameStatus> resultList = new ArrayList<GameStatus>();
		updateStatus(gameStatus);
		Set<GameAction> actionSet = GameRules.getAllPossibleActions(gameStatus);
		
		if(actionSet == null || actionSet.size() ==0) {
			//No action possible
			resultList.add((Game) this.clone());
			return resultList;
		} else {
			for(GameAction action : actionSet) {
				
				//
				possibleActionList.add(action);
				action.perform(gameStatus);
				Game nodeGame = (Game) this.clone();
				resultList.add(nodeGame);
				resultList.addAll(nodeGame.getAllPossibleNextGames());	
	
			}
		}
		return resultList;		
	}
}
