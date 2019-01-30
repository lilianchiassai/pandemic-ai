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
	int strength;
	
	public Treat(Desease desease, int strength) {
		super();
		this.desease=desease;
		this.strength=strength;
	}
	
	public Treat(Desease desease, int strength, int cost) {
		super();
		this.desease=desease;
		this.strength=strength;
		this.actionCost=cost;
	}
	
	public String toString() {
		return "Treat ("+strength+")";
	}

	public boolean perform(GameStatus gameStatus) {
		Set<Cube> cubeSet = gameStatus.getCityCubeSet(gameStatus.getCurrentCharacterPosition(), desease);
		if(cubeSet.size()>0 && super.perform(gameStatus)) {
			GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" treats "+desease.getName()+" desease in "+gameStatus.getCurrentCharacterPosition().getName());
			if(GameRules.isCured(gameStatus, desease) && cubeSet.size() == strength) {
				gameStatus.removeAndReserveCubeSet(gameStatus.getCurrentCharacterPosition(), desease);
				
				GameRules.checkEradicated(gameStatus, desease);
				return true;
			} else {
				if(cubeSet.size()>0 && strength ==1) {
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
				if(GameRules.isCured(gameStatus, desease)) {
					treatSet.add(GameProperties.treatAction.get(desease).get(gameStatus.getCityCubeSet(gameStatus.getCurrentCharacterPosition(), desease).size()-1));
				} else {
					treatSet.add(GameProperties.treatAction.get(desease).get(0));
				}
				
			}
		}
		return treatSet;
	}
	
	public static Set<Treat> getDefaultGameActionSet() {
		Set<Treat> treatSet = new HashSet<Treat>();
		for(Desease desease : GameProperties.deseaseSet) {
			treatSet.add(GameProperties.treatAction.get(desease).get(0));
		}
		return treatSet;
	}
	
	public Desease getDesease() {
		return this.desease;
	}
}
