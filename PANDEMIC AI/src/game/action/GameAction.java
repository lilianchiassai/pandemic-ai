package game.action;

import java.io.Serializable;

import game.GameStatus;

public abstract class GameAction implements Serializable {
	
	public int actionCost;
	
	public GameAction() {
		this.actionCost = 1;
	}
	
	public boolean perform(GameStatus gameStatus) {
		if(gameStatus.getCurrentPlayer().getCurrentActionCount() - this.actionCost >=0 ) {
			gameStatus.getCurrentPlayer().setCurrentActionCount(gameStatus.getCurrentPlayer().getCurrentActionCount() - this.actionCost);
			return true;
		}
		return false;
	}

	public boolean canPerform(GameStatus gameStatus) {
		if(gameStatus.getCurrentPlayer().getCurrentActionCount() - this.actionCost >=0 ) {
			return true;
		}
		return false;
	}
}
