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
		try {
			throw new Exception();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.actionCount-=this.actionCost;
	}

	public boolean canPerform(GameStatus gameStatus) {
		return gameStatus.getCurrentActionCount() >= this.actionCost;
	}
	
	public boolean cancel(GameStatus gameStatus) {
		if(GameProperties.actionCount - gameStatus.getCurrentActionCount() >=0 && gameStatus.removeFromActionList(this)) {
			gameStatus.increaseCurrentActionCount(this.actionCost);
			return true;
		}
		return false;
	}

	public int getCost() {
		return this.actionCost;
	}
}
