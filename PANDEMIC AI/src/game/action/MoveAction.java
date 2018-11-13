package game.action;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import gameStatus.Game;
import objects.City;

public abstract class MoveAction extends GameAction {
City destination;
	
	public MoveAction(Game game, City destination) {
		super(game);
		this.destination=destination;
	}

	public boolean isValid() {
		return false;
	}

	public static Set<MoveAction> getValidGameActionSet(Game game) {
		return new HashSet<MoveAction>();
	}
}
