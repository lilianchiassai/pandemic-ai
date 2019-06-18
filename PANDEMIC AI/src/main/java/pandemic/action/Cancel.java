package pandemic.action;

import pandemic.Pandemic;
import pandemic.material.City;

public class Cancel extends GameAction {

	public Cancel(City origin) {
		super(origin);
	}
	
	public boolean canPerform(Pandemic pandemic) {
		return pandemic.gameState.getPreviousActionList().size()>0;
	}
	
	public boolean perform(Pandemic pandemic) {
		if(canPerform(pandemic)) {
			pandemic.gameState.getPreviousActionList().getLast().cancel(pandemic);
			return true;
		}
		return false;
	}

	public static int priority = -1;
	
	@Override
	public int getPriority() {
		return Cancel.priority;
	}

}
