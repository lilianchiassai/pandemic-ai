package game;

import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameRules.GameStep;
import util.GameUtil;

public class GameEngine extends Observable{
	private static Logger logger = LogManager.getLogger(GameEngine.class.getName());
	
	GameStatus gameStatus;
	GameProperties gameProperties;
	
	
	
	public GameEngine(GameProperties gameProperties, GameStatus gameStatus, boolean humanPlayer) {
		this.gameProperties=gameProperties;
		this.gameStatus=gameStatus;
		if(humanPlayer) {
			this.addObserver(new HumanPlayer(this.gameStatus));
		} else {
			this.addObserver(new AIPlayer(this.gameStatus,400,100));
		}
	}

	public static void main(String[] args) {
		//Starting game
		GameEngine gameEngine = new GameEngine(new GameProperties(), new GameStatus(4,5, GameUtil.getCity("Atlanta")), false);
		gameEngine.run();
	}
	
	public void run() {
		while(!gameStatus.isOver()) {
			if(gameStatus.getGameStep() == GameRules.GameStep.turnStart) {
				logger.info("Turn "+gameStatus.getTurnCounter());
			}
			GameRules.updateStatus(gameStatus);
			GameRules.playStatus(this);
		}
		return;
	}
	
	public void notifyGameStep(GameStep gameStep) {
		this.setChanged();
		notifyObservers(gameStep);
	}

	public GameStatus getGameStatus() {
		return this.gameStatus;
	}
}
