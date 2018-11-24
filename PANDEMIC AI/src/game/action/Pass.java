package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameProperties;
import game.GameStatus;
import objects.City;
import util.GameUtil;

public class Pass extends GameAction {

	public Pass() {
		super();
	}
	
	public boolean perform(GameStatus gameStatus) {
		GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" passes.");
		gameStatus.decreaseCurrentActionCount(gameStatus.getCurrentActionCount());
		return true;
	}
	
	public static Set<Pass> getValidGameActionSet(GameStatus gameStatus) {
		Set<Pass> passSet = new HashSet<Pass>();
		passSet.add(GameProperties.passAction);
		return passSet;
	}
}
