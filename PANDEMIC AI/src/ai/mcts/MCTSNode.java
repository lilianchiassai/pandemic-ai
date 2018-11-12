package ai.mcts;

import java.util.ArrayList;
import java.util.List;

import game.Game;

public class MCTSNode<T> extends Node {
	
	boolean fullyExpanded;
	boolean expanded;
	boolean terminal;
	int visitCount;
	int victoryCount;
	

	public MCTSNode(T data) {
		super(data);
    }
    
    public MCTSNode(T data, Node<T> parent) {
    	super(data, parent);
    }

	public boolean isFullyExpanded() {	
		return this.fullyExpanded;
	}

	public boolean isTerminal() {
		if(terminal) {
			return true;
		} else {
			expand();
			return false;
		}
	}

	private void expand() {
		ArrayList<Game> gameChildren = ((Game)this.data).getAllPossibleNextGames();
		if(gameChildren == null || gameChildren.size()==0) {
			terminal = true;
			expanded = true;
		} else {
			for(Game game : gameChildren) {
				//TODO : improve by removing node with matching game states
				this.addChild(new MCTSNode(game, this));
			}
			expanded = true;
		}
	}

	

	public void updateStats(MCTSNode node, Node terminalNode) {
		node.visitCount++;
		if(((Game) terminalNode.getData()).isWin()) {
			node.victoryCount++;
		}
	}
}
