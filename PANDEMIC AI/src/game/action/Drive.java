package game.action;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import game.GameProperties;
import game.GameStatus;
import objects.City;

public class Drive extends MoveAction {
	
	public Drive(GameStatus gameStatus, City destination) {
		super(destination);
	}

	@Override
	public boolean perform(GameStatus gameStatus) {
		for(DefaultEdge edge : GameProperties.map.outgoingEdgesOf(gameStatus.getCurrentPlayer().getPosition())) {
			City target = GameProperties.map.getEdgeTarget(edge);
			if(target == destination && super.perform(gameStatus)) {
				return gameStatus.getCurrentPlayer().setPosition(destination);
			}
		}
		return false;
	}
	

	public static Set<MoveAction> getValidGameActionSet(GameStatus gameStatus) {
		Set<MoveAction> driveSet = new HashSet<MoveAction>();
		for(DefaultEdge edge : GameProperties.map.outgoingEdgesOf(gameStatus.getCurrentPlayer().getPosition())) {
			driveSet.add(new Drive(gameStatus, GameProperties.map.getEdgeTarget(edge)));
		}
		return driveSet;
	}
}
