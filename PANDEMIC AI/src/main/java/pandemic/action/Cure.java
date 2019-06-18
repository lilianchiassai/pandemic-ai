package pandemic.action;

import java.util.HashSet;
import java.util.Set;
import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.material.card.CityCard;
import pandemic.util.GameUtil;

public class Cure extends StaticAction {

  public static int priority = 2;
  public static boolean strictPriority = true;

  Desease desease;
  private Set<CityCard> set;

  public Cure(City origin, Desease desease, Set<CityCard> set) {
    super(origin);
    this.desease = desease;
    this.setSet(set);
  }


  public boolean canPerform(Pandemic pandemic) {
    if (pandemic.gameState.getCurrentActionCount() >= this.actionCost) {
      if (pandemic.gameState.getCurrentHand().size() > this.set.size()
          && !pandemic.gameState.isCured(desease)
          && pandemic.gameState.hasResearchCenter(pandemic.gameState.getCurrentPlayerPosition())) {
        if (pandemic.gameState.getCurrentHand().containsAll(this.set)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean perform(Pandemic pandemic) {
    if (pandemic.gameState.getCurrentHand().size() > this.set.size()
        && !pandemic.gameState.isCured(desease) && pandemic.gameState.hasResearchCenter(origin)) {
      if (pandemic.gameState.getCurrentHand().containsAll(this.set)) {
        pandemic.gameState.getCurrentHand().removeAndDiscard(pandemic.gameState, this.set);
        GameUtil.log(pandemic, GameAction.logger,
            pandemic.gameState.getCurrentPlayer().getName() + " finds a Cure in " + origin.getName()
                + " for the " + desease.getName() + " desease.");
        return pandemic.findCure(desease);
      }
    }
    return false;
  }

  public void perform(LightGameStatus lightGameStatus) {
    lightGameStatus.hand.removeAll(this.set);
    lightGameStatus.curedDeseaseSet[this.desease.id] = Boolean.TRUE;
    lightGameStatus.actionCount -= this.actionCost;
  }



  public static Set<Set<CityCard>> getCombinations(Set<CityCard> set, int combinationSize) {
    if (combinationSize == 1) {
      Set<Set<CityCard>> resultSet = new HashSet<Set<CityCard>>();
      for (CityCard o : set) {
        Set<CityCard> subResultSet = new HashSet<CityCard>();
        subResultSet.add(o);
        resultSet.add(subResultSet);
      }
      return resultSet;
    } else {
      Set<Set<CityCard>> result = new HashSet<Set<CityCard>>();
      Set<CityCard> subset = new HashSet<>(set);
      for (CityCard o : set) {
        subset.remove(o);
        Set<Set<CityCard>> combinationSet = getCombinations(subset, combinationSize - 1);
        for (Set<CityCard> resultSet : combinationSet) {
          resultSet.add(o);
          result.add(resultSet);
        }
      }
      return result;
    }
  }

  public Set<CityCard> getSet() {
    return set;
  }

  public void setSet(Set<CityCard> set) {
    this.set = set;
  }

  public Desease getDesease() {
    return this.desease;
  }

  public int getPriority() {
    return Cure.priority;
  }

  public boolean isStrictPriority() {
    return Cure.strictPriority;
  }

  public void cancel(Pandemic pandemic) {
    super.cancel(pandemic);
    pandemic.gameState.unCureDesease(desease);
    pandemic.gameState.getCurrentHand().addAll(this.set);
    pandemic.gameState.getPlayerDeck().getDiscardPile().removeAll(this.set);
  }
}
