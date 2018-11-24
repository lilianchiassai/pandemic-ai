package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
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
	

	public static Set<Drive> getValidGameActionSet(GameStatus gameStatus) {
		return gameStatus.getCurrentCharacterPosition().getDriveActionSet();
	}

	
}
