package game;

import java.util.ArrayList;

public interface Game {
  public Game duplicate();

  public ArrayList<? extends Move> getMoves();

  public void perform(Move move);

  public boolean isOver();

  public boolean update();

  public GameState getGameState();

  public boolean isWin();
}
