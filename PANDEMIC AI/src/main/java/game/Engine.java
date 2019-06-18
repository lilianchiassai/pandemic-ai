package game;

import java.util.Observable;

public class Engine extends Observable {

  public Engine(Player player) {
    this.addObserver(player);
  }

  public Game run(Game game) {
    while (!game.isOver()) {
      if (game.update()) {
        notifyPlayers(game);
      }
    }
    return game;
  }

  public void notifyPlayers(Game game) {
    this.setChanged();
    notifyObservers(game);
  }
}
