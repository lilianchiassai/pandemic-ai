package game.action;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameRules;
import game.GameStatus;

public abstract class GameAction implements Serializable {
	
	protected static Logger logger = LogManager.getLogger(GameAction.class.getName());
	
	public int actionCost;
	
	public GameAction() {
		this.actionCost = 1;
	}
	
	public boolean perform(GameStatus gameStatus) {
		if(gameStatus.getCurrentActionCount() - this.actionCost >=0 ) {
			gameStatus.decreaseCurrentActionCount(this.actionCost);
			return true;
		}
		return false;
	}

	public boolean canPerform(GameStatus gameStatus) {
		return gameStatus.getCurrentActionCount() >= this.actionCost;
	}

	public int getCost() {
		return this.actionCost;
	}
}
