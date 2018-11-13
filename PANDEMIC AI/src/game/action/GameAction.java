package game.action;

import java.util.Set;

import gameStatus.Game;

public abstract class GameAction {
	
	public Game game;
	public int actionCost;
	
	public GameAction(Game game) {
		this.game=game;
		this.actionCost = 1;
	}
	
	public boolean perform() {
		if(game.getCurrentPlayer().getCurrentActionCount() - this.actionCost >=0 ) {
			game.getCurrentPlayer().setCurrentActionCount(game.getCurrentPlayer().getCurrentActionCount() - this.actionCost);
			return true;
		}
		return false;
	}
}
