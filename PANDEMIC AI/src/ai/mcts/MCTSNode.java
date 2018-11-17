package ai.mcts;

import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.action.GameAction;

public class MCTSNode<T> extends Node implements Comparable {
	
	boolean fullyExpanded;
	boolean expanded;
	boolean terminal;
	int visitCount;
	int victoryCount;
	int visited;
	double uct;
	List<GameAction> actionList;
	
	public MCTSNode(MCTSNode node) {
		super(((Game)node.getData()).clone(), node.getParent());
		fullyExpanded = node.fullyExpanded;
		expanded = node.expanded;
		terminal = node.terminal;
		visitCount = 0;
		victoryCount = 0;
		uct = 0.0;
		actionList = new ArrayList<GameAction>();
		
    }

	public MCTSNode clone() {
		MCTSNode clone = new MCTSNode(this);
		for(Object child : this.getUnvisitedChildren()) {
			clone.addUnVisitedChild(new MCTSNode(child));
		}
		for(Object child : this.getVisitedChildren()) {
			clone.addVisitedChild(new MCTSNode(child));
		}
		return clone;
	}
	
	public MCTSNode(T data) {
		super(data);
		fullyExpanded = false;
		expanded = false;
		terminal = false;
		visitCount = 0;
		victoryCount = 0;
		uct = 0.0;
		actionList = new ArrayList<GameAction>();
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
			return !expand();
		}
	}

	private boolean expand() {
		((Game)this.getData()).setPossibleActionList(new ArrayList<GameAction>());
		Game clone = ((Game)this.getData()).clone();
		ArrayList<Game> gameChildren = clone.getAllPossibleNextGames();
		if(clone.isOver()) {
			terminal = true;
			expanded = true;
			fullyExpanded = true;
		} else {
			for(Game game : gameChildren) {
				//TODO : improve by removing node with matching game states
				this.addChild(new MCTSNode<Game>(game, this));
			}
			expanded = true;
		}
		return expanded && !terminal;
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
			
		if(!isRoot()) {
			((MCTSNode<Game>) this.getParent()).visitCount++;
			((MCTSNode<Game>) this.getParent()).visitChild(this);
		}
	}

	public void visitChild(MCTSNode node) {
		this.removeUnvisitedChild(node);
		this.addVisitedChild(node);
		if(this.getUnvisitedChildren().size() == 0) {
			this.fullyExpanded = true;
		}
	}
	
	public double getVictoryCount() {
		return this.victoryCount;
	}
	
	public double getVisitCount() {
		return this.visitCount;
	}
	
	public boolean isVisited() {
		return this.visitCount>0;
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

	public List<GameAction> getActionList() {
		return ((Game)this.getData()).getPossibleActionList();
	}
	
	
}
