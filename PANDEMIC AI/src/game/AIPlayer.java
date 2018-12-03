package game;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.mcts.MCTSNode;
import ai.mcts.MonteCarloTreeSearch;
import game.action.GameAction;
import game.action.superaction.SuperAction;

public class AIPlayer extends Player {

	private static Logger logger = LogManager.getLogger(AIPlayer.class.getName());
	
	List<GameAction> currentActionList;
	int timeLimit;
	int shortTimeLimit;
	MonteCarloTreeSearch mcts;
	
	public AIPlayer(GameStatus gameStatus, int timeLimit, int shortTimeLimit) {
		super(gameStatus);
		this.timeLimit = timeLimit;
		this.shortTimeLimit = shortTimeLimit;
		mcts = new MonteCarloTreeSearch(gameStatus, timeLimit, new RolloutPlayer(gameStatus));
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
		mcts.setLimit(shortTimeLimit);
		mcts.setRoot(gameStatus);
		MCTSNode node = mcts.run();
		for(GameAction gameAction : node.getGameStatus().previousActionList) {
			gameAction.perform(gameStatus);
		}
		mcts.setLimit(timeLimit);
	}
	
	private void event() {
		
	}
	
	private void action() {
		mcts.setRoot(gameStatus);
		MCTSNode node = mcts.run();	
		for(SuperAction gameAction : node.previousSuperActionList) {
			gameAction.perform(gameStatus);
		}
	}
}
