
package pandemic.action;

import java.util.LinkedList;
import java.util.Set;
import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.util.GameUtil;

public class Drive extends MoveAction {

  public static int priority = 1;

  public Set<LinkedList<City>> pathSet;

  public Drive(City origin, City destination) {
    super(origin, destination);
    this.actionCost = origin.getDistance(destination);
  }

  public boolean perform(Pandemic pandemic) {
    if (super.perform(pandemic)) {
      GameUtil.log(pandemic, GameAction.logger,
          pandemic.gameState.getCurrentPlayer().getName() + " drives from " + origin.getName()
              + " to " + destination.getName() + " in " + this.actionCost + " hours");
      return pandemic.gameState.setCharacterPosition(pandemic.gameState.getCurrentPlayer(),
          destination);
    }
    return false;
  }

  public void cancel(Pandemic pandemic) {
    super.cancel(pandemic);
  }

  public int getPriority() {
    return Drive.priority;
  }

  public String toString() {
    return origin + " -> " + destination + " (" + actionCost + ")";
  }
}
