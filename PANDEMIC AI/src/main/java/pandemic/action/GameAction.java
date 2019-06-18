package pandemic.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;

public abstract class GameAction {
	
	protected static Logger logger = LogManager.getLogger(GameAction.class.getName());
	
	public static int priority;
	public int actionCost;
	
	public City origin;
	
	public GameAction(City origin) {
		this.actionCost = 1;
		this.origin=origin;
	}
	
	public boolean canPerform(Pandemic pandemic) {
		return pandemic.gameState.getCurrentActionCount() >= this.actionCost;
	}
	
	public boolean perform(Pandemic pandemic) {
		if(pandemic.gameState.getCurrentActionCount() - this.actionCost >=0 ) {
			pandemic.gameState.addToActionList(this);
			pandemic.gameState.decreaseCurrentActionCount(this.actionCost);
			return true;
		}
		return false;
	}
	
	public void cancel(Pandemic pandemic) {
		pandemic.gameState.increaseCurrentActionCount(this.actionCost);
		pandemic.gameState.removeFromActionList(this);
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.actionCount-=this.actionCost;
	}

	public int getCost() {
		return this.actionCost;
	}
	
	public City getOrigin() {
		return this.origin;
	}
	
	public abstract int getPriority();
	
}
