package game;

import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.action.Cure;
import game.action.Discard;
import game.action.GameAction;
import game.action.GiveKnowledge;
import game.action.Pass;
import game.action.Treat;
import objects.Character;
import objects.City;
import objects.Desease;
import objects.card.Card;
import objects.card.Hand;
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
		for(Hand hand : gameStatus.getCharacterHandList()) {
			while (hand.getCardDeck().size() > 7) {
				logger.info("Player "+hand.getCharacter().getName()+" has too many cards, please discard.");
				logger.info("Your cards are "+hand.toString());
				logger.info("Type the name of the card to discard.");
				Scanner scanner = new Scanner(System.in);
				String str = scanner.nextLine();
				Discard discard = new Discard(hand.getCharacter(), hand.getCard(str));
				while(!discard.perform(gameStatus)) {
					logger.info("Invalid name of card please try again");
					scanner = new Scanner(System.in);
					str = scanner.nextLine();
					discard = new Discard(hand.getCharacter(), hand.getCard(str));
				}
			}
		}		
	}
	
	private void event(GameStatus gameStatus) {
		
	}
	
	private void action(GameStatus gameStatus) {
		Character character = gameStatus.getCurrentPlayer();
		logger.info("You are in "+gameStatus.getCharacterPosition(character).getName()+". There are "+ gameStatus.getCityCubeSet(gameStatus.getCharacterPosition(character), gameStatus.getCharacterPosition(character).getDesease()).size()+" cubes.");
		logger.info("Your cards are "+gameStatus.getCurrentHand().toString());
		logger.info(gameStatus.getCurrentActionCount() +" actions remaining.");
		logger.info("Your only possible action are : drive-city,directFlight-city,charterFlight-city,shuttleFlight-city, treatDesease, shareKnowledge-player, build, cure-desease");
		
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		GameAction gameAction = null;
		if(str.startsWith("drive")) {
			String cityName = str.split("-")[1];
			City city = GameUtil.getCity(cityName);
			gameAction = city.getDriveAction();
		} else if(str.startsWith("directFlight")) {
			String cityName = str.split("-")[1];
			City city = GameUtil.getCity(cityName);
			gameAction = gameStatus.getCurrentCharacterPosition().getDirectFlightAction(city);
		} else if(str.startsWith("charterFlight")) {
			String cityName = str.split("-")[1];
			City city = GameUtil.getCity(cityName);
			gameAction = gameStatus.getCurrentCharacterPosition().getCharterFlightAction(city);
		} else if(str.startsWith("shuttleFlight")) {
			String cityName = str.split("-")[1];
			City city = GameUtil.getCity(cityName);
			gameAction = gameStatus.getCurrentCharacterPosition().getShuttleFlightAction(city);
		} else if (str.startsWith("treat")) {
			String deseaseName = str.split("-")[1];
			Desease desease = GameUtil.getDesease(deseaseName);
			gameAction = new Treat(desease, GameRules.isCured(gameStatus, desease) ? gameStatus.getCityCubeSet(gameStatus.getCurrentCharacterPosition(), desease).size() : 1);
		} else if (str.startsWith("giveKnowledge")) {
			String playerName = str.split("-")[1];
			Character otherPlayer = GameUtil.getPlayer(playerName);
			gameAction = new GiveKnowledge(otherPlayer);
		} else if (str.startsWith("cure")) {
			String deseaseName = str.split("-")[1];
			Desease desease = GameUtil.getDesease(deseaseName);
			String cardNames = str.split("-")[2];
			HashSet<Card> cardSet = new HashSet<Card>();
			for(String cityName : cardNames.split(",")) {
				cardSet.add(GameUtil.getCard(cityName));
			}
			gameAction = new Cure(desease, cardSet);
		} else if (str.startsWith("build")) {
			gameAction = GameProperties.buildAction;
		} else if (str.startsWith("pass")) {
			gameAction = new Pass(gameStatus.getCurrentActionCount());
		} else {
			logger.info("Wrong action please try again");
		}
		if(!gameAction.perform(gameStatus)) {
			logger.info("Invalid action");
		}
	}

}
