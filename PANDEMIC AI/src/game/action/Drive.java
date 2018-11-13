package game.action;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import game.Game;
import objects.City;

public class Drive extends MoveAction {
	
	public Drive(Game game, City destination) {
		super(game,destination);
	}

	@Override
	public boolean perform() {
		if(isValid() && super.perform()) {
			return game.getCurrentPlayer().move(destination);
		}
		return false;
	}
	
	@Override
	public boolean isValid() {
		for(DefaultEdge edge : game.getMap().outgoingEdgesOf(game.getCurrentPlayer().getPosition())) {
			City target = game.getMap().getEdgeTarget(edge);
			if(target == destination) { return true;}
		}
		return false;
	}

	public static Set<MoveAction> getValidGameActionSet(Game game) {
		Set<MoveAction> driveSet = new HashSet<MoveAction>();
		for(DefaultEdge edge : game.getMap().outgoingEdgesOf(game.getCurrentPlayer().getPosition())) {
			driveSet.add(new Drive(game, game.getMap().getEdgeTarget(edge)));
		}
		return driveSet;
	}
}
