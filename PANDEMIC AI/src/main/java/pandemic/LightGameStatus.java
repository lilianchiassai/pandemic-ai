package pandemic;

import java.util.HashSet;
import java.util.Set;
import pandemic.material.City;
import pandemic.material.card.Card;

public class LightGameStatus {
  public State state;

  public Set<Card> hand;
  public boolean[] researchCenterCity;
  public boolean[] curedDeseaseSet;
  public City position;
  public int actionCount;


  public LightGameStatus(State state) {
    this.state = state;
    hand = new HashSet<Card>(state.getCurrentHand());
    researchCenterCity = state.cityBuilt.clone();
    position = state.getCurrentPlayerPosition();
    actionCount = state.getCurrentActionCount();
    curedDeseaseSet = state.curedDeseases.clone();
  }

  public LightGameStatus(LightGameStatus lightGameStatus) {
    state = lightGameStatus.state;
    hand = new HashSet<Card>(lightGameStatus.hand);
    researchCenterCity = state.cityBuilt.clone();
    position = lightGameStatus.position;
    actionCount = lightGameStatus.actionCount;
    curedDeseaseSet = state.curedDeseases.clone();
  }

  public LightGameStatus lightClone() {
    return new LightGameStatus(this);
  }
}
