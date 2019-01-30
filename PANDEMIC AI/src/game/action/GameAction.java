package game.action;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameProperties;
import game.GameStatus;
import game.LightGameStatus;

public abstract class GameAction implements Serializable {
	
	protected static Logger logger = LogManager.getLogger(GameAction.class.getName());
	
	public int actionCost;
	
	public GameAction() {
		this.actionCost = 1;
	}
	
	public boolean perform(GameStatus gameStatus) {
		if(gameStatus.getCurrentActionCount() - this.actionCost >=0 ) {
			gameStatus.addToActionList(this);
			gameStatus.decreaseCurrentActionCount(this.actionCost);
			return true;
		}
		return false;
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.actionCount-=this.actionCost;
	}

	public boolean canPerform(GameStatus gameStatus) {
		return gameStatus.getCurrentActionCount() >= this.actionCost;
	}

	public int getCost() {
		return this.actionCost;
	}
}
