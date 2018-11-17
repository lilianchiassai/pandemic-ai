package game;

import java.util.Observer;

public abstract class Player implements Observer {

	GameStatus gameStatus;
	
	public Player(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
}
