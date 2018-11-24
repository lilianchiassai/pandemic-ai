package game;

import java.util.List;
import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.mcts.MCTSNode;
import ai.mcts.MonteCarloTreeSearch;
import game.action.GameAction;

public class RolloutPlayer extends Player {
private static Logger logger = LogManager.getLogger(AIPlayer.class.getName());
	
	List<GameAction> currentActionList;
	MonteCarloTreeSearch mcts;
	
	public RolloutPlayer(GameStatus gameStatus) {
		super(gameStatus);
	}
	
	@Override
	public void update(Observable obs, Object gameStep) {
		switch((GameRules.GameStep) gameStep) {
			case play:
				action();
				break;
			case discard:
				discard();
				break;
			case event:
				event();
				break;
		}
	}
	
	private void discard() {
		GameAction action = mcts.rolloutPolicy(gameStatus);
		action.perform(gameStatus);
	}
	
	private void event() {
		
	}
	
	private void action() {
		GameAction action = mcts.rolloutPolicy(gameStatus);
		action.perform(gameStatus);
	}

	public void setGameStatus(GameStatus clone) {
		this.gameStatus = clone;
	}

	public void setMCTS(MonteCarloTreeSearch monteCarloTreeSearch) {
		this.mcts = monteCarloTreeSearch;
	}
}
