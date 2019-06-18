package pandemic.engine;

import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.AbstractPlayer;
import pandemic.Pandemic;
import pandemic.action.Cure;
import pandemic.action.Discard;
import pandemic.action.GameAction;
import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.material.PlayedCharacter;
import pandemic.material.card.CityCard;
import pandemic.material.card.Hand;
import pandemic.util.GameUtil;

public class HumanPlayer extends AbstractPlayer {

  private static Logger logger = LogManager.getLogger(HumanPlayer.class.getName());
  private Scanner scanner;

  public HumanPlayer() {
    super();
  }

  @Override
  public void update(Observable obs, Object game) {
    switch (((Pandemic) game).gameState.getGameStep()) {
      case play:
        action(((Pandemic) game));
        break;
      case discard:
        discard(((Pandemic) game));
        break;
      default:
        break;
    }
  }

  private void discard(Pandemic pandemic) {
    for (Hand hand : pandemic.gameState.getAllCharacterHand()) {
      while (hand.size() > 7) {
        logger.info(
            "Player " + hand.getCharacter().getName() + " has too many cards, please discard.");
        logger.info("Your cards are " + hand.toString());
        logger.info("Type the name of the card to discard.");
        scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        Discard discard = new Discard(hand.getCharacter(), GameUtil.getCard(hand, str));
        while (!discard.perform(pandemic)) {
          logger.info("Invalid name of card please try again");
          scanner = new Scanner(System.in);
          str = scanner.nextLine();
          discard = new Discard(hand.getCharacter(), GameUtil.getCard(hand, str));
        }
      }
    }
  }

  private void action(Pandemic pandemic) {
    boolean invalid = false;
    PlayedCharacter playedCharacter = pandemic.gameState.getCurrentPlayer();
    logger.info("You are in " + pandemic.gameState.getCharacterPosition(playedCharacter).getName()
        + ". There are "
        + pandemic.gameState.getCityCubeCount(
            pandemic.gameState.getCharacterPosition(playedCharacter),
            pandemic.gameState.getCharacterPosition(playedCharacter).getDesease())
        + " cubes.");
    if (pandemic.gameState
        .hasResearchCenter(pandemic.gameState.getCharacterPosition(playedCharacter))) {
      logger.info("You are in a Research Center ! Other research centers are located in: "
          + pandemic.gameState.getResearchCenters().toString() + ".");
    }
    logger.info("Your cards are " + pandemic.gameState.getCurrentHand().toString());
    logger.info(pandemic.gameState.getCurrentActionCount() + " actions remaining.");
    logger.info(
        "Your only possible action are : drive-cityName,directFlight-cityName,charterFlight-cityName,shuttleFlight-cityName, treat, shareKnowledge-playerName, build, cure-deseaseName-card1,card2,card3,card4,card5");

    scanner = new Scanner(System.in);
    String str = scanner.nextLine();
    GameAction gameAction = null;
    if (str.startsWith("drive")) {
      String cityName = str.split("-")[1];
      City city = GameUtil.getCity(cityName);
      if (city != null) {
        gameAction = pandemic.gameState.getCharacterPosition(playedCharacter).getMultiDrive(city);
      } else {
        invalid = true;
      }
    } else if (str.startsWith("directFlight")) {
      String cityName = str.split("-")[1];
      City city = GameUtil.getCity(cityName);
      if (city != null) {
        gameAction = pandemic.gameState.getCharacterPosition(playedCharacter).getDirectFlight(city);
      } else {
        invalid = true;
      }
    } else if (str.startsWith("charterFlight")) {
      String cityName = str.split("-")[1];
      City city = GameUtil.getCity(cityName);
      if (city != null) {
        gameAction =
            pandemic.gameState.getCharacterPosition(playedCharacter).getCharterFlight(city);
      } else {
        invalid = true;
      }
    } else if (str.startsWith("shuttleFlight")) {
      String cityName = str.split("-")[1];
      City city = GameUtil.getCity(cityName);
      if (city != null) {
        gameAction =
            pandemic.gameState.getCharacterPosition(playedCharacter).getShuttleFlight(city);
      } else {
        invalid = true;
      }
    } else if (str.startsWith("treat")) {
      gameAction = pandemic.gameState.getCharacterPosition(playedCharacter).getTreat(
          pandemic.gameState.getCityCubeCount(pandemic.gameState.getCurrentPlayerPosition(),
              pandemic.gameState.getCurrentPlayerPosition().getDesease()),
          pandemic.gameState.isCured(pandemic.gameState.getCurrentPlayerPosition().getDesease()));
    } else if (str.startsWith("shareKnowledge")) {
      String playerName = str.split("-")[1];
      PlayedCharacter otherPlayer = GameUtil.getPlayer(playerName);
      if (otherPlayer != null) {
        gameAction =
            pandemic.gameState.getCharacterPosition(playedCharacter).getShareKnowledge(otherPlayer);
      } else {
        invalid = true;
      }
    } else if (str.startsWith("cure")) {
      String deseaseName = str.split("-")[1];
      Desease desease = GameUtil.getDesease(deseaseName);
      String cardNames = str.split("-")[2];
      HashSet<CityCard> cardSet = new HashSet<CityCard>();
      if (cardNames != null) {
        for (String cityName : cardNames.split(",")) {
          cardSet.add((CityCard) GameUtil.getCard(cityName));
        }
        gameAction = new Cure(pandemic.gameState.getCurrentPlayerPosition(), desease, cardSet);
      } else {
        invalid = true;
      }
    } else if (str.startsWith("build")) {
      gameAction = pandemic.gameState.getCurrentPlayerPosition().getBuildAction();
    } else if (str.startsWith("pass")) {
      gameAction = pandemic.gameState.getCurrentPlayerPosition().getPass(pandemic.gameState);
    } else if (str.startsWith("cancel")) {
      gameAction = pandemic.gameState.getCurrentPlayerPosition().getCancel(pandemic.gameState);
    } else {
      logger.info("Wrong action please try again");
    }
    if (invalid || !gameAction.perform(pandemic)) {
      logger.info("Invalid action");
    }
  }

}
