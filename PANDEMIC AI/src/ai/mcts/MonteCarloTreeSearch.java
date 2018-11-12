package ai.mcts;

import game.Game;

public class MonteCarloTreeSearch {
	MCTSNode<Game> root;
	int timeLimit;
	int iterationTime;
	
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
			//TODO isTerminal method expands the node
			node = rolloutPolicy(node);
		}
		return node;
	}
	
	public MCTSNode rolloutPolicy(MCTSNode node) {
		return (MCTSNode) node.getChildren().get((int) ((Math.random() * 10)/10));
	}
	
	public void backpropagate(MCTSNode node, MCTSNode terminalNode) {
		if(node.isRoot()) {
			return;
		}
		node.updateStats(node, terminalNode);
		backpropagate((MCTSNode) node.getParent(), terminalNode);
	}
	
	public MCTSNode bestChild(MCTSNode node) {
		// TODO return child with max number of visits
		return (MCTSNode) node.getChildren().get(0);
	}
	
	public MCTSNode bestUCTChild(MCTSNode node) {
		// TODO return node maximizing UCT(vi,v) = Q(vi)/N(vi) + C * square_root(log(N(v))/N(vi))
		return (MCTSNode) node.getChildren().get(0);
	}
	
	public MCTSNode bestUnvisitedChild(MCTSNode node) {
		//TODO pick at random only between unvisited nodes
		return (MCTSNode) node.getChildren().get((int) ((Math.random() * 10)/10));
	}
	
	public boolean resourceAvailable() {
		return iterationTime + timeLimit < System.currentTimeMillis();
	}
}
