package game;

public abstract class GameState<T extends GameProperties> {

  public T gameProperties;

  public GameState() {}

  protected abstract GameState<?> duplicate();

}
