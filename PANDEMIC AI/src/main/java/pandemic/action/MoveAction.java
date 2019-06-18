package pandemic.action;

import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;

public abstract class MoveAction extends GameAction {

	public City destination;
	
	public MoveAction(City origin, City destination) {
		super(origin);
		this.destination=destination;
	}
	
	public boolean perform(Pandemic pandemic) {
		return super.perform(pandemic);
	}
	
	public void cancel(Pandemic pandemic) {
		super.cancel(pandemic);
		pandemic.gameState.setCharacterPosition(pandemic.gameState.getCurrentPlayer(), origin);
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.actionCount-=this.actionCost;
		lightGameStatus.position=this.destination;
	}

	public City getDestination() {
		return this.destination;
	}
	

}
