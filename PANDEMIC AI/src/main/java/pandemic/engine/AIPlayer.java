package pandemic.engine;

import java.util.ArrayList;
import game.Game;
import mcts.MCTSPlayer;
import mcts.MonteCarloTreeSearch;
import pandemic.Pandemic;
import pandemic.Pandemic.GameStep;
import pandemic.action.ActionSerie;

public class AIPlayer extends MCTSPlayer {

  public AIPlayer(MonteCarloTreeSearch mcts, int defaultLimit, int discardLimit) {
    super(mcts, (Game game) -> {
      if (((Pandemic) game).getGameState().getGameStep() == GameStep.discard)
        return discardLimit;
      return defaultLimit;
    });
  }

  @Override
  public void playPolicy(Game game) {
    switch (((Pandemic) game).getGameState().getGameStep()) {
      case play:
        super.playPolicy(game);
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
