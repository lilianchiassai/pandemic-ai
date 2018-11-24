package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameProperties;
import game.GameRules;
import game.GameStatus;
import objects.Cube;
import objects.Desease;
import util.GameUtil;

public class Treat extends GameAction {
	Desease desease;
	
	
	
	public Treat(Desease desease) {
		super();
		this.desease=desease;
	}

	public boolean perform(GameStatus gameStatus) {
		Set<Cube> cubeSet = gameStatus.getCityCubeSet(gameStatus.getCurrentCharacterPosition(), desease);
		if(cubeSet.size()>0 && super.perform(gameStatus)) {
			GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" treats "+desease.getName()+" desease in "+gameStatus.getCurrentCharacterPosition().getName());
			if(GameRules.isCured(gameStatus, desease)) {
				gameStatus.removeAndReserveCubeSet(gameStatus.getCurrentCharacterPosition(), desease);
				GameRules.checkEradicated(gameStatus, desease);
				return true;
			} else {
				if(cubeSet.size()>0) {
					return gameStatus.removeAndReserveCube(gameStatus.getCurrentCharacterPosition(), desease);
				}
			}
		}
		return false;
	}

	public static Set<Treat> getValidGameActionSet(GameStatus gameStatus) {
		Set<Treat> treatSet = new HashSet<Treat>();
		for(Desease desease : GameProperties.deseaseSet) {
			if(gameStatus.getCityCubeSet(gameStatus.getCurrentCharacterPosition(), desease).size() > 0) {
				treatSet.add(GameProperties.treatAction.get(desease));
			}
		}
		return treatSet;
	}
}
