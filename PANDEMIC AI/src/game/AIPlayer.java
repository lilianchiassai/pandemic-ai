package game;

import java.util.List;
import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.mcts.MCTSNode;
import ai.mcts.MonteCarloTreeSearch;
import game.GameRules.GameStep;
import game.action.GameAction;

public class AIPlayer extends Player {

	private static Logger logger = LogManager.getLogger(AIPlayer.class.getName());
	
	List<GameAction> currentActionList;
	int timeLimit;
	int shortTimeLimit;
	
	public AIPlayer(GameStatus gameStatus, int timeLimit, int shortTimeLimit) {
		super(gameStatus);
		this.timeLimit = timeLimit;
		this.shortTimeLimit = shortTimeLimit;
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
		MCTSNode<GameStatus> node = new MonteCarloTreeSearch(gameStatus, shortTimeLimit).run();
		currentActionList = node.getActionList();
		if(currentActionList.size() > 0) {
			GameAction gameAction = currentActionList.get(0);
			currentActionList.remove(gameAction);
			gameAction.perform(gameStatus);
			
		}	
	}
	
	private void event() {
		
	}
	
	private void action() {
		logger.info("Looking for action, turn : "+gameStatus.getTurnCounter()+" steps = "+gameStatus.getGameStep());
		MCTSNode<GameStatus> node = new MonteCarloTreeSearch(gameStatus, timeLimit).run();
		currentActionList = node.getActionList();
		if(currentActionList.size() > 0) {
			GameAction gameAction = currentActionList.get(0);
			currentActionList.remove(gameAction);
			gameAction.perform(gameStatus);
			
		}
	}
}
