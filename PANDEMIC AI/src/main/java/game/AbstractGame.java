package game;

public abstract class AbstractGame<S extends GameState<?>> implements Game {
  public S gameState;

  @Override
  public void perform(Move move) {
    move.perform(this);
  }
}
