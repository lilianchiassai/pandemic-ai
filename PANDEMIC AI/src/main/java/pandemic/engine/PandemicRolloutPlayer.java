package pandemic.engine;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.Game;
import mcts.RolloutPlayer;
import pandemic.Pandemic;
import pandemic.action.ActionSerie;

public class PandemicRolloutPlayer extends RolloutPlayer {

  private static Logger logger = LogManager.getLogger(PandemicRolloutPlayer.class.getName());

  public PandemicRolloutPlayer() {}

  @Override
  public void rolloutPolicy(Game game) {
    switch (((Pandemic) game).getGameState().getGameStep()) {
      case play:
        super.rolloutPolicy(game);
        break;
      case discard:
        discard((Pandemic) game);
        break;
      default:
        break;
    }
  }

  private void discard(Pandemic game) {
    ArrayList<ActionSerie> moves = game.getMoves();
    int random = (int) (Math.random() * moves.size() * 10 / 10);
    game.perform(moves.get(random));
  }

}
