package pandemic.action;

import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.util.GameUtil;

public class Build extends StaticAction {

  public static int priority = 2;

  public Build(City origin) {
    super(origin);
  }

  @Override
  public boolean canPerform(Pandemic pandemic) {
    if (pandemic.gameState.getCurrentActionCount() >= this.actionCost) {
      if (!pandemic.gameState.hasResearchCenter(origin)
          && pandemic.gameState
              .getResearchCenterCount() < pandemic.gameState.gameProperties.maxResearchCenterCounter
          && pandemic.gameState.getCurrentHand().contains(origin.getCityCard())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean perform(Pandemic pandemic) {
    if (pandemic.gameState.getCurrentHand().contains(origin.getCityCard())) {
      if (canPerform(pandemic)) {
        if (pandemic.gameState.addResearchCenter(pandemic.gameState.getCurrentPlayerPosition())) {
          GameUtil.log(pandemic, GameAction.logger, pandemic.gameState.getCurrentPlayer().getName()
              + " builds a new Research Center in " + origin.getName() + ".");
          pandemic.gameState.getCurrentHand().removeAndDiscard(pandemic.gameState,
              origin.getCityCard());
          return super.perform(pandemic);
        }
      }
    }
    return false;
  }

  @Override
  public void cancel(Pandemic pandemic) {
    super.cancel(pandemic);
    pandemic.gameState.removeResearchCenter(this.origin);
    pandemic.gameState.getCurrentHand().add(origin.getCityCard());
    pandemic.gameState.getPlayerDeck().getDiscardPile().remove(origin.getCityCard());
  }

  @Override
  public void perform(LightGameStatus lightGameStatus) {
    lightGameStatus.hand.remove(lightGameStatus.position.getCityCard());
    lightGameStatus.researchCenterCity[lightGameStatus.position.id] = Boolean.TRUE;
    lightGameStatus.actionCount -= this.actionCost;
  }

  @Override
  public int getPriority() {
    return Build.priority;
  }


}
