package game;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.mcts.MonteCarloTreeSearch;
import game.action.GameAction;
import game.action.superaction.SuperAction;
import util.GameUtil;

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
		mcts.rolloutPolicyRandom(gameStatus);
		//GameStatus save = gameStatus.clone();
		/*for(GameAction action : actionList) {
			action.perform(gameStatus);
		}*/
		/*save.updateValue();
		gameStatus.updateValue();
		if(gameStatus.value<=save.value) {
			gameStatus = save.clone();
		}*/
	}
	
	private void event() {
		
	}
	
	private void action() {
		mcts.rolloutPolicy(gameStatus);
		//GameStatus save = gameStatus.clone();
		/*for(GameAction action : actionList) {
			action.perform(gameStatus);
		}*/
		/*save.updateValue();
		gameStatus.updateValue();
		if(gameStatus.value<=save.value) {
			gameStatus = save.clone();
		}*/
	}

	public void setGameStatus(GameStatus clone) {
		this.gameStatus = clone;
	}

	public void setMCTS(MonteCarloTreeSearch monteCarloTreeSearch) {
		this.mcts = monteCarloTreeSearch;
	}
}
