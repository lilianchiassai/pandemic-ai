package game;

import java.beans.PropertyChangeSupport;

public class Engine<T extends Game> {

  final private PropertyChangeSupport support;
  final private String fireEvent = "";

  public Engine(Player player) {
    this.support = new PropertyChangeSupport(this);
    this.support.addPropertyChangeListener(player);
  }

  public T run(T game) {
    while (!game.isOver()) {
      if (game.update()) {
        notifyPlayers(game);
      }
    }
    return game;
  }

  public void notifyPlayers(T game) {
    support.firePropertyChange(fireEvent, null, game);
  }
}
