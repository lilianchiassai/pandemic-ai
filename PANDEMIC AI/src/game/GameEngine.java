package game;

import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameRules.GameStep;
import tree.Node;
import util.GameUtil;

public class GameEngine extends Observable{
	private static Logger logger = LogManager.getLogger(GameEngine.class.getName());
	
	GameStatus gameStatus;
	static GameProperties gameProperties;
	
	
	
	public GameEngine(GameStatus gameStatus, Player player) {
		this.gameStatus=gameStatus;
		this.addObserver(player);
	}

	public static void main(String[] args) {
		//Starting game
		gameProperties = new GameProperties();
		
		//Node actionRoot = GameRules.expandActionTree(GameUtil.getCity("Atlanta"));
		//System.out.println(GameRules.getGameActionList(actionRoot).size());
		//GameRules.filterActionTree(actionRoot,actionRoot);
		//System.out.println(GameRules.getGameActionList(actionRoot).size());
		GameStatus gameStatus = new GameStatus(2,3, GameUtil.getCity("Atlanta"));
		GameEngine gameEngine = new GameEngine(gameStatus, new AIPlayer(gameStatus,3000,200));
		gameEngine.run();
		if(gameStatus.isWin()) {
			logger.info("GG");
		} else {
			logger.info("You lose");
		}
		System.out.println("Number of plays : "+GameProperties.visitCount);
		System.out.println("Number of victory : "+GameProperties.victoryCount);
	}
	
	public GameStatus run() {
		while(!gameStatus.isOver()) {
			if(gameStatus.getGameStep() == GameRules.GameStep.turnStart) {
				logger.info("Turn "+gameStatus.getTurnCounter());
			}
			GameRules.updateStatus(this.gameStatus);
			GameRules.playStatus(this);
		}
		return gameStatus;
	}
	
	public void notifyGameStep(GameStep gameStep) {
		this.setChanged();
		notifyObservers(gameStep);
	}

	public GameStatus getGameStatus() {
		return this.gameStatus;
	}
}
