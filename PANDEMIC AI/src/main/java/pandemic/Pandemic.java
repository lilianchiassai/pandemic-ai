package pandemic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

public class Pandemic extends AbstractGame<State>{
	
	boolean debugLog;
	Set<City> alreadyEcloded;
	City eclosionStart;
	
	public Pandemic(State state) {
		this.gameState=state;
		this.debugLog=false;
		this.alreadyEcloded = new HashSet<City>();
	}
	
	public Pandemic(int numberOfPlayers, int difficulty) {
		this.debugLog=false;
		this.alreadyEcloded = new HashSet<City>();
		this.gameState=State.Builder.startStateBuilder(new Properties(numberOfPlayers,difficulty)).build();
		start();
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
		if (!this.gameState.isEradicated(desease)) {
			int cubeCounter = 0;
			while (cubeCounter < strength) {
				if (this.gameState.getCityCubeCount(city, desease) == 3) {
					eclosion(city, desease);
				} else {
					GameUtil.log(this, logger, "The " + desease.getName() + " desease spreads in " + city.getName() + ".");
					if (!this.gameState.addCube(city, desease)) {
						GameUtil.log(this, logger, "No more "+desease.getName()+" cubes to add.");
						lose();
					}
				}
				cubeCounter++;
			}
		}
	}

	private void eclosion(City city, Desease desease) {
		if(this.alreadyEcloded.contains(city)) {
			//do nothing
		} else {
			GameUtil.log(this, logger, "Eclosion in " + city.getName() + ".");
			this.gameState.increaseEclosionCounter();
			if (this.gameState.eclosionCount > this.gameState.gameProperties.maxEclosionCounter) {
				GameUtil.log(this, logger, "Too many eclosions !");
				lose();
			} else {
				if(this.alreadyEcloded.size() == 0) {
					this.eclosionStart=city;
				}
				this.alreadyEcloded.add(city);
				for (City target : city.getNeighbourSet()) {
					infect(target, 1, desease);
				}
			}
		}
		if(this.eclosionStart == city) {
			this.eclosionStart=null;
			this.alreadyEcloded.clear();
		}
		
	}

	public PlayedCharacter nextPlayer() {
		this.gameState.increaseTurnCounter();
		this.gameState.nextPlayer();
		GameUtil.log(this, logger, "Turn "+this.gameState.turnCount);
		GameUtil.log(this, logger, this.gameState.getCurrentPlayer().getName() + " starts a new turn in "+this.gameState.getCurrentPlayerPosition().getName()+".");
		GameUtil.log(this, logger, "There are "+this.gameState.getCityCubeCount(this.gameState.getCurrentPlayerPosition(), this.gameState.getCurrentPlayerPosition().getDesease())+" cubes.");
		GameUtil.log(this, logger, "Player hand "+this.gameState.getCurrentHand().toString()+".");
		return this.gameState.getCurrentPlayer();
	}

	public void drawEndTurn() {
		// current Player draws
		GameUtil.log(this, logger, this.gameState.getCurrentPlayer().getName() + " draws.");
		for (int i = 0; i < 2; i++) {
			PlayerCard card = (PlayerCard) this.gameState.getPlayerDeck().draw();
			if (card == null) {
				//lose(gameStatus);
				GameUtil.log(this, logger, "No more player card.");
				this.setStep(GameStep.win);
			} else if (card instanceof EpidemicCard) {
				// do Epidemy
				GameUtil.log(this, logger, this.gameState.getCurrentPlayer().getName() + " draws the "
						+ (this.gameState.getEpidemicCount()+1) + "th Epidemic.");
				PropagationCard infectedCard = (PropagationCard) this.gameState.propagationDeck.drawBottomCard();
				if (infectedCard == null) {
					GameUtil.log(this, logger, "No propagation card");
					lose();
				} else {
					this.gameState.propagationDeck.discard(infectedCard);
					infect(infectedCard.getCity(), 3);
					this.gameState.increaseEpidemicCounter();
					this.gameState.propagationDeck.addOnTop(this.gameState.propagationDeck.getDiscardPile().shuffle());
				}
			} else {
				this.gameState.getCurrentHand().add((CityCard) card);
			}
		}
	}

	public void propagationEndTurn() {
		// propagation
		GameUtil.log(this, logger, "Propagation");
		for (int i = 0; i < this.gameState.getPropagationSpeed(); i++) {
			PropagationCard card = (PropagationCard) this.gameState.propagationDeck.draw();
			if (card == null) {
				GameUtil.log(this, logger, "No propagation card");
				lose();
			} else {
				infect(card.getCity());
				this.gameState.propagationDeck.discard(card);
			}
		}
	}

	public void lose() {
		this.setStep(Pandemic.GameStep.lose);
	}

	public boolean giveCard(PlayedCharacter character1, PlayedCharacter character2, CityCard card) {
		this.gameState.getCharacterHand(character1).remove(card);
		this.gameState.getCharacterHand(character2).add(card);
		return true;
	}

	public void updateStatus() {
		// TODO refactoring previous code to use a step and status manager
		switch (this.gameState.gameStep) {
		case play:
			if (!canPlay()) {
				this.setStep(Pandemic.GameStep.draw);
			}
			break;
		case draw:
			this.drawEndTurn();
			this.setStep(Pandemic.GameStep.discard);
			break;
		case discard:
			if (mustDiscard()) {
				this.setStep(Pandemic.GameStep.propagate);
			}
			break;
		case propagate:
			propagationEndTurn();
			nextPlayer();
			this.setStep(Pandemic.GameStep.play);
			break;
		default:
			break;
		}
	}

	public boolean findCure(Desease desease) {
		this.gameState.cureDesease(desease);
		if (this.gameState.curedDeseaseCount == this.gameState.gameProperties.deseaseList.size()-2) {
			this.setStep(GameStep.win);
		}
		return true;
	}
	
	public boolean cancelCure(Desease desease) {
		this.gameState.unCureDesease(desease);
		uneradicate(desease);
		if (this.gameState.gameStep == GameStep.win) {
			this.setStep(GameStep.play);
		}
		
		return true;
	}

	public boolean checkEradicated(Desease desease) {
		if (this.gameState.isCubeReserveFull(desease) && this.gameState.isCured(desease)) {
			return eradicate(desease);
		} 
		uneradicate(desease);
		return false;
	}

	private boolean eradicate(Desease desease) {
		GameUtil.log(this, logger, "Desease "+ desease.getName()+" is now eradicated");
		return this.gameState.eradicateDesease(desease);
	}
	
	private boolean uneradicate(Desease desease) {
		GameUtil.log(this, logger, "Desease "+ desease.getName()+" is not eradicated anymore");
		return this.gameState.unEradicateDesease(desease);
	}

	public boolean canPlay() {
		return this.gameState.gameStep==GameStep.play && this.gameState.getCurrentActionCount() > 0;
	}
	
	public boolean mustDiscard() {
		if(this.gameState.gameStep == GameStep.discard) {
			for(Hand hand : this.gameState.getAllCharacterHand()) {
				if(hand.size()>this.gameState.gameProperties.maxHandSize) return true;
			}
		}
		return false;
	}
	
	public Set<ActionSerie> allActions(){
		Set<ActionSerie> result = new HashSet<ActionSerie>();
		if(this.gameState.getCurrentActionCount()> 0) {
			if(this.gameState.previousActionList.size() == 0 || this.gameState.previousActionList.getLast() instanceof MoveAction) {
				HashSet<ActionSerie> actionSerieSet = getStaticActions(this.gameState.getCurrentActionCount(), this.gameState.previousActionList.size());
				for(ActionSerie actionSerie : actionSerieSet) {
					if(actionSerie.perform(this)) {
						if(this.gameState.getCurrentActionCount() == 0) {
							result.add(new ActionSerie(this.gameState.previousActionList));
						} else {
							result.addAll(allActions());
						}
						actionSerie.cancel(this);
					} else {
						//TODO throw exception
						System.out.println("error");
					}
				}
			}
			if(this.gameState.previousActionList.size() == 0 || this.gameState.previousActionList.getLast() instanceof StaticAction) {
				HashSet<ActionSerie> actionSerieSet = getMoveActions(this.gameState.getCurrentActionCount(), this.gameState.previousActionList.size());
				for(ActionSerie actionSerie : actionSerieSet) {
					if(actionSerie.perform(this)) {
						if(this.gameState.getCurrentActionCount() == 0) {
							result.add(new ActionSerie(this.gameState.previousActionList));
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
			result.add(this.gameState.previousActionList);
		}
		
		return result;
	}
	
	public boolean canMergeCancelVersion(GameAction gameAction) {
		return true;
	}
	
	public HashSet<ActionSerie>getMoveActions(int actionsLeft, int index) {
		HashSet<ActionSerie> result = new HashSet<ActionSerie>();
		GameAction lastAction = this.gameState.previousActionList.size()>0 && this.gameState.previousActionList.getLast() instanceof MoveAction ? this.gameState.previousActionList.getLast():null;
		if(actionsLeft == 0) {
		} else if(actionsLeft == 1) {
			for(GameAction gameAction : this.gameState.getCurrentPlayerPosition().allMoveActions) {
				if(lastAction !=null) {
					if(lastAction.getPriority() < gameAction.getPriority()) {
						//OK
					} else {
						if(lastAction instanceof ShuttleFlight && gameAction instanceof Drive) {
							//OK
						} else {
							 continue;				
						}
					}
				}
				
				
				if(canMergeCancelVersion(gameAction)) {
					if(gameAction.canPerform(this)) {
						ActionSerie merged = new ActionSerie(this.gameState.previousActionList.subList(index, this.gameState.previousActionList.size()));
						merged.addLast(gameAction);
						result.add(merged);
					}
				}

			}
		} else {
			
			for(GameAction firstAction : this.gameState.getCurrentPlayerPosition().allMoveActions) {
				if(lastAction !=null) {
					if(lastAction.getPriority() < firstAction.getPriority()) {
						//OK
					} else {
						if(lastAction instanceof ShuttleFlight && firstAction instanceof Drive) {
							//OK
						} else {
							 continue;				
						}
					}
				}
				
				if(firstAction.perform(this)) {
					HashSet<ActionSerie> subResult = getMoveActions(actionsLeft-firstAction.actionCost, index);
					subResult.add(new ActionSerie(this.gameState.previousActionList.subList(index, this.gameState.previousActionList.size())));
					for(ActionSerie gameActionList : subResult) {
						
						int shuttleFlightCount = 0;
						boolean invalid = false;
						Iterator<GameAction> it = gameActionList.listIterator(gameActionList.indexOf(firstAction));
						GameAction gameAction;
						int actionCost=0;
						while(it.hasNext()  && !invalid) {
							gameAction = it.next();
							actionCost+=gameAction.actionCost;
							//Check for the next part of the next move actions if a simple Drive would not have been more efficient
							if(actionCost >= firstAction.origin.getDistance(((MoveAction)gameAction).getDestination())) {
								invalid = !(gameAction instanceof Drive && gameAction == firstAction);
							}
							//Do not directFlight to a close location if the goal is to move afterward : it is better to move then charterflight
							if(shuttleFlightCount==0 && !invalid && firstAction instanceof DirectFlight) {
								if(gameAction instanceof Drive && ((Drive) gameAction).actionCost >=firstAction.origin.getDistance(((DirectFlight) firstAction).getDestination())) {
									invalid = true;
								}
							}
							//Only one shuttleFlight per move action
							if(!invalid && gameAction instanceof ShuttleFlight) {
								shuttleFlightCount++;
								if(shuttleFlightCount>1) {
									invalid=true;
								}
							}
							//If the first action is a directflight and the last a charterFlight, the move between the two actions can be done both ways. Remove arbitrarily 
							if(!invalid && firstAction instanceof DirectFlight && gameAction instanceof CharterFlight) {
								if(gameAction.origin.id>((DirectFlight)firstAction).destination.id) {
									invalid=true;
								}
							}
							
							
						}
						//Last move cannot discard except if destination is a card to give or if is research center
						//TODO should adapt to what happened during the ActionSerie
						if(this.gameState.getCurrentActionCount()+firstAction.actionCost<=actionCost) {
							if(gameActionList.getFirst() instanceof DirectFlight) {
								if(!this.gameState.getCurrentHand().contains(((MoveAction)gameActionList.getLast()).destination.getCityCard())) {
									invalid=true;
								} 								
							}
							if(gameActionList.getLast() instanceof CharterFlight) {
								if(!this.gameState.getCurrentHand().contains(((MoveAction)gameActionList.getLast()).destination.getCityCard()) && !this.gameState.hasResearchCenter(((CharterFlight)gameActionList.getLast()).destination)) {
									invalid=true;
								}
							}
						}
						if(!invalid) {
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
		
		HashSet<ActionSerie> result = new HashSet<ActionSerie>();
		GameAction lastAction = this.gameState.previousActionList.size()>0 && this.gameState.previousActionList.getLast() instanceof StaticAction ? this.gameState.previousActionList.getLast():null;
		if(actionsLeft == 0) {
		} else if(actionsLeft == 1) {
			for(GameAction gameAction : this.gameState.getCurrentPlayerPosition().allStaticActions) {
				if(lastAction !=null) {
					if(lastAction.getPriority() < gameAction.getPriority()) {
						//OK
					} else {
						if(lastAction instanceof ShuttleFlight && gameAction instanceof Drive) {
							//OK
						} else {
							 continue;				
						}
					}
				}
				
				if(canMergeCancelVersion(gameAction)) {
					if(gameAction.canPerform(this)) {
						ActionSerie merged = new ActionSerie(this.gameState.previousActionList.subList(index, this.gameState.previousActionList.size()));
						merged.addLast(gameAction);
						result.add(merged);
					}
				}

			}
		} else {
			
			for(GameAction firstAction : this.gameState.getCurrentPlayerPosition().allStaticActions) {
				if(lastAction !=null) {
					if(lastAction.getPriority() < firstAction.getPriority()) {
						//OK
					} else {
						if(lastAction instanceof ShuttleFlight && firstAction instanceof Drive) {
							//OK
						} else {
							 continue;				
						}
					}
				}
				
				if(firstAction.perform(this)) {
					result.add(new ActionSerie(this.gameState.previousActionList.subList(index, this.gameState.previousActionList.size())));
					HashSet<ActionSerie> subResult = getStaticActions(actionsLeft-firstAction.actionCost, index);
					result.addAll(subResult);
					firstAction.cancel(this);
				}
			}
			
		}
		return result;
	}

	@Override
	public Pandemic duplicate() {
		Pandemic result =  new Pandemic(gameState.duplicate());
		result.debugLog=true;
		return result;
	}

	@Override
	public ArrayList<ActionSerie> getMoves() {
		this.gameState.previousActionList.clear();
		return new ArrayList<ActionSerie>(allActions());
	}

	@Override
	public boolean update() {
		switch (this.gameState.gameStep) {
		case play:
			if (!canPlay()) {
				this.setStep(Pandemic.GameStep.draw);
			} else {
				return true;
			}
		case draw:
			this.drawEndTurn();
			this.setStep(Pandemic.GameStep.discard);
			break;
		case discard:
			if (!mustDiscard()) {
				this.setStep(Pandemic.GameStep.propagate);
			} else {
				return true;
			}
			break;
		case propagate:
			propagationEndTurn();
			nextPlayer();
			this.setStep(Pandemic.GameStep.play);
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public State getGameState() {
		return (State) this.gameState;
	}

	@Override
	public boolean isOver() {
		if(this.gameState.gameStep == GameStep.lose || isWin()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isWin() {
		return this.gameState.gameStep == GameStep.win;
	}
	
	public void setStep(GameStep setStep) {
		if(!isOver()) {
			this.gameState.gameStep = setStep;
		}
	}

	public boolean isDebugLog() {
		return this.debugLog;
	}
	
	public void start() {
		// Draw 9 propagation card
		if(this.gameState.gameStep == GameStep.ready) {
			for (int i = 3; i>0; i--) {
				for (int k = 0; k<3; k++) {
					PropagationCard card = (PropagationCard) this.gameState.propagationDeck.draw();
					this.infect(card.getCity(), i);
					this.gameState.propagationDeck.discard(card);
				}
			}
			//infect(Reserve.getInstance().getMap().get("Atlanta"), 3);
			setStep(GameStep.play);
		}
		
	}

	public void setDebugLog(boolean b) {
		this.debugLog=true;
	}
}
