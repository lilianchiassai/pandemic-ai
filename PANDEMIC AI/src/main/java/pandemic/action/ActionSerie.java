package pandemic.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import game.Move;
import pandemic.Pandemic;

public class ActionSerie extends LinkedList<GameAction> implements Move<Pandemic> {

  /**
   * 
   */
  private static final long serialVersionUID = -2558256800448539788L;
  public int actionCost;
  int cancelIndex;

  public ActionSerie() {
    super();
  }

  public ActionSerie(Collection<? extends GameAction> collection) {
    super(collection);
    for (GameAction gameAction : collection) {
      this.actionCost += gameAction.actionCost;
    }
  }

  @Override
  public boolean add(GameAction gameAction) {
    if (super.add(gameAction)) {
      this.actionCost += gameAction.actionCost;
      return true;
    }
    return false;
  }

  @Override
  public GameAction removeLast() {
    this.actionCost -= this.getLast().actionCost;
    return super.removeLast();
  }

  @Override
  public boolean perform(Pandemic pandemic) {
    for (GameAction gameAction : this) {
      if (!gameAction.perform(pandemic)) {
        throw new IllegalArgumentException(
            "ActionSerie could not be performed. ActionSerie validity should be ensured before perform call.");
      }
    }
    return true;
  }

  public void cancel(Pandemic pandemic) {
    Iterator<GameAction> it = this.descendingIterator();
    while (it.hasNext()) {
      it.next().cancel(pandemic);
    }
  }
}
