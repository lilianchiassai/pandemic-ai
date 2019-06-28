package pandemic.action;

import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.material.PlayedCharacter;
import pandemic.material.card.Card;
import pandemic.material.card.CityCard;
import pandemic.util.GameUtil;

public class Discard extends GameAction {

  public static int priority = 0;

  CityCard card;
  PlayedCharacter playedCharacter;

  public Discard(City origin, PlayedCharacter character, CityCard playerCard) {
    super(origin);
    this.actionCost = 0;
    this.card = playerCard;
    this.playedCharacter = character;
  }

  public boolean canPerform(Pandemic pandemic) {
    return pandemic.gameState.getCharacterHand(playedCharacter).contains(card);
  }

  @Override
  public boolean perform(Pandemic pandemic) {
    if (canPerform(pandemic) && super.perform(pandemic)) {
      GameUtil.log(pandemic, GameAction.logger,
          pandemic.gameState.getCurrentPlayer().getName() + " discards " + card.getTitle() + ".");
      return pandemic.gameState.getCharacterHand(playedCharacter).remove(card);
    }
    return false;
  }

  public void cancel(Pandemic pandemic) {
    pandemic.gameState.getCharacterHand(playedCharacter).add(card);
    pandemic.gameState.getCurrentHand().add(card);
    super.cancel(pandemic);
  }

  public void perform(LightGameStatus lightGameStatus) {
    lightGameStatus.hand.remove(this.card);
  }

  public Card getCard() {
    return this.card;
  }

  public int getPriority() {
    return Pass.priority;
  }

}
