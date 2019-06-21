package mcts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;
import game.AbstractPlayer;
import game.Game;
import game.Move;

public class RolloutPlayer extends AbstractPlayer {

  public RolloutPlayer() {}

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    rolloutPolicy((Game) evt.getNewValue());
  }

  public void rolloutPolicy(Game game) {
    ArrayList<? extends Move> moves = game.getMoves();
    int random = (int) (Math.random() * moves.size() * 10 / 10);
    game.perform(moves.get(random));
  }
}
