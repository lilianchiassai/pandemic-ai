package game;

public interface Move<T extends Game> {

  boolean perform(T abstractGame);

}
