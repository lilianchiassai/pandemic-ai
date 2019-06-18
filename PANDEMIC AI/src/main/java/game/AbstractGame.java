package game;

public abstract class AbstractGame<S extends GameState> implements Game {
  public S gameState;

  public void perform(Move move) {
    move.perform(this);
  }
}
