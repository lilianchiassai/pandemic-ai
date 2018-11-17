package ai.mcts;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.Game;
import game.GameStatus;

public class MonteCarloTreeSearch {
	
	private static Logger logger = LogManager.getLogger(MonteCarloTreeSearch.class.getName());
	
	MCTSNode<Game> root;
	int timeLimit;
	long iterationTime;
	
	
	public MonteCarloTreeSearch(GameStatus game, int timeLimit) {
		this.root = new MCTSNode(game.clone());
		this.timeLimit = timeLimit;
	}
	
	public MCTSNode run() {
		logger.info("New run");
		
		if(root.isTerminal()) {
			return root;
		} else {
			iterationTime = System.currentTimeMillis();
			while(resourceAvailable()) {
				MCTSNode leaf = traverse(root);
				
				MCTSNode terminalNode = rollout(leaf);
				backpropagate(leaf, terminalNode);
				logger.info("New iteration");
			}
			root = bestChild(root);
			return root;
		}
	}
	
	public MCTSNode traverse(MCTSNode node) {
		if(node.isFullyExpanded()) {
			return bestUCTChild(node);
		} else {
			if(!node.isTerminal()) {
				return bestUnvisitedChild(node);
			} else {
				return null;
			}
		}
	}
	
	public MCTSNode rollout(MCTSNode rolloutRootNode) {
		MCTSNode node = rolloutRootNode.clone();
		while(!node.isTerminal()) {
			node = rolloutPolicy(node);
		}
		return node;
	}
	
	public MCTSNode rolloutPolicy(MCTSNode node) {
		return (MCTSNode) node.getChildren().get(((int) ((Math.random() * 10)/10))*node.getChildren().size());
	}
	
	public void backpropagate(MCTSNode leaf, MCTSNode terminalNode) {
		if(leaf.isRoot()) {
			return;
		}
		leaf.updateStats(terminalNode);
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
		MCTSNode result = null;
		for(Object child : node.getVisitedChildren()) {
			if(max<((MCTSNode)child).getUct()) {
				max = ((MCTSNode)child).getUct();
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
		System.out.println(iterationTime + timeLimit > System.currentTimeMillis());
		return iterationTime + timeLimit > System.currentTimeMillis();
	}
}
