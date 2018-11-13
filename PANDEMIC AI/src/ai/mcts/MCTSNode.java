package ai.mcts;

import java.util.ArrayList;

import game.Game;

public class MCTSNode<T> extends Node implements Comparable {
	
	boolean fullyExpanded;
	boolean expanded;
	boolean terminal;
	int visitCount;
	int victoryCount;
	int visited;
	double uct;
	

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
		ArrayList<Game> gameChildren = ((Game)this.getData()).getAllPossibleNextGames();
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

	private void updateUct() {
		this.uct = this.getVictoryCount()/this.getVisitCount() + Math.sqrt(Math.log10(((MCTSNode)this.getParent()).getVisitCount()/this.getVisitCount()));
	}
	
	public double getUct() {
		return this.uct;
	}

	public void updateStats(Node terminalNode) {
		this.visitCount++;
		if(((Game) terminalNode.getData()).isWin()) {
			this.victoryCount++;
		}
		this.updateUct();
	}

	public double getVictoryCount() {
		return this.victoryCount;
	}
	
	public double getVisitCount() {
		return this.victoryCount;
	}
	
	public boolean isVisited() {
		return this.victoryCount>0;
	}

	@Override
	public int compareTo(Object o) {
		if(this.isVisited() && ((MCTSNode) o).isVisited()) {
			return 0;
		} else if (this.isVisited()  && !((MCTSNode) o).isVisited()) {
			return 1;
		} else if (!this.isVisited()  && ((MCTSNode) o).isVisited()) {
			return -1;
		}
		if(this.uct>((MCTSNode)o).getUct()) {
			return 1;
		} else if(this.uct == ((MCTSNode)o).getUct()) {
			return 0;
		} else {
			return -1;
		}
	}

	
}
