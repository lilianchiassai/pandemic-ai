package pandemic.action;

import pandemic.LightGameStatus;
import pandemic.Pandemic;
import pandemic.material.City;
import pandemic.util.GameUtil;

public class ShuttleFlight extends MoveAction {
	
	public static int priority = 2;
	
	public ShuttleFlight(City origin, City destination) {
		super(origin, destination);
	}
	
	public boolean canPerform(Pandemic pandemic) {
		if(pandemic.gameState.getCurrentActionCount() >= this.actionCost) {
			if(pandemic.gameState.hasResearchCenter(origin) && pandemic.gameState.hasResearchCenter(destination)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean perform(Pandemic pandemic) {
		if(pandemic.gameState.hasResearchCenter(pandemic.gameState.getCurrentPlayerPosition()) && pandemic.gameState.hasResearchCenter(destination) && super.perform(pandemic)) {
			GameUtil.log(pandemic, GameAction.logger, pandemic.gameState.getCurrentPlayer().getName()+" flies in a shuttle from "+origin.getName()+" to "+ destination.getName());
			return pandemic.gameState.setCharacterPosition(pandemic.gameState.getCurrentPlayer(), destination);
		}
		return false;
	}
	
	public void cancel(Pandemic pandemic) {
		super.cancel(pandemic);
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.position = this.destination;
		lightGameStatus.actionCount-=this.actionCost;
	}
	
	public int getPriority() {
		return ShuttleFlight.priority;
	}
}