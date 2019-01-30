package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.action.Build;
import game.action.CharterFlight;
import game.action.Cure;
import game.action.DirectFlight;
import game.action.Discard;
import game.action.Drive;
import game.action.GameAction;
import game.action.GiveKnowledge;
import game.action.MoveAction;
import game.action.MultiDrive;
import game.action.Pass;
import game.action.ReceiveKnowledge;
import game.action.ShareKnowledge;
import game.action.ShuttleFlight;
import game.action.Treat;
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.card.Card;
import objects.card.CityCard;
import objects.card.EpidemicCard;
import objects.card.PlayerCard;
import objects.card.PropagationCard;
import tree.Node;
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
				//lose(gameStatus);
				GameUtil.log(gameStatus, logger, "No more player card.");
				gameStatus.setGameStep(GameStep.win);
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
	
	/*public static List<GameStatus> getAllPossibleGameStatus(int refValue, GameStatus gameStatus, boolean filter) {
		List<GameStatus> resultList = new ArrayList<GameStatus>();
		if(canPlay(gameStatus) || mustDiscard(gameStatus)) {
			for(GameStatus clone : getNextGameStatus(gameStatus)) {
				resultList.addAll(getAllPossibleGameStatus(refValue, clone, filter));
			}
		} else {
			gameStatus.updateValue();
			if(!filter || gameStatus.value - refValue > 0) {
				resultList.add(gameStatus);
			}
		}
		return resultList;
	}*/
	


	public static List<GameStatus> getNextGameStatus(GameStatus gameStatus) {
		List<GameStatus> gameStatusList = new ArrayList<GameStatus>();
		getAllPossibleActions(gameStatus);
		for(GameAction gameAction : gameStatus.actionList) {
			GameStatus clone = gameStatus.clone();
			gameAction.perform(clone);
			gameStatusList.add(clone);
		}
		return gameStatusList;
	}
	
	public static List<GameStatus> getAllPossibleGameStatus(int refValue, GameStatus gameStatus, boolean filter) {
		LinkedList<GameStatus> resultList = new LinkedList<GameStatus>();
		List<LinkedList<GameAction>> possibleActionLists = getAllPossibleGameActionsList(gameStatus);
		if(filter) {
			List<GameAction> maxActionList = null;
			int max = -1000;
			for(LinkedList<GameAction> actionList : possibleActionLists) {
				int value = GameRules.getValue(gameStatus, actionList);
				if(value>4) {
					GameStatus clone = gameStatus.clone();
					for(GameAction gameAction : actionList) {
						gameAction.perform(clone);
					}
					resultList.add(clone);
				} else if (resultList.size() == 0) {
					if(value>max) {
						max = value;
						maxActionList = actionList;
					}				
				}
			}
			if(resultList.size() == 0) {
				GameStatus clone = gameStatus.clone();
				for(GameAction gameAction : maxActionList) {
					gameAction.perform(clone);
				}
				resultList.add(clone);
			} 
			
		} else {
			for(LinkedList<GameAction> actionList : possibleActionLists) {
			
				GameStatus clone = gameStatus.clone();
				for(GameAction gameAction : actionList) {
					gameAction.perform(clone);
				}
				resultList.add(clone);
			
			}
		}
		return resultList;
	}
	


	public static List<LinkedList<GameAction>> getAllPossibleGameActionsList(GameStatus gameStatus) {
		List<LinkedList<GameAction>> result = new ArrayList<LinkedList<GameAction>>();
		if(canPlay(gameStatus) || mustDiscard(gameStatus)) {
			List<GameAction> gameActionList = new ArrayList<GameAction>( getAllPossibleActions(gameStatus));
			Iterator<GameAction> it = gameActionList.iterator();
			while(it.hasNext()) {
				GameAction gameAction = it.next(); 
				gameAction.perform(gameStatus);
				List<LinkedList<GameAction>> possibleGameActionsList = getAllPossibleGameActionsList(gameStatus);
				if(possibleGameActionsList.size() > 0) {
					for(LinkedList<GameAction> actionList : possibleGameActionsList) {
						actionList.add(0,gameAction);
						result.add(actionList);
					}
				} else {
					LinkedList<GameAction> actionList = new LinkedList<GameAction>();
					actionList.add(gameAction);
					result.add(actionList);
				}
				
				gameStatus.rollBack();
			}
		}
		
		return result;
	}
	
	public static List<GameAction> getAllPossibleActions(GameStatus gameStatus) {
		gameStatus.actionList.clear();
		if (gameStatus.getGameStep() == GameStep.discard) {
			for (Character character : gameStatus.getCharacterPositionMap().keySet()) {
				gameStatus.actionList.addAll(Discard.getValidGameActionSet(gameStatus, character));
			}
		} else if (gameStatus.getGameStep() == GameStep.play) {
			
			gameStatus.actionList.addAll(Cure.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(Treat.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(Build.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(ShareKnowledge.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(Pass.getValidGameActionSet(gameStatus));
			/*gameStatus.actionList.addAll(Drive.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(DirectFlight.getValidGameActionSet(gameStatus));
			gameStatus.actionList.addAll(CharterFlight.getValidGameActionSet(gameStatus));*/
			gameStatus.actionList.addAll(ShuttleFlight.getValidGameActionSet(gameStatus));
				
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
		if (gameStatus.getCuredDeseaseSet().size() == GameProperties.deseaseSet.size()-2) {
			gameStatus.setGameStep(GameStep.win);
		}
		return true;
	}
	
	public static boolean cancelCure(GameStatus gameStatus, Desease desease) {
		gameStatus.removeCuredDesease(desease);
		if (gameStatus.getGameStep() == GameStep.win) {
			gameStatus.setGameStep(GameStep.play);
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
		uneradicate(gameStatus, desease);
		return false;
	}

	private static boolean eradicate(GameStatus gameStatus, Desease desease) {
		GameUtil.log(gameStatus, logger, "Desease "+ desease.getName()+" is now eradicated");
		return gameStatus.addEradicatedDesease(desease);
	}
	
	private static boolean uneradicate(GameStatus gameStatus, Desease desease) {
		GameUtil.log(gameStatus, logger, "Desease "+ desease.getName()+" is not eradicated anymore");
		return gameStatus.removeEradicatedDesease(desease);
	}

	public static boolean canPlay(GameStatus gameStatus) {
		return gameStatus.getGameStep()==GameStep.play && gameStatus.getCurrentActionCount() > 0;
	}
	
	public static boolean mustDiscard(GameStatus gameStatus) {
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
	
	private static int getValue(GameStatus gameStatus, LinkedList<GameAction> actionList) {
		if( gameStatus.getGameStep() == GameStep.play) {
			for(GameAction gameAction : actionList) {
				if(!(gameAction instanceof MoveAction)) {
					if(gameAction instanceof Pass) {
						return -1;
					}
					return 5;
				}
				
				
			}
			
			return 0;
		} else if(gameStatus.getGameStep() == GameStep.discard) {
			return 5;
		} else {
			return 1;
		}
	}
	
	private static double getPositionValue(GameStatus gameStatus, City position) {
		double value =0;
		for(City city : position.getNeighbourSet()) {
			if(gameStatus.getCityCubeSet(city, city.getDesease()).size()>0) {
				value++;
			} else {
				for(City neighbour : city.getNeighbourSet()) {
					if(gameStatus.getCityCubeSet(neighbour, neighbour.getDesease()).size()>0) {
						value+=0.5;
					} else {
						for(City neighbour2 : neighbour.getNeighbourSet()) {
							if(gameStatus.getCityCubeSet(neighbour, neighbour.getDesease()).size()>0) {
								value+=0.2;
							}
						}
					}
				}
			}
			
		}
		return 0;
	}
	
	public static Set<List<GameAction>> getGameActionList(Node<GameAction> node) {
		Set<List<GameAction>> result = new HashSet<List<GameAction>>();
		if(node.getChildren() !=null) {
			for(Node<GameAction> child : node.getChildren()) {
				if(child.isLeaf()) {
					result.add(child.getAncestorDataList());
				} else {
					result.addAll(getGameActionList(child));
				}
			}
		}
		return result;
	}
	
	public static Node<GameAction> expandActionTree(City origin) {
		Node root = new Node(null, null);
		expandActionTree(root, origin, 4);
		return root;
	}
	
	static int counter = 0;
	
	private static void expandActionTree(Node<GameAction> root, City origin, int actionCount) {
		if(actionCount == 3) {
			counter++;
			logger.warn(counter);
		}
		if(actionCount >0) {
			City neworigin = origin;
			if( root.getData() instanceof MoveAction) {
				neworigin = ((MoveAction) root.getData()).getDestination();
			}
			root.expand(getAllPossibleDefaultActions(neworigin, root.getData(), actionCount));
			for(Node<GameAction> node : root.getChildren()) {
				expandActionTree(node, neworigin, actionCount - node.getData().getCost());
			}
		}	
	}
	static City defaultOrigin = new City("Default Origin", null, 0);
	
	private static ArrayList<GameAction> getAllPossibleDefaultActions(City origin, GameAction previousAction, int actionCount) {
		ArrayList<GameAction> actionList = new ArrayList<GameAction>();
		if(!(previousAction instanceof MultiDrive || previousAction instanceof DirectFlight || previousAction instanceof CharterFlight)) {
			for(int i = 0; i<actionCount; i++) {
				actionList.addAll(origin.getMultiDriveActionSet(i));
			}
		}
		if(!(previousAction instanceof DirectFlight || previousAction instanceof CharterFlight)) {
			actionList.addAll(origin.getDirectFlightActionSet());
			actionList.addAll(origin.getCharterFlightActionSet());
			if(!(previousAction instanceof ShuttleFlight)) {
				actionList.addAll(origin.getShuttleFlightActionSet());
			}
		}
		if(!(previousAction instanceof Cure)){
			actionList.addAll(Cure.getDefaultGameActionSet());
		}
		actionList.addAll(Treat.getDefaultGameActionSet());
		if(!(previousAction instanceof Build)){
			actionList.addAll(Build.getDefaultGameActionSet());
		}
		actionList.addAll(ShareKnowledge.getDefaultGameActionSet());
		actionList.addAll(Pass.getDefaultGameActionSet(actionCount));
		
		return actionList;
	}
	
	private static boolean isValuable(Node<GameAction> node, Node<GameAction> root) {
		 return !isEquivalentOrInferior(node, root);
	}
	
	public static void filterActionTree(Node<GameAction> node,Node<GameAction> root) {
		if(node == root || isValuable(node,root)) {
			if(!node.isLeaf()) {
				for(Node<GameAction> child : node.getChildren()) {
					filterActionTree(child,root);
				}
			}
		} else {
			node.getParent().removeChild(node);
		}
	}
	
	private static boolean isEquivalentOrInferior(Node<GameAction> node1, Node<GameAction> root) {
		/*
		 * Is equivalent if
		 * last destination is the same
		 * total of treat actions are the same in the same cities or inferior (if no cure)
		 * build actions on the same spot
		 * shareknowledge on the same spot
		 * directflight to the same spot
		 * 
		 * 
		 */
		
		class GameActionListEffects {
			Set<City> builtResearchCenters;
			Set<Desease> curedDesease;
			Set<Card> cardDiscarded;
			Map<Card, Character> cardReceived;
			Map<Card, Character> cardGiven;
			Map<Desease, Map<City,Integer>> treatedDeseasePriorCure;
			Map<Desease, Set<City>> treatedDeseaseAfterCure;
			City finalDestination;
			int actionCost;
			boolean coherence;
			
			GameActionListEffects(List<GameAction> ancestorDataList) {
				builtResearchCenters = new HashSet<City>();
				curedDesease = new HashSet<Desease>();
				cardDiscarded = new HashSet<Card>();
				cardReceived = new HashMap<Card,Character>();
				cardGiven = new HashMap<Card,Character>();
				treatedDeseasePriorCure = new HashMap<Desease,Map<City,Integer>>();
				treatedDeseaseAfterCure = new HashMap<Desease,Set<City>>();
				for(Desease desease : GameProperties.deseaseSet) {
					treatedDeseasePriorCure.put(desease, new HashMap<City,Integer>());
					treatedDeseaseAfterCure.put(desease, new HashSet<City>());
				}
				finalDestination = defaultOrigin;
				actionCost = 0;
				for(GameAction gameAction : ancestorDataList) {
					if(gameAction instanceof Drive) {
						finalDestination = ((Drive)gameAction).getDestination();
						actionCost+= gameAction.getCost();
					} else if (gameAction instanceof DirectFlight){
						finalDestination = ((DirectFlight)gameAction).getDestination();
						if(cardGiven.keySet().contains(finalDestination.getCityCard()) || cardDiscarded.contains(finalDestination.getCityCard())) {
							coherence = false;
						}
						cardDiscarded.add(finalDestination.getCityCard());
						actionCost+= gameAction.getCost();
					} else if (gameAction instanceof CharterFlight){
						if(cardGiven.keySet().contains(finalDestination.getCityCard()) || cardDiscarded.contains(finalDestination.getCityCard())) {
							coherence = false;
						}
						cardDiscarded.add(finalDestination.getCityCard());
						finalDestination = ((CharterFlight)gameAction).getDestination();
						actionCost+= gameAction.getCost();
					} else if (gameAction instanceof Treat){
						if(curedDesease.contains(((Treat)gameAction).getDesease())) {
							if(treatedDeseaseAfterCure.get(((Treat)gameAction).getDesease()).contains(finalDestination)) {
								coherence = false;
							}
							treatedDeseaseAfterCure.get(((Treat)gameAction).getDesease()).add(finalDestination);
						} else {
							if(treatedDeseasePriorCure.get(((Treat)gameAction).getDesease()).get(finalDestination) == null) {
								treatedDeseasePriorCure.get(((Treat)gameAction).getDesease()).put(finalDestination, 1);								
							} else {
								treatedDeseasePriorCure.get(((Treat)gameAction).getDesease()).put(finalDestination, treatedDeseasePriorCure.get(((Treat)gameAction).getDesease()).get(finalDestination)+1);								
							}
						}
					} else if (gameAction instanceof ReceiveKnowledge){
						if(cardGiven.get(finalDestination.getCityCard()) == ((ShareKnowledge) gameAction).getCharacter() ||cardReceived.keySet().contains(finalDestination.getCityCard()) || cardGiven.keySet().contains(finalDestination.getCityCard()) || cardDiscarded.contains(finalDestination.getCityCard())) {
							coherence = false;
						}
						cardReceived.put(finalDestination.getCityCard(),((ShareKnowledge) gameAction).getCharacter());
					} else if (gameAction instanceof GiveKnowledge){
						if(cardReceived.get(finalDestination.getCityCard()) == ((ShareKnowledge) gameAction).getCharacter() || cardGiven.keySet().contains(finalDestination.getCityCard()) || cardDiscarded.contains(finalDestination.getCityCard())) {
							coherence = false;
						}
						cardGiven.put(finalDestination.getCityCard(), ((ShareKnowledge) gameAction).getCharacter());
					} else if (gameAction instanceof Build){
						builtResearchCenters.add(finalDestination);
					} else if (gameAction instanceof Cure){
						if(cardDiscarded.size()+cardGiven.keySet().size()-cardGiven.keySet().size()>2 || curedDesease.size()>0) {
							coherence = false;
						}
						curedDesease.add(((Cure)gameAction).getDesease());
						cardDiscarded.addAll(((Cure)gameAction).getSet());
					}
				}
			}
			
			boolean equivalentOrInferior(GameActionListEffects nodeEffects) {
				boolean equals = true;
				if ((!coherence) ||
						(nodeEffects.builtResearchCenters.containsAll(this.builtResearchCenters)
						&& nodeEffects.curedDesease.containsAll(this.curedDesease)
						&& nodeEffects.cardDiscarded.containsAll(this.cardDiscarded)
						&& nodeEffects.cardReceived.equals(this.cardReceived)
						&& nodeEffects.cardGiven.equals(this.cardGiven)
						&& nodeEffects.treatedDeseasePriorCure.equals(this.treatedDeseasePriorCure)
						&& nodeEffects.treatedDeseaseAfterCure.equals(this.treatedDeseaseAfterCure)
						&& nodeEffects.finalDestination == this.finalDestination
						&& nodeEffects.actionCost <= this.actionCost)
						) {
					return true;
				} 
				return false;
			}
		}
		
		GameActionListEffects effectNode1 = new GameActionListEffects(node1.getAncestorDataList());
		Set<List<GameAction>> gameActionList = GameRules.getGameActionList(root);
		if(gameActionList!=null && gameActionList.size()>0) {
			for(List<GameAction> actionList : gameActionList) {
				GameActionListEffects effectNode2 = new GameActionListEffects(actionList);
				if(effectNode1.equivalentOrInferior(effectNode2)) {
					return true;
				}
			}
		}
		
			
		return false;
	}
	
	public Set<ArrayList<GameAction>> findActionLists() {
		
		
		
		return null;
	}
	
	
	
}
