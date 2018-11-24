package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.mcts.MCTSNode;
import game.action.Build;
import game.action.CharterFlight;
import game.action.Cure;
import game.action.DirectFlight;
import game.action.Discard;
import game.action.Drive;
import game.action.GameAction;
import game.action.Pass;
import game.action.ShareKnowledge;
import game.action.ShuttleFlight;
import game.action.Treat;
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.card.CityCard;
import objects.card.EpidemicCard;
import objects.card.PlayerCard;
import objects.card.PropagationCard;
import util.GameUtil;

public class GameRules {

	public enum GameStep {
		turnStart, play, draw, propagate, win, lose, discard, event
	}

	private static Logger logger = LogManager.getLogger(GameRules.class.getName());

	public static void infect(GameStatus gameStatus, City city) {
		infect(gameStatus, city, 1, city.getDesease());
	}

	public static void infect(GameStatus gameStatus, City city, int strength) {
		infect(gameStatus, city, strength, city.getDesease());
	}

	public static void infect(GameStatus gameStatus, City city, int strength, Desease desease) {
		// Check if eradicated
		if (!isEradicated(gameStatus, desease)) {
			
			int cubeCounter = 0;
			while (cubeCounter < strength) {
				Set<Cube> cubeSet = gameStatus.getCityCubeSet(city, desease);
				if (cubeSet.size() == 3) {
					eclosion(gameStatus, city, desease);
				} else {
					GameUtil.log(gameStatus, logger,
							"The " + desease.getName() + " desease spreads in " + city.getName() + ".");
					if (!gameStatus.addCube(city, desease)) {
						GameUtil.log(gameStatus, logger, "No more "+desease.getName()+" cubes to add.");
						lose(gameStatus);
					}
				}
				cubeCounter++;
			}
		}
	}

	public static void eclosion(GameStatus gameStatus, City city, Desease desease) {
		if(gameStatus.getAlreadyEclodedCities().contains(city)) {
			//do nothing
		} else {
			GameUtil.log(gameStatus, logger, "Eclosion in " + city.getName() + ".");
			gameStatus.increaseEclosionCounter();
			if (gameStatus.getEclosionCounter() > GameProperties.maxEclosionCounter) {
				GameUtil.log(gameStatus, logger, "Too many eclosions !");
				lose(gameStatus);
			} else {
				if(gameStatus.getAlreadyEclodedCities().size() == 0) {
					gameStatus.setEclosionStart(city);
				}
				gameStatus.addEclodedCity(city);
				for (City target : city.getNeighbourSet()) {
					infect(gameStatus, target, 1, desease);
				}
			}
		}
		if(gameStatus.getEclosionStart() == city) {
			gameStatus.setEclosionStart(null);
			gameStatus.clearEclodedCity();
		}
		
	}

	public static Character nextPlayer(GameStatus gameStatus) {
		gameStatus.increaseTurnCounter();
		gameStatus.nextPlayer();
		GameUtil.log(gameStatus, logger, "Turn "+gameStatus.getTurnCounter());
		GameUtil.log(gameStatus, logger, gameStatus.getCurrentPlayer().getName() + " starts a new turn in "+gameStatus.getCurrentCharacterPosition().getName()+".");
		GameUtil.log(gameStatus, logger, "There are "+gameStatus.getCityCubeSet(gameStatus.getCurrentCharacterPosition(), gameStatus.getCurrentCharacterPosition().getDesease()).size()+" cubes.");
		GameUtil.log(gameStatus, logger, "Player hand "+gameStatus.getCurrentHand().toString()+".");
		return gameStatus.getCurrentPlayer();
	}

	public static void drawEndTurn(GameStatus gameStatus) {
		// current Player draws
		GameUtil.log(gameStatus, logger, gameStatus.getCurrentPlayer().getName() + " draws.");
		for (int i = 0; i < 2; i++) {
			PlayerCard card = (PlayerCard) gameStatus.getPlayerDeck().draw();
			if (card == null) {
				lose(gameStatus);
				GameUtil.log(gameStatus, logger, "No more player card.");
				//gameStatus.setGameStep(GameStep.win);
			} else if (card instanceof EpidemicCard) {
				// do Epidemy
				GameUtil.log(gameStatus, logger, gameStatus.getCurrentPlayer().getName() + " draws the "
						+ (gameStatus.getEpidemicCounter()+1) + "th Epidemic.");
				PropagationCard infectedCard = (PropagationCard) gameStatus.getPropagationDeck().drawBottomCard();
				if (infectedCard == null) {
					GameUtil.log(gameStatus, logger, "No propagation card");
					lose(gameStatus);
				} else {
					gameStatus.getPropagationDeck().discard(infectedCard);
					infect(gameStatus, infectedCard.getCity(), 3);
					gameStatus.increaseEpidemicCounter();
					gameStatus.getPropagationDeck().addOnTop(gameStatus.getPropagationDeck().getDiscardPile().shuffle());
				}
			} else {
				gameStatus.getCurrentHand().addOnTop(card);
			}
		}
	}

	public static void propagationEndTurn(GameStatus gameStatus) {
		// propagation
		GameUtil.log(gameStatus, logger, "Propagation");
		for (int i = 0; i < gameStatus.getPropagationSpeed(); i++) {
			PropagationCard card = (PropagationCard) gameStatus.getPropagationDeck().draw();
			if (card == null) {
				GameUtil.log(gameStatus, logger, "No propagation card");
				lose(gameStatus);
			} else {
				infect(gameStatus, card.getCity());
				gameStatus.getPropagationDeck().discard(card);
			}
		}
	}

	public static void lose(GameStatus gameStatus) {
		gameStatus.setGameStep(GameRules.GameStep.lose);
	}

	public static boolean giveCard(GameStatus gameStatus, Character character1, Character character2, CityCard card) {
		gameStatus.getPlayerHand(character1).removeCard(card);
		gameStatus.getPlayerHand(character2).addOnTop(card);
		return true;
	}
	
	public static List<GameStatus> getAllPossibleGameStatus(GameStatus gameStatus) {
		List<GameStatus> resultList = new ArrayList<GameStatus>();
		if(canPlay(gameStatus) || mustDiscard(gameStatus)) {
			for(GameStatus clone : getNextGameStatus(gameStatus)) {
				resultList.addAll(getAllPossibleGameStatus(clone));
			}
		} else {
			resultList.add(gameStatus);
		}
		return resultList;
	}
	


	public static List<GameStatus> getNextGameStatus(GameStatus gameStatus) {
		List<GameStatus> gameStatusList = new ArrayList<GameStatus>();
		getAllPossibleActions(gameStatus);
		for(GameAction gameAction : gameStatus.actionList) {
			GameStatus clone = gameStatus.clone();
			clone.previousActionList.addLast(gameAction);
			gameAction.perform(clone);
			gameStatusList.add(clone);
		}
		return gameStatusList;
	}
	
	public static List<GameAction> getAllPossibleActions(GameStatus gameStatus) {
		gameStatus.actionList.clear();
		if (gameStatus.getGameStep() == GameStep.discard) {
			for (Character character : gameStatus.getCharacterPositionMap().keySet()) {
				gameStatus.actionList.addAll(Discard.getValidGameActionSet(gameStatus, character));
			}
		} else if (gameStatus.getGameStep() == GameStep.play) {
			gameStatus.actionList.addAll(Drive.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(DirectFlight.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(CharterFlight.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(ShuttleFlight.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(Treat.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(ShareKnowledge.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(Cure.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(Build.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(Pass.getValidGameActionSet(gameStatus));
				
		}
		return gameStatus.actionList;
	}

	public static void updateStatus(GameStatus gameStatus) {
		// TODO refactoring previous code to use a step and status manager
		switch (gameStatus.getGameStep()) {
		case play:
			if (!canPlay(gameStatus)) {
				gameStatus.nextStep(GameRules.GameStep.draw);
			}
			break;
		case draw:
			GameRules.drawEndTurn(gameStatus);
			gameStatus.nextStep(GameRules.GameStep.discard);
			break;
		case discard:
			if (Discard.getValidGameActionSet(gameStatus, gameStatus.getCurrentPlayer()).size() == 0) {
				gameStatus.nextStep(GameRules.GameStep.propagate);
			}
			break;
		case propagate:
			GameRules.propagationEndTurn(gameStatus);
			GameRules.nextPlayer(gameStatus);
			gameStatus.nextStep(GameRules.GameStep.play);
			break;
		default:
			break;
		}
	}

	public static void playStatus(GameEngine gameEngine) {
		// TODO refactoring previous code to use a step and status manager
		switch (gameEngine.getGameStatus().getGameStep()) {
		case play:
			gameEngine.notifyGameStep(GameStep.play);
			break;
		case discard:
			if (Discard.getValidGameActionSet(gameEngine.getGameStatus(), gameEngine.getGameStatus().getCurrentPlayer())
					.size() > 0) {
				gameEngine.notifyGameStep(GameStep.discard);
			}
			break;
		default:
			break;
		}
	}

	public static boolean isCured(GameStatus gameStatus, Desease desease) {
		return gameStatus.getCuredDeseaseSet().contains(desease);
	}

	public static boolean findCure(GameStatus gameStatus, Desease desease) {
		gameStatus.addCuredDesease(desease);
		if (gameStatus.getCuredDeseaseSet().size() == GameProperties.deseaseSet.size() - 3) {
			gameStatus.setGameStep(GameStep.win);
		}
		return true;
	}

	private static boolean isEradicated(GameStatus gameStatus, Desease desease) {
		return gameStatus.getEradicatedDeseaseSet().contains(desease);
	}

	public static boolean checkEradicated(GameStatus gameStatus, Desease desease) {
		if (gameStatus.isCubeReserveFull(desease) && isCured(gameStatus, desease)) {
			return eradicate(gameStatus, desease);
		}
		return false;
	}

	private static boolean eradicate(GameStatus gameStatus, Desease desease) {
		GameUtil.log(gameStatus, logger, "Desease "+ desease.getName()+" is now eradicated");
		return gameStatus.addEradicatedDesease(desease);
	}

	private static boolean canPlay(GameStatus gameStatus) {
		return gameStatus.getGameStep()==GameStep.play && gameStatus.getCurrentActionCount() > 0;
	}
	
	private static boolean mustDiscard(GameStatus gameStatus) {
		return gameStatus.getGameStep() == GameStep.discard && Discard.getValidGameActionSet(gameStatus, gameStatus.getCurrentPlayer()).size()>0;
	}

	/*
	 * public ArrayList<GameStatus> getAllPossibleNextGames(GameStatus
	 * gameStatus) { if(gameStatus.isOver()) { return new
	 * ArrayList<GameStatus>(); } ArrayList<GameStatus> resultList = new
	 * ArrayList<GameStatus>(); updateStatus(gameStatus); Set<GameAction>
	 * actionSet = GameRules.getAllPossibleActions(gameStatus);
	 * 
	 * if(actionSet == null || actionSet.size() ==0) { //No action possible
	 * resultList.add((Game) this.clone()); return resultList; } else {
	 * for(GameAction action : actionSet) {
	 * 
	 * // possibleActionList.add(action); action.perform(gameStatus); Game
	 * nodeGame = (Game) this.clone(); resultList.add(nodeGame);
	 * resultList.addAll(nodeGame.getAllPossibleNextGames());
	 * 
	 * } } return resultList; }
	 */
}
