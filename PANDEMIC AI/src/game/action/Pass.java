package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameProperties;
import game.GameStatus;
import game.LightGameStatus;
import objects.City;
import util.GameUtil;

public class Pass extends GameAction {
	
	public Pass(int actionCost) {
		super();
		this.actionCost = actionCost;
	}
	
	public boolean perform(GameStatus gameStatus) {
		GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" passes.");
		super.perform(gameStatus);
		return true;
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.actionCount-=this.actionCost;
	}
	
	public static Set<Pass> getValidGameActionSet(GameStatus gameStatus) {
		Set<Pass> passSet = new HashSet<Pass>();
		passSet.add(GameProperties.passActionList.get(gameStatus.getCurrentActionCount()));
		return passSet;
	}
}
