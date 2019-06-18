package pandemic.action;

import pandemic.material.City;
import pandemic.material.PlayedCharacter;

public abstract class ShareKnowledge extends StaticAction {

  PlayedCharacter playedCharacter;

  public ShareKnowledge(City origin, PlayedCharacter playedCharacter) {
    super(origin);
    this.playedCharacter = playedCharacter;
  }

  public PlayedCharacter getCharacter() {
    return this.playedCharacter;
  }
}
