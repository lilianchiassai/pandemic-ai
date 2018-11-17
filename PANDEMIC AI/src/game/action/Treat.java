package game.action;

import java.util.HashSet;
import java.util.Set;

import game.GameStatus;
import objects.Cube;
import objects.Desease;

public class Treat extends GameAction {
	Desease desease;
	
	
	
	public Treat(Desease desease) {
		super();
		this.desease=desease;
	}

	public boolean perform(GameStatus gameStatus) {
		Set<Cube> cubeSet = gameStatus.getCurrentPlayer().getPosition().getCubeSet(desease);
		if(cubeSet != null && super.perform(gameStatus)) {
			if(desease.isCured()) {
				return gameStatus.removeAndReserveCubeSet(gameStatus.getCurrentPlayer().getPosition(), desease);
			} else {
				if(cubeSet.size()>0) {
					return gameStatus.removeAndReserveCube(gameStatus.getCurrentPlayer().getPosition(), desease);
				}
			}
		}
		return false;
	}

	public static Set<Treat> getValidGameActionSet(GameStatus gameStatus) {
		Set<Treat> treatSet = new HashSet<Treat>();
		for(Desease desease : gameStatus.getCurrentPlayer().getPosition().getDeseaseSet()) {
			treatSet.add(new Treat(desease));
		}
		return treatSet;
	}
}
