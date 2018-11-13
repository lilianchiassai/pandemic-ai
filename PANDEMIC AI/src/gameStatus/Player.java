package gameStatus;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import objects.Character;
import objects.City;
import objects.Desease;

public class Player implements Observer {

	private static Logger logger = LogManager.getLogger(Player.class.getName());
	
	public Player() {
		
	}
	
	@Override
	public void update(Observable obs, Object notifyMap) {
		if(((Map<String, Object>)notifyMap).get("action") != null) {
			action((Game)obs, (Character) ((Map<String, Object>)notifyMap).get("action"));
		} else if (((Map<String, Object>)notifyMap).get("discard") != null) {
			discard((Game)obs, (Character) ((Map<String, Object>)notifyMap).get("discard"));
		} else if (((Map<String, Object>)notifyMap).get("event") != null) {
			event((Game)obs, (Character) ((Map<String, Object>)notifyMap).get("event"));
		}
	}
	
	private void discard(Game game, Character character) {
		logger.info("Player "+character.getName()+" has too many cards, please discard.");
		logger.info("Your cards are "+character.getHand().toString());
		logger.info("Type the name of the card to discard.");
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		while(!character.getHand().discard(character.getHand().getCard(str))) {
			logger.info("Invalid name of card please try again");
			scanner = new Scanner(System.in);
			str = scanner.nextLine();
		}
	}
	
	private void event(Game game, Character character) {
		
	}
	
	private void action(Game game, Character character) {
		logger.info("You are in "+character.getPosition().getName()+". There are "+character.getPosition().getCubeSet(character.getPosition().getDesease()).size()+" cubes.");
		logger.info("Your cards are "+character.getHand().toString());
		logger.info(character.getCurrentActionCount() +" actions remaining.");
		logger.info("Your only possible action are : drive-city,directFlight-city,charterFlight-city,shuttleFlight-city, treatDesease, shareKnowledge-player, build, cure-desease");
		
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		if(str.startsWith("drive")) {
			String cityName = str.split("-")[1];
			City city = game.getCity(cityName);
			if(!character.drive(city)) {
				logger.info("Invalid action");
			}
		} else if(str.startsWith("directFlight")) {
			String cityName = str.split("-")[1];
			City city = game.getCity(cityName);
			if(!character.directFlight(city)) {
				logger.info("Invalid action");
			}
		} else if(str.startsWith("charterFlight")) {
			String cityName = str.split("-")[1];
			City city = game.getCity(cityName);
			if(!character.charterFlight(city)) {
				logger.info("Invalid action");
			}
		} else if(str.startsWith("shuttleFlight")) {
			String cityName = str.split("-")[1];
			City city = game.getCity(cityName);
			if(!character.shuttleFlight(city)) {
				logger.info("Invalid action");
			}
		} else if (str.startsWith("treatDesease")) {
			if(!character.treatDesease()) {
				logger.info("Invalid action");
			}
		} else if (str.startsWith("shareKnowledge")) {
			String playerName = str.split("-")[1];
			Character otherPlayer = game.getPlayer(playerName);
			if(!character.shareKnowledge(otherPlayer)) {
				logger.info("Invalid action");
			}
		} else if (str.startsWith("find")) {
			String deseaseName = str.split("-")[1];
			Desease desease = game.getDesease(deseaseName);
			if(!character.discoverCure(desease, character.getHand().getCityCardSet())) {
				logger.info("Invalid action");
			}
		} else if (str.startsWith("build")) {
			if(!character.buildResearchCenter()) {
				logger.info("Invalid action");
			}
		} else if (str.startsWith("pass")) {
			character.setCurrentActionCount(0);
		} else {
			logger.info("Wrong action please try again");
		}
	}

}
