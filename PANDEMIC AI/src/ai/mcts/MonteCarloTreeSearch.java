package ai.mcts;

import java.util.Collections;

import game.Game;

public class MonteCarloTreeSearch {
	MCTSNode<Game> root;
	int timeLimit;
	int iterationTime;
	MCTSNode rolloutRootNode;
	
	public MonteCarloTreeSearch(Game gameState, int timeLimit) {
		this.root = new MCTSNode(gameState);
		this.timeLimit = timeLimit;
	}
	
	public MCTSNode run(MCTSNode currentRoot) {
		while(resourceAvailable()) {
			MCTSNode leaf = traverse(currentRoot);
			MCTSNode terminalNode = rollout(leaf);
			backpropagate(leaf, terminalNode);
		}
		return bestChild(currentRoot);
	}
	
	public MCTSNode traverse(MCTSNode node) {
		if(node.isFullyExpanded()) {
			return bestUCTChild(node);
		} else {
			return bestUnvisitedChild(node);
		}
	}
	
	public MCTSNode rollout(MCTSNode rolloutRootNode) {
		MCTSNode node = rolloutRootNode;
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
		((MCTSNode<Game>) leaf.getParent()).updateStats(terminalNode);
		leaf.updateStats(terminalNode);
	}
	
	public MCTSNode bestChild(MCTSNode node) {
		//Performed through children sorting : the maxed uct child is always the first
		double max = 0;
		MCTSNode result = null;
		for(Object child : node.getChildren()) {
			if(max<((MCTSNode)child).getVisitCount()) {
				max = ((MCTSNode)child).getVisitCount();
				result =(MCTSNode) child;
			}
		}
		return (MCTSNode) node.getChildren().get(0);
	}
	
	public MCTSNode bestUCTChild(MCTSNode node) {
		//TODO sort children list to perform a quicker max ?
		double max = 0;
		MCTSNode result = null;
		for(Object child : node.getChildren()) {
			if(max<((MCTSNode)child).getUct()) {
				max = ((MCTSNode)child).getUct();
				result =(MCTSNode) child;
			}
		}
		return result;
	}
	
	
	
	public MCTSNode bestUnvisitedChild(MCTSNode node) {
		//TODO pick at random only between unvisited nodes
		return (MCTSNode) node.getChildren().get(((int) ((Math.random() * 10)/10))*node.getChildren().size());
	}
	
	public boolean resourceAvailable() {
		return iterationTime + timeLimit < System.currentTimeMillis();
	}
}
