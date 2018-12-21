package ai.mcts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameEngine;
import game.GameProperties;
import game.GameRules;
import game.GameStatus;
import game.RolloutPlayer;
import game.action.GameAction;
import game.action.superaction.SuperAction;

public class MonteCarloTreeSearch {
	
	private static Logger logger = LogManager.getLogger(MonteCarloTreeSearch.class.getName());
	
	MCTSNode root;
	int limit;
	long iterationTime;
	int iterationCounter;
	RolloutPlayer rolloutPlayer;
	
	ArrayList<Integer> weightedActionMap;
	
	
	public MonteCarloTreeSearch(GameStatus gameStatus, int limit, RolloutPlayer player) {
		this.root = new MCTSNode((GameStatus)gameStatus.clone(), null);
		this.limit = limit;
		rolloutPlayer = player;
		rolloutPlayer.setMCTS(this);
		this.weightedActionMap = new ArrayList<Integer>();
	}

	public MCTSNode run() {
		if(root.isTerminal()) {
			return root;
		} else {
			iterationTime = System.currentTimeMillis();
			iterationCounter = 0;
			while(resourceAvailable()) {
				MCTSNode leaf = traverse(root);	
				GameStatus terminalStatus = rollout(leaf);
				backpropagate(leaf, terminalStatus);
				iterationCounter++;
			}
			return bestChild(root);
		}
	}
	
	public MCTSNode traverse(MCTSNode node) {
		if(node.isFullyExpanded()) {
			return bestUCTChild(node);
		} else {
			if(!node.isTerminal()) {
				node.expand();
				return bestUnvisitedChild(node);
			} else {
				return null;
			}
		}
	}
	
	public GameStatus rollout(MCTSNode leaf) {
		GameStatus gameStatus = leaf.getGameStatus().clone();
		rolloutPlayer.setGameStatus(gameStatus);
		GameEngine gameEngine = new GameEngine(gameStatus, rolloutPlayer);
		return gameEngine.run();
	}
	
	public void rolloutPolicy(GameStatus gameStatus) {
		//Get all possible actions
		//weightedActionMap.clear();
		//gameStatus.previousActionList.clear();
		//gameStatus.updateValue();
		//List<GameStatus> gameStatusSet = GameRules.getAllPossibleGameStatus(gameStatus.value, gameStatus, gameStatus.getGameStep()==GameStep.play);
		List<List<SuperAction>> superActionListSet = SuperAction.getAllPossibleSuperActionLists(gameStatus);
		/*for(List<SuperAction> superActionList : superActionListSet) {
			GameStatus clone = gameStatus.clone();
			for(SuperAction superAction : superActionList) {
				superAction.perform(clone);
			}
		}*/
		int random = (int) (Math.random() *superActionListSet.size()*10/10);
		for(SuperAction superAction : superActionListSet.get(random)) {
			superAction.perform(gameStatus);
		}
	}
	
	public void rolloutPolicyRandom(GameStatus gameStatus) {
		//Get all possible actions
		while(GameRules.canPlay(gameStatus)|| GameRules.mustDiscard(gameStatus)) {
			weightedActionMap.clear();
			List<GameAction> actionList = GameRules.getAllPossibleActions(gameStatus);
			
			if(actionList.size()>0) {
				for(int i = 0; i<actionList.size(); i++) {
					int weight = GameProperties.getActionWeight(gameStatus, actionList.get(i));
					for(int k = 0; k<weight; k++) {
						weightedActionMap.add(i);
					}
				}
				//Pick one at random
				int random = (int) (Math.random() *weightedActionMap.size()*10/10);
				//Perform
				GameAction gameAction = actionList.get(weightedActionMap.get(random));
				gameAction.perform(gameStatus);
			} 
		}
	}
	
	public void backpropagate(MCTSNode leaf, GameStatus terminalStatus) {
		if(leaf.isRoot()) {
			return;
		}
		leaf.updateStats(terminalStatus);
	}
	
	public MCTSNode bestChild(MCTSNode node) {
		//TODO sort children list to perform a quicker max ?
		double max = 0;
		MCTSNode result = null;
		for(Object child : node.getVisitedChildren()) {
			if(max<((MCTSNode)child).getVisitCount()) {
				max = ((MCTSNode)child).getVisitCount();
				result =(MCTSNode) child;
			}
		}
		return (MCTSNode) result;
	}
	
	public MCTSNode bestUCTChild(MCTSNode node) {
		//TODO sort children list to perform a quicker max ?
		double max = 0;
		MCTSNode result = node.getVisitedChildren().iterator().next();
		for(MCTSNode child : node.getVisitedChildren()) {
			if(max<child.getUCT()) {
				max = child.getUCT();
				result =(MCTSNode) child;
			}
		}
		return result;
	}
	
	public MCTSNode bestUnvisitedChild(MCTSNode node) {
		//TODO pick at random only between unvisited nodes
		return (MCTSNode) node.getUnvisitedChildren().get(((int) ((Math.random() * 10)/10))*node.getUnvisitedChildren().size());
	}
	
	public boolean resourceAvailable() {
		//return iterationTime + timeLimit > System.currentTimeMillis();
		return iterationCounter<this.limit;
	}

	public void setRoot(GameStatus gameStatus) {
		GameStatus clone = (GameStatus)gameStatus.clone();
		clone.previousActionList = new LinkedList<GameAction>();
		this.root = new MCTSNode(clone, null);
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
