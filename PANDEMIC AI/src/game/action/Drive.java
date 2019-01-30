package game.action;

import java.util.Set;

import game.GameProperties;
import game.GameStatus;
import game.LightGameStatus;
import objects.City;
import util.GameUtil;

public class Drive extends MoveAction {
	
	public Drive(City destination) {
		super(destination);
	}

	@Override
	public boolean perform(GameStatus gameStatus) {
		for(City target : gameStatus.getCurrentCharacterPosition().getNeighbourSet()) {
			if(target == destination && super.perform(gameStatus)) {
				GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" drives from "+gameStatus.getCurrentCharacterPosition().getName()+" to "+ destination.getName());
				return gameStatus.setCharacterPosition(gameStatus.getCurrentPlayer(), destination);
			}
		}
		return false;
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.hand.remove(destination.getCityCard());
		lightGameStatus.position = destination;
		lightGameStatus.actionCount-=this.actionCost;
	}

	/*public static Set<Drive> getValidGameActionSet(GameStatus gameStatus) {
		return gameStatus.getCurrentCharacterPosition().getDriveActionSet();
	}*/

	public static Set<Drive> getDefaultGameActionSet() {
		return GameProperties.driveActionSet;
	}
}
