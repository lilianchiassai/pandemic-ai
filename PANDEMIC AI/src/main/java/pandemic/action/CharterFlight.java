package pandemic.action;

import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.material.card.CityCard;
import pandemic.util.GameUtil;

public class CharterFlight extends MoveAction {

  public static int priority = 3;

  public CharterFlight(City origin, City destination) {
    super(origin, destination);
  }

  @Override
  public boolean canPerform(Pandemic pandemic) {
    if (pandemic.gameState.getCurrentActionCount() >= this.actionCost) {
      if (pandemic.gameState.getCurrentPlayerPosition() == origin
          && pandemic.gameState.getCurrentHand().contains(origin.getCityCard())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean perform(Pandemic pandemic) {
    CityCard cityCard = pandemic.gameState.getCurrentHand()
        .getCityCard(pandemic.gameState.getCurrentPlayerPosition());
    if (cityCard != null && super.perform(pandemic)) {
      pandemic.gameState.getCurrentHand().removeAndDiscard(pandemic.gameState, cityCard);
      GameUtil.log(pandemic, GameAction.logger, pandemic.gameState.getCurrentPlayer().getName()
          + " takes a charter flight from " + origin.getName() + " to " + destination.getName());
      return pandemic.gameState.setCharacterPosition(pandemic.gameState.getCurrentPlayer(),
          destination);
    }
    return false;
  }

  @Override
  public void cancel(Pandemic pandemic) {
    super.cancel(pandemic);
    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
    pandemic.gameState.getPlayerDeck().getDiscardPile().remove(origin.getCityCard());
  }

  @Override
  public void perform(LightGameStatus lightGameStatus) {
    lightGameStatus.hand.remove(lightGameStatus.position.getCityCard());
    lightGameStatus.position = destination;
  }


  @Override
  public int getPriority() {
    return CharterFlight.priority;
  }



  public String toString() {
    return "CharterFlight " + origin + " -> " + destination;
  }
}
