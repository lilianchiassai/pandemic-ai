package pandemic.action;

import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.util.GameUtil;

public class Pass extends GameAction {

  public static int priority = 10;

  public Pass(City origin, int actionCost) {
    super(origin);
    this.actionCost = actionCost;
  }

  public boolean perform(Pandemic pandemic) {
    GameUtil.log(pandemic, GameAction.logger,
        pandemic.gameState.getCurrentPlayer().getName() + " passes.");
    return super.perform(pandemic);
  }

  public void perform(LightGameStatus lightGameStatus) {
    lightGameStatus.actionCount -= this.actionCost;
  }

  public int getPriority() {
    return Pass.priority;
  }
}
