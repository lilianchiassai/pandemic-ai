package pandemic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.ToIntFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.AbstractGame;
import pandemic.action.ActionSerie;
import pandemic.action.CharterFlight;
import pandemic.action.DirectFlight;
import pandemic.action.Drive;
import pandemic.action.GameAction;
import pandemic.action.MoveAction;
import pandemic.action.ShuttleFlight;
import pandemic.action.StaticAction;
import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.material.PlayedCharacter;
import pandemic.material.card.CityCard;
import pandemic.material.card.EpidemicCard;
import pandemic.material.card.Hand;
import pandemic.material.card.PlayerCard;
import pandemic.material.card.PropagationCard;
import pandemic.util.GameUtil;

public class Pandemic extends AbstractGame<State> {

  boolean debugLog;
  Set<City> alreadyEcloded;
  City eclosionStart;
  ToIntFunction<Pandemic> gameOver;

  public Pandemic(State state) {
    gameState = state;
    debugLog = false;
    alreadyEcloded = new HashSet<City>();
  }

  public Pandemic(int numberOfPlayers, int difficulty) {
    debugLog = false;
    alreadyEcloded = new HashSet<City>();
    gameState =
        State.Builder.startStateBuilder(new Properties(numberOfPlayers, difficulty)).build();
  }

  public enum GameStep {
    ready, turnStart, play, draw, propagate, win, lose, discard
  }

  private static Logger logger = LogManager.getLogger(Pandemic.class.getName());

  private void infect(City city) {
    infect(city, 1, city.getDesease());
  }

  public void infect(City city, int strength) {
    infect(city, strength, city.getDesease());
  }

  public void infect(City city, int strength, Desease desease) {
    // Check if eradicated
    if (!gameState.isEradicated(desease)) {
      int cubeCounter = 0;
      while (cubeCounter < strength) {
        if (gameState.getCityCubeCount(city, desease) == 3) {
          eclosion(city, desease);
        } else {
          GameUtil.log(this, Pandemic.logger,
              "The " + desease.getName() + " desease spreads in " + city.getName() + ".");
          if (!gameState.addCube(city, desease)) {
            GameUtil.log(this, Pandemic.logger, "No more " + desease.getName() + " cubes to add.");
            lose();
          }
        }
        cubeCounter++;
      }
    }
  }

  private void eclosion(City city, Desease desease) {
    if (alreadyEcloded.contains(city)) {
      // do nothing
    } else {
      GameUtil.log(this, Pandemic.logger, "Eclosion in " + city.getName() + ".");
      gameState.increaseEclosionCounter();
      if (gameState.eclosionCount > gameState.gameProperties.maxEclosionCounter) {
        GameUtil.log(this, Pandemic.logger, "Too many eclosions !");
        lose();
      } else {
        if (alreadyEcloded.size() == 0) {
          eclosionStart = city;
        }
        alreadyEcloded.add(city);
        for (final City target : city.getNeighbourSet()) {
          infect(target, 1, desease);
        }
      }
    }
    if (eclosionStart == city) {
      eclosionStart = null;
      alreadyEcloded.clear();
    }

  }

  public PlayedCharacter nextPlayer() {
    gameState.increaseTurnCounter();
    gameState.nextPlayer();
    GameUtil.log(this, Pandemic.logger, "Turn " + gameState.turnCount);
    GameUtil.log(this, Pandemic.logger, gameState.getCurrentPlayer().getName()
        + " starts a new turn in " + gameState.getCurrentPlayerPosition().getName() + ".");
    GameUtil.log(this, Pandemic.logger,
        "There are " + gameState.getCityCubeCount(gameState.getCurrentPlayerPosition(),
            gameState.getCurrentPlayerPosition().getDesease()) + " cubes.");
    GameUtil.log(this, Pandemic.logger,
        "Player hand " + gameState.getCurrentHand().toString() + ".");
    return gameState.getCurrentPlayer();
  }

  public void drawEndTurn() {
    // current Player draws
    GameUtil.log(this, Pandemic.logger, gameState.getCurrentPlayer().getName() + " draws.");
    for (int i = 0; i < 2; i++) {
      final PlayerCard card = gameState.getPlayerDeck().draw();
      if (card == null) {
        // lose(gameStatus);
        GameUtil.log(this, Pandemic.logger, "No more player card.");
        setStep(GameStep.win);
      } else if (card instanceof EpidemicCard) {
        // do Epidemy
        GameUtil.log(this, Pandemic.logger, gameState.getCurrentPlayer().getName() + " draws the "
            + (gameState.getEpidemicCount() + 1) + "th Epidemic.");
        final PropagationCard infectedCard = gameState.propagationDeck.drawBottomCard();
        if (infectedCard == null) {
          GameUtil.log(this, Pandemic.logger, "No propagation card");
          lose();
        } else {
          gameState.propagationDeck.discard(infectedCard);
          infect(infectedCard.getCity(), 3);
          gameState.increaseEpidemicCounter();
          gameState.propagationDeck.addOnTop(gameState.propagationDeck.getDiscardPile().shuffle());
        }
      } else {
        gameState.getCurrentHand().add((CityCard) card);
      }
    }
  }

  public void propagationEndTurn() {
    // propagation
    GameUtil.log(this, Pandemic.logger, "Propagation");
    for (int i = 0; i < gameState.getPropagationSpeed(); i++) {
      final PropagationCard card = gameState.propagationDeck.draw();
      if (card == null) {
        GameUtil.log(this, Pandemic.logger, "No propagation card");
        lose();
      } else {
        infect(card.getCity());
        gameState.propagationDeck.discard(card);
      }
    }
  }

  public void lose() {
    setStep(Pandemic.GameStep.lose);
  }

  public boolean giveCard(PlayedCharacter character1, PlayedCharacter character2, CityCard card) {
    gameState.getCharacterHand(character1).remove(card);
    gameState.getCharacterHand(character2).add(card);
    return true;
  }

  public boolean findCure(Desease desease) {
    gameState.cureDesease(desease);
    return true;
  }

  public boolean cancelCure(Desease desease) {
    gameState.unCureDesease(desease);
    uneradicate(desease);
    return true;
  }

  public boolean checkEradicated(Desease desease) {
    if (gameState.isCubeReserveFull(desease) && gameState.isCured(desease)) {
      return eradicate(desease);
    }
    uneradicate(desease);
    return false;
  }

  private boolean eradicate(Desease desease) {
    GameUtil.log(this, Pandemic.logger, "Desease " + desease.getName() + " is now eradicated");
    return gameState.eradicateDesease(desease);
  }

  private boolean uneradicate(Desease desease) {
    GameUtil.log(this, Pandemic.logger,
        "Desease " + desease.getName() + " is not eradicated anymore");
    return gameState.unEradicateDesease(desease);
  }

  public void setStep(GameStep setStep) {
    if (!isOver()) {
      gameState.gameStep = setStep;
    }
  }

  public boolean isDebugLog() {
    return debugLog;
  }

  public void setDebugLog(boolean b) {
    debugLog = true;
  }

  public boolean canPlay() {
    return gameState.gameStep == GameStep.play && gameState.getCurrentActionCount() > 0;
  }

  public boolean mustDiscard() {
    if (gameState.gameStep == GameStep.discard) {
      for (final Hand hand : gameState.getAllCharacterHand()) {
        if (hand.size() > gameState.gameProperties.maxHandSize)
          return true;
      }
    }
    return false;
  }

  public Set<ActionSerie> allActions() {
    final Set<ActionSerie> result = new HashSet<ActionSerie>();
    if (gameState.getCurrentActionCount() > 0) {
      if (gameState.previousActionList.size() == 0
          || gameState.previousActionList.getLast() instanceof MoveAction) {
        final HashSet<ActionSerie> actionSerieSet = getStaticActions(
            gameState.getCurrentActionCount(), gameState.previousActionList.size());
        for (final ActionSerie actionSerie : actionSerieSet) {
          if (actionSerie.perform(this)) {
            if (gameState.getCurrentActionCount() == 0) {
              result.add(new ActionSerie(gameState.previousActionList));
            } else {
              result.addAll(allActions());
            }
            actionSerie.cancel(this);
          } else {
            // TODO throw exception
            System.out.println("error");
          }
        }
      }
      if (gameState.previousActionList.size() == 0
          || gameState.previousActionList.getLast() instanceof StaticAction) {
        final HashSet<ActionSerie> actionSerieSet =
            getMoveActions(gameState.getCurrentActionCount(), gameState.previousActionList.size());
        for (final ActionSerie actionSerie : actionSerieSet) {
          if (actionSerie.perform(this)) {
            if (gameState.getCurrentActionCount() == 0) {
              result.add(new ActionSerie(gameState.previousActionList));
            } else {
              result.addAll(allActions());
            }
            actionSerie.cancel(this);
          } else {
            throw new AssertionError("");
          }
        }
      }

    } else {
      result.add(gameState.previousActionList);
    }

    return result;
  }

  public boolean canMergeCancelVersion(GameAction gameAction) {
    return true;
  }

  public HashSet<ActionSerie> getMoveActions(int actionsLeft, int index) {
    final HashSet<ActionSerie> result = new HashSet<ActionSerie>();
    final GameAction lastAction = gameState.previousActionList.size() > 0
        && gameState.previousActionList.getLast() instanceof MoveAction
            ? gameState.previousActionList.getLast()
            : null;
    if (actionsLeft == 0) {
    } else if (actionsLeft == 1) {
      for (final GameAction gameAction : gameState.getCurrentPlayerPosition().allMoveActions) {
        if (lastAction != null) {
          if (lastAction.getPriority() < gameAction.getPriority()) {
            // OK
          } else {
            if (lastAction instanceof ShuttleFlight && gameAction instanceof Drive) {
              // OK
            } else {
              continue;
            }
          }
        }


        if (canMergeCancelVersion(gameAction)) {
          if (gameAction.canPerform(this)) {
            final ActionSerie merged = new ActionSerie(
                gameState.previousActionList.subList(index, gameState.previousActionList.size()));
            merged.addLast(gameAction);
            result.add(merged);
          }
        }

      }
    } else {

      for (final GameAction firstAction : gameState.getCurrentPlayerPosition().allMoveActions) {
        if (lastAction != null) {
          if (lastAction.getPriority() < firstAction.getPriority()) {
            // OK
          } else {
            if (lastAction instanceof ShuttleFlight && firstAction instanceof Drive) {
              // OK
            } else {
              continue;
            }
          }
        }

        if (firstAction.perform(this)) {
          final HashSet<ActionSerie> subResult =
              getMoveActions(actionsLeft - firstAction.actionCost, index);
          subResult.add(new ActionSerie(
              gameState.previousActionList.subList(index, gameState.previousActionList.size())));
          for (final ActionSerie gameActionList : subResult) {

            int shuttleFlightCount = 0;
            boolean invalid = false;
            final Iterator<GameAction> it =
                gameActionList.listIterator(gameActionList.indexOf(firstAction));
            GameAction gameAction;
            int actionCost = 0;
            while (it.hasNext() && !invalid) {
              gameAction = it.next();
              actionCost += gameAction.actionCost;
              // Check for the next part of the next move actions if a simple Drive would not have
              // been more efficient
              if (actionCost >= firstAction.origin
                  .getDistance(((MoveAction) gameAction).getDestination())) {
                invalid = !(gameAction instanceof Drive && gameAction == firstAction);
              }
              // Do not directFlight to a close location if the goal is to move afterward : it is
              // better to move then charterflight
              if (shuttleFlightCount == 0 && !invalid && firstAction instanceof DirectFlight) {
                if (gameAction instanceof Drive
                    && ((Drive) gameAction).actionCost >= firstAction.origin
                        .getDistance(((DirectFlight) firstAction).getDestination())) {
                  invalid = true;
                }
              }
              // Only one shuttleFlight per move action
              if (!invalid && gameAction instanceof ShuttleFlight) {
                shuttleFlightCount++;
                if (shuttleFlightCount > 1) {
                  invalid = true;
                }
              }
              // If the first action is a directflight and the last a charterFlight, the move
              // between the two actions can be done both ways. Remove arbitrarily
              if (!invalid && firstAction instanceof DirectFlight
                  && gameAction instanceof CharterFlight) {
                if (gameAction.origin.id > ((DirectFlight) firstAction).destination.id) {
                  invalid = true;
                }
              }


            }
            // Last move cannot discard except if destination is a card to give or if is research
            // center
            // TODO should adapt to what happened during the ActionSerie
            if (gameState.getCurrentActionCount() + firstAction.actionCost <= actionCost) {
              if (gameActionList.getFirst() instanceof DirectFlight) {
                if (!gameState.getCurrentHand()
                    .contains(((MoveAction) gameActionList.getLast()).destination.getCityCard())) {
                  invalid = true;
                }
              }
              if (gameActionList.getLast() instanceof CharterFlight) {
                if (!gameState.getCurrentHand()
                    .contains(((MoveAction) gameActionList.getLast()).destination.getCityCard())
                    && !gameState.hasResearchCenter(
                        ((CharterFlight) gameActionList.getLast()).destination)) {
                  invalid = true;
                }
              }
            }
            if (!invalid) {
              result.add(gameActionList);

            }
          }
          firstAction.cancel(this);
        }
      }
    }
    return result;
  }

  public HashSet<ActionSerie> getStaticActions(int actionsLeft, int index) {

    final HashSet<ActionSerie> result = new HashSet<ActionSerie>();
    final GameAction lastAction = gameState.previousActionList.size() > 0
        && gameState.previousActionList.getLast() instanceof StaticAction
            ? gameState.previousActionList.getLast()
            : null;
    if (actionsLeft == 0) {
    } else if (actionsLeft == 1) {
      for (final GameAction gameAction : gameState.getCurrentPlayerPosition().allStaticActions) {
        if (lastAction != null) {
          if (lastAction.getPriority() < gameAction.getPriority()) {
            // OK
          } else {
            if (lastAction instanceof ShuttleFlight && gameAction instanceof Drive) {
              // OK
            } else {
              continue;
            }
          }
        }

        if (canMergeCancelVersion(gameAction)) {
          if (gameAction.canPerform(this)) {
            final ActionSerie merged = new ActionSerie(
                gameState.previousActionList.subList(index, gameState.previousActionList.size()));
            merged.addLast(gameAction);
            result.add(merged);
          }
        }

      }
    } else {

      for (final GameAction firstAction : gameState.getCurrentPlayerPosition().allStaticActions) {
        if (lastAction != null) {
          if (lastAction.getPriority() < firstAction.getPriority()) {
            // OK
          } else {
            if (lastAction instanceof ShuttleFlight && firstAction instanceof Drive) {
              // OK
            } else {
              continue;
            }
          }
        }

        if (firstAction.perform(this)) {
          result.add(new ActionSerie(
              gameState.previousActionList.subList(index, gameState.previousActionList.size())));
          final HashSet<ActionSerie> subResult =
              getStaticActions(actionsLeft - firstAction.actionCost, index);
          result.addAll(subResult);
          firstAction.cancel(this);
        }
      }

    }
    return result;
  }

  @Override
  public Pandemic duplicate() {
    final Pandemic result = new Pandemic(gameState.duplicate());
    result.debugLog = true;
    return result;
  }

  @Override
  public ArrayList<ActionSerie> getMoves() {
    gameState.previousActionList.clear();
    return new ArrayList<ActionSerie>(this.duplicate().allActions());
  }

  @Override
  public boolean update() {
    if(gameState.curedDeseaseCount == gameState.gameProperties.deseaseList.size()) {
      setStep(Pandemic.GameStep.win);
      return false;
    } else {
      switch (gameState.gameStep) {
        case play:
          if (!canPlay()) {
            setStep(Pandemic.GameStep.draw);
          } else {
            return true;
          }
        case draw:
          drawEndTurn();
          setStep(Pandemic.GameStep.discard);
          break;
        case discard:
          if (!mustDiscard()) {
            setStep(Pandemic.GameStep.propagate);
          } else {
            return true;
          }
          break;
        case propagate:
          propagationEndTurn();
          nextPlayer();
          setStep(Pandemic.GameStep.play);
          break;
        default:
          break;
      }
      return false;
    }
    
    
  }

  @Override
  public State getGameState() {
    return gameState;
  }

  @Override
  public boolean isOver() {
    if (gameState.gameStep == GameStep.lose || isWin()) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isWin() {
    return gameState.gameStep == GameStep.win;
  }

}
