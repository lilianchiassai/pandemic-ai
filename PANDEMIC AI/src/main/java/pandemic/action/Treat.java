package pandemic.action;

import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.util.GameUtil;

public class Treat extends StaticAction {

  public static int priority = 3;

  Desease desease;
  boolean healAll;
  int strength;

  public Treat(City origin, Desease desease) {
    super(origin);
    this.desease = desease;
    this.strength = 1;
    this.actionCost = 1;
    this.healAll = false;
  }

  public Treat(City origin, Desease desease, int strength) {
    super(origin);
    this.desease = desease;
    this.strength = strength;
    this.actionCost = strength;
    this.healAll = false;
  }

  public Treat(City origin, Desease desease, int strength, int cost) {
    super(origin);
    this.desease = desease;
    this.strength = strength;
    this.actionCost = cost;
    this.healAll = strength > cost;
  }

  public boolean canPerform(Pandemic pandemic) {
    if (pandemic.gameState.getCurrentActionCount() >= this.actionCost) {
      if (healAll) {
        if (pandemic.gameState.getCityCubeCount(pandemic.gameState.getCurrentPlayerPosition(),
            desease) == strength && pandemic.gameState.isCured(desease)) {
          return true;
        }
        return false;
      } else if (pandemic.gameState.getCityCubeCount(origin, desease) >= strength) {
        return true;
      }
    }
    return false;
  }

  public boolean perform(Pandemic pandemic) {
    if (canPerform(pandemic) && pandemic.gameState.getCityCubeCount(origin, desease) >= strength) {
      GameUtil.log(pandemic, GameAction.logger, pandemic.gameState.getCurrentPlayer().getName()
          + " treats " + desease.getName() + " desease in " + origin.getName());
      if (healAll && pandemic.gameState.getCityCubeCount(origin, desease) == strength
          && pandemic.gameState.isCured(desease) && super.perform(pandemic)) {
        pandemic.gameState.removeCube(origin, desease, strength);
        pandemic.checkEradicated(desease);
        return true;
      }
      if (!healAll && super.perform(pandemic)) {
        return pandemic.gameState.removeCube(pandemic.gameState.getCurrentPlayerPosition(), desease,
            strength);
      }
    }
    return false;
  }

  public void cancel(Pandemic pandemic) {
    super.cancel(pandemic);
    pandemic.infect(origin, strength, desease);
  }

  public Desease getDesease() {
    return this.desease;
  }

  public int getPriority() {
    return Treat.priority;
  }

  public int getStrength() {
    return this.strength;
  }

  public boolean isHealAll() {
    return this.healAll;
  }

  public String toString() {
    return "Treat (" + strength + ")";
  }
}
