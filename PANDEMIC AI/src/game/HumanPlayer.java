package game;

import java.util.Observable;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.action.Build;
import game.action.CharterFlight;
import game.action.Cure;
import game.action.DirectFlight;
import game.action.Discard;
import game.action.Drive;
import game.action.GameAction;
import game.action.ShareKnowledge;
import game.action.ShuttleFlight;
import game.action.Treat;
import objects.Character;
import objects.City;
import objects.Desease;
import util.GameUtil;

public class HumanPlayer extends Player {
	
	private static Logger logger = LogManager.getLogger(HumanPlayer.class.getName());

	public HumanPlayer(GameStatus gameStatus) {
		super(gameStatus);
	}
	
	@Override
	public void update(Observable obs, Object gameStep) {
		switch((GameRules.GameStep) gameStep) {
			case play:
				action(((GameEngine)obs).getGameStatus());
				break;
			case discard:
				discard(((GameEngine)obs).getGameStatus());
				break;
			case event:
				event(((GameEngine)obs).getGameStatus());
				break;
		}
	}
	
	private void discard(GameStatus gameStatus) {
		for(Character character : gameStatus.getCharacterList()) {
			while (character.getHand().getCardDeck().size() > 7) {
				logger.info("Player "+character.getName()+" has too many cards, please discard.");
				logger.info("Your cards are "+character.getHand().toString());
				logger.info("Type the name of the card to discard.");
				Scanner scanner = new Scanner(System.in);
				String str = scanner.nextLine();
				Discard discard = new Discard(character, null);
				while(!discard.perform(gameStatus)) {
					logger.info("Invalid name of card please try again");
					scanner = new Scanner(System.in);
					str = scanner.nextLine();
					discard = new Discard(character, character.getHand().getCard(str));
				}
			}
		}		
	}
	
	private void event(GameStatus gameStatus) {
		
	}
	
	private void action(GameStatus gameStatus) {
		Character character = gameStatus.getCurrentPlayer();
		logger.info("You are in "+character.getPosition().getName()+". There are "+character.getPosition().getCubeSet(character.getPosition().getDesease()).size()+" cubes.");
		logger.info("Your cards are "+character.getHand().toString());
		logger.info(character.getCurrentActionCount() +" actions remaining.");
		logger.info("Your only possible action are : drive-city,directFlight-city,charterFlight-city,shuttleFlight-city, treatDesease, shareKnowledge-player, build, cure-desease");
		
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		GameAction gameAction = null;
		if(str.startsWith("drive")) {
			String cityName = str.split("-")[1];
			City city = GameUtil.getCity(cityName);
			gameAction = new Drive(gameStatus, city);
		} else if(str.startsWith("directFlight")) {
			String cityName = str.split("-")[1];
			City city = GameUtil.getCity(cityName);
			gameAction = new DirectFlight(gameStatus, city);
		} else if(str.startsWith("charterFlight")) {
			String cityName = str.split("-")[1];
			City city = GameUtil.getCity(cityName);
			gameAction = new CharterFlight(gameStatus, city);
		} else if(str.startsWith("shuttleFlight")) {
			String cityName = str.split("-")[1];
			City city = GameUtil.getCity(cityName);
			gameAction = new ShuttleFlight(gameStatus, city);
		} else if (str.startsWith("treat")) {
			String deseaseName = str.split("-")[1];
			Desease desease = GameUtil.getDesease(deseaseName);
			gameAction = new Treat(desease);
		} else if (str.startsWith("shareKnowledge")) {
			String playerName = str.split("-")[1];
			Character otherPlayer = GameUtil.getPlayer(playerName);
			gameAction = new ShareKnowledge(otherPlayer);
		} else if (str.startsWith("cure")) {
			String deseaseName = str.split("-")[1];
			Desease desease = GameUtil.getDesease(deseaseName);
			//TODO
			gameAction = new Cure(desease, null);
		} else if (str.startsWith("build")) {
			gameAction = new Build();
		} else if (str.startsWith("pass")) {
			character.setCurrentActionCount(0);
		} else {
			logger.info("Wrong action please try again");
		}
		if(!gameAction.perform(gameStatus)) {
			logger.info("Invalid action");
		}
	}

}
