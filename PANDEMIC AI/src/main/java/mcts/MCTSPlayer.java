package mcts;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.AbstractPlayer;
import game.Game;
import pandemic.action.GameAction;

public class MCTSPlayer extends AbstractPlayer {

  private static Logger logger = LogManager.getLogger(MCTSPlayer.class.getName());

  List<GameAction> currentActionList;
  Function<Game, Integer> timeLimit;
  MonteCarloTreeSearch mcts;

  public MCTSPlayer(MonteCarloTreeSearch mcts, Function<Game, Integer> timeLimit) {
    this.mcts = mcts;
    this.timeLimit = timeLimit;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    playPolicy((Game) evt.getNewValue());
  }

  public void playPolicy(Game game) {
    mcts.setLimit(this.timeLimit.apply(game));
    mcts.setRoot(game);
    MCTSNode node = mcts.run();
    game.perform(node.move);
  }
}
