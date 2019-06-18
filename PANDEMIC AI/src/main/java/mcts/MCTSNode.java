package mcts;

import java.util.ArrayList;
import java.util.List;
import game.Game;
import game.Move;

public class MCTSNode {
  public Game game;
  private MCTSNode parent;
  private List<MCTSNode> unvisitedChildren;
  private List<MCTSNode> visitedChildren;
  public Move<?> move;

  boolean fullyExpanded;
  boolean expanded;
  int visitCount;
  int victoryCount;
  int visited;
  // GameAction gameAction;

  public MCTSNode(Game game, MCTSNode parent) {
    this.game = game;
    this.parent = parent;
    this.unvisitedChildren = new ArrayList<MCTSNode>();
    this.visitedChildren = new ArrayList<MCTSNode>();
    this.fullyExpanded = false;
    this.fullyExpanded = false;
    this.visitCount = 0;
    this.victoryCount = 0;
    this.visited = 0;
  }

  public MCTSNode getParent() {
    return this.parent;
  }

  public boolean isRoot() {
    return this.parent == null;
  }

  protected void addChild(MCTSNode node) {
    this.unvisitedChildren.add(node);
  }

  public Game getGame() {
    return this.game;
  }

  public List<MCTSNode> getUnvisitedChildren() {
    return this.unvisitedChildren;
  }

  public List<MCTSNode> getVisitedChildren() {
    return this.visitedChildren;
  }

  public boolean removeUnvisitedChild(MCTSNode node) {
    return this.unvisitedChildren.remove(node);
  }

  public void addUnVisitedChild(MCTSNode node) {
    this.unvisitedChildren.add(node);
  }

  public void removeVisitedChild(MCTSNode node) {
    this.visitedChildren.remove(node);
  }

  public void addVisitedChild(MCTSNode node) {
    this.visitedChildren.add(node);
  }

  public boolean isFullyExpanded() {
    /*
     * if(isTerminal()) { this.fullyExpanded = true; }
     */
    return this.fullyExpanded;
  }

  public boolean isTerminal() {
    return game.isOver();
  }

  public boolean expand() {
    if (isTerminal()) {
      fullyExpanded = true;
      return true;
    }
    if (isExpanded()) {
      return true;
    }

    this.expanded = true;

    ArrayList<? extends Move> moves = game.getMoves();
    for (Move<?> move : moves) {
      Game clone = game.duplicate();
      clone.perform(move);
      MCTSNode node = new MCTSNode(clone, this);
      node.move = move;
      this.addChild(node);
    }

    return false;
  }

  private boolean isExpanded() {
    return this.expanded;
  }

  public double getUCT() {
    return (this.getVictoryCount() / this.getVisitCount()
        + 2 * Math.sqrt(Math.log(this.getParent().getVisitCount()) / this.getVisitCount()));

    // return GameProperties.getActionWeight(gameStatus, gameAction) *
    // (this.getVictoryCount()/this.getVisitCount() +
    // 2*Math.sqrt(Math.log(this.getParent().getVisitCount())/this.getVisitCount()));
  }

  public void updateStats(Game terminalStatus) {
    this.visitCount++;
    if (terminalStatus.isWin()) {
      this.victoryCount++;
    }


    if (!isRoot()) {
      this.getParent().visitCount++;
      this.getParent().visitChild(this);
    }
  }

  public void visitChild(MCTSNode node) {
    if (!this.fullyExpanded && this.removeUnvisitedChild(node)) {
      this.addVisitedChild(node);
    }
    if (this.getUnvisitedChildren().size() == 0) {
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
    return this.visitCount > 0;
  }
}
