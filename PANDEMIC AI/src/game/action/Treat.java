package game.action;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import gameStatus.Game;
import objects.Cube;
import objects.Desease;
import objects.card.Card;
import objects.card.CityCard;

public class Treat extends GameAction {
	Desease desease;
	
	
	
	public Treat(Game game, Desease desease) {
		super(game);
		this.desease=desease;
	}

	public boolean perform() {
		Set<Cube> cubeSet = game.getCurrentPlayer().getPosition().getCubeSet(desease);
		if(cubeSet != null && super.perform()) {
			if(desease.isCured()) {
				game.getCurrentPlayer().getPosition().removeCube(cubeSet);
				game.getReserve().addCube(cubeSet);
				return true;
			} else {
				Iterator<Cube> it = cubeSet.iterator();
				if(it.hasNext()) {
					Cube cube = it.next();
					game.getCurrentPlayer().getPosition().removeCube(cube);
					game.getReserve().addCube(cube);
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValid() {
		Set<Cube> cubeSet = game.getCurrentPlayer().getPosition().getCubeSet(desease);
		return cubeSet != null && cubeSet.size()>0;
	}

	public static Set<Treat> getValidGameActionSet(Game game) {
		Set<Treat> treatSet = new HashSet<Treat>();
		for(Desease desease : game.getCurrentPlayer().getPosition().getDeseaseSet()) {
			treatSet.add(new Treat(game, desease));
		}
		return treatSet;
	}
}
