package pandemic.action;

import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.material.card.CityCard;
import pandemic.util.GameUtil;

public class DirectFlight extends MoveAction {

  public static int priority = 0;
  public static boolean strictPriority = true;

  public DirectFlight(City origin, City destination) {
    super(origin, destination);
  }

  @Override
  public boolean canPerform(Pandemic pandemic) {
    if (pandemic.gameState.getCurrentActionCount() >= this.actionCost) {
      if (pandemic.gameState.getCurrentPlayerPosition() == origin
          && pandemic.gameState.getCurrentHand().contains(destination.getCityCard())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean perform(Pandemic pandemic) {
    CityCard cityCard = pandemic.gameState.getCurrentHand().getCityCard(destination);
    if (cityCard != null && super.perform(pandemic)) {
      pandemic.gameState.getCurrentHand().remove(cityCard);
      GameUtil.log(pandemic, GameAction.logger, pandemic.gameState.getCurrentPlayer().getName()
          + " flies directly from " + origin.getName() + " to " + destination.getName());
      return pandemic.gameState.setCharacterPosition(pandemic.gameState.getCurrentPlayer(),
          destination);
    }
    return false;
  }

  @Override
  public void cancel(Pandemic pandemic) {
    super.cancel(pandemic);
    pandemic.gameState.getCurrentHand().add(destination.getCityCard());
  }

  @Override
  public void perform(LightGameStatus lightGameStatus) {
    lightGameStatus.hand.remove(destination.getCityCard());
    lightGameStatus.position = destination;
    lightGameStatus.actionCount -= this.actionCost;
  }

  @Override
  public int getPriority() {
    return DirectFlight.priority;
  }

  public String toString() {
    return "DirectFlight " + origin + " -> " + destination;
  }
}
