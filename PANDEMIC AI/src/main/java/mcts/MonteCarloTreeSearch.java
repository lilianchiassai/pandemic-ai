package mcts;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.Engine;
import game.Game;

public class MonteCarloTreeSearch {
	
	private static Logger logger = LogManager.getLogger(MonteCarloTreeSearch.class.getName());
	
	MCTSNode root;
	int limit;
	long iterationTime;
	int iterationCounter;
	RolloutPlayer rolloutPlayer;
	Engine rolloutEngine;
	
	ArrayList<Integer> weightedActionMap;
	
	
	public MonteCarloTreeSearch(Game game, int limit, RolloutPlayer player) {
		this.root = new MCTSNode(game.duplicate(), null);
		this.limit = limit;
		this.rolloutPlayer = player;
		this.rolloutEngine = new Engine(rolloutPlayer);
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
				Game terminalStatus = rollout(leaf);
				backpropagate(leaf, terminalStatus);
				iterationCounter++;
				logger.info(iterationCounter);
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
	
	public Game rollout(MCTSNode leaf) {
		Game game = leaf.getGame().duplicate();
		return rolloutEngine.run(game);
	}
	
	public void backpropagate(MCTSNode leaf, Game terminalStatus) {
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

	public void setRoot(Game game) {
		Game clone = game.duplicate();
		this.root = new MCTSNode(clone, null);
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
