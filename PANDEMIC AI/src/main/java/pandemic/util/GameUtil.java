package pandemic.util;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import pandemic.Pandemic;
import pandemic.Reserve;
import pandemic.State;
import pandemic.action.ActionSerie;
import pandemic.action.Build;
import pandemic.action.CharterFlight;
import pandemic.action.Cure;
import pandemic.action.DirectFlight;
import pandemic.action.Discard;
import pandemic.action.Drive;
import pandemic.action.GameAction;
import pandemic.action.GiveKnowledge;
import pandemic.action.Pass;
import pandemic.action.ReceiveKnowledge;
import pandemic.action.ShareKnowledge;
import pandemic.action.ShuttleFlight;
import pandemic.action.Treat;
import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.material.PlayedCharacter;
import pandemic.material.card.Card;
import pandemic.material.card.CityCard;
import pandemic.material.card.Hand;
import pandemic.material.card.PlayerCard;

public class GameUtil {
	
	
	
	public static void log(Pandemic pandemic, Logger logger, String message) {
		if(pandemic.isDebugLog()) {
			logger.debug(message);
		} else {
			logger.info(message);
		}
	}
	
	public static Predicate<? super Card> getCityCardPredicate() {
		return (card) -> (card.getClass().isAssignableFrom(CityCard.class));
	}
	
	
	public static Predicate<? super Card> getCityCardPredicate(City city) {
		return (card) -> (card.getClass().isAssignableFrom(CityCard.class) && ((CityCard)card).getCity() == city );
	}

	public static Predicate<? super Card> getCityCardPredicate(Desease desease) {
		return (card) -> (((CityCard)card).getCity().getDesease() == desease);
	}

	public static Predicate<? super City> getCityNamePredicate(String name) {
		return (city) -> (city.getClass().isAssignableFrom(City.class) && ((City)city).getName().equals(name));
	}

	public static Predicate<? super PlayedCharacter> getCharacterNamePredicate(String name) {
		return (playedCharacter) -> (playedCharacter.getClass().isAssignableFrom(PlayedCharacter.class) && ((PlayedCharacter)playedCharacter).getName().equals(name));
	}

	public static Predicate<? super Desease> getDeseaseNamePredicate(String name) {
		return (desease) -> (desease.getClass().isAssignableFrom(Desease.class) && ((Desease)desease).getName().equals(name));
	}
	
	/* public static Graph<City, DefaultEdge> makeBidirectionnal(Graph<City, DefaultEdge> graph) {
		for (City source: graph.vertexSet()) {
		    Set<DefaultEdge> edges = graph.outgoingEdgesOf(source);
		    for (DefaultEdge edge : edges) {
		        City target = graph.getEdgeTarget(edge);
		        boolean comingBack = graph.containsEdge(target, source);
		        if(!graph.containsEdge(target, source)) {
		        	graph.addEdge(target,  source);
		        }
		    }
		}
		return graph;
	} */

	public static Predicate<? super Card> getCardTitlePredicate(String title) {
		return (card) -> (card instanceof Card && ((Card)card).getTitle().equals(title));
	}
	
	public static City getCity(String cityName) {
		Set<City> citySet = (Set<City>) Reserve.getInstance().getMap().values().stream().filter(GameUtil.getCityNamePredicate(cityName)).collect(Collectors.toSet());
		if(citySet != null) {
			Iterator<City> it = citySet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}
	
	public static PlayedCharacter getCardOwner(State state, Card card) {
		for(PlayedCharacter playedCharacter : Reserve.getInstance().getCharacterReserve()) {
			if(state.getCharacterHand(playedCharacter).contains(card)) {
				return playedCharacter;
			}
		}
		return null;
	}

	public static PlayedCharacter getPlayer(String playerName) {
		Set<PlayedCharacter> playerSet =  (Set<PlayedCharacter>) Reserve.getInstance().getCharacterReserve().stream().filter(GameUtil.getCharacterNamePredicate(playerName)).collect(Collectors.toSet());
		if(playerSet != null) {
			Iterator<PlayedCharacter> it = playerSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}

	public static Desease getDesease(String deseaseName) {
		Set<Desease> deseaseSubSet = (Set<Desease>) Reserve.getInstance().getDeseaseSet().stream().filter(GameUtil.getDeseaseNamePredicate(deseaseName)).collect(Collectors.toSet());
		if(deseaseSubSet != null) {
			Iterator<Desease> it = deseaseSubSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}

	public static PlayerCard getCard(String title) {
		Set<PlayerCard> cardSet = (Set<PlayerCard>) Reserve.getInstance().getPlayerCardReserve().stream().filter(GameUtil.getCardTitlePredicate(title)).collect(Collectors.toSet());
		if(cardSet != null) {
			Iterator<PlayerCard> it = cardSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}

	public static CityCard getCard(Hand hand, String title) {
		PlayerCard card = getCard(title);
		if(hand.contains(card)) return (CityCard)card;
		return null;
	}
	public static int getActionSerieWeight(State state, ActionSerie actionSerie) {
		int result=0;
		for(GameAction gameAction : actionSerie) {
			result+=getActionWeight(state, gameAction);
		}
		return result;
	}
	
	public static int getActionWeight(State state, GameAction gameAction) {
		if (gameAction.getClass().isAssignableFrom(Drive.class)) {
			return (state.getCityCubeCount(((Drive)gameAction).getDestination(), ((Drive)gameAction).getDestination().getDesease())) *10 +10;
		} else if (gameAction.getClass().isAssignableFrom(DirectFlight.class)) {
			return state.getCityCubeCount(((DirectFlight)gameAction).getDestination(), ((DirectFlight)gameAction).getDestination().getDesease())*5 +1;
		} else if (gameAction.getClass().isAssignableFrom(CharterFlight.class)) {
			return state.getCityCubeCount(((CharterFlight)gameAction).getDestination(), ((CharterFlight)gameAction).getDestination().getDesease())*5 +1;
		} else if (gameAction.getClass().isAssignableFrom(ShuttleFlight.class)) {
			return (state.getCityCubeCount(((ShuttleFlight)gameAction).getDestination(), ((ShuttleFlight)gameAction).getDestination().getDesease())) *10+5;
		} else if (gameAction.getClass().isAssignableFrom(Treat.class)) {
			return state.getCityCubeCount(state.getCurrentPlayerPosition(), state.getCurrentPlayerPosition().getDesease()) * 200;
		} else if (gameAction.getClass().isAssignableFrom(ShareKnowledge.class)) {
			if(state.isCured(state.getCurrentPlayerPosition().getDesease())) {
				return 0;
			} else {
				int current = state.getCurrentHand().stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(state.getCurrentPlayerPosition().getDesease())).collect(Collectors.toSet()).size();
				int other = state.getCharacterHand(((ShareKnowledge)gameAction).getCharacter()).stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(state.getCurrentPlayerPosition().getDesease())).collect(Collectors.toSet()).size();
				if(current>=other && gameAction instanceof ReceiveKnowledge) {
					return 2000;
				} else if (current <= other && gameAction instanceof GiveKnowledge) {
					return 2000;
				}
			}
			return 0;
		} else if (gameAction.getClass().isAssignableFrom(Cure.class)) {
			return 2000;
		}  else if (gameAction.getClass().isAssignableFrom(Build.class)) {
			for(City city : state.getCurrentPlayerPosition().getNeighbourSet()) {
				if(state.hasResearchCenter(city)) {
					return 0;
				}
			}
			if(!state.isCured(state.getCurrentPlayerPosition().getDesease())) {
				int max = 0;
				Hand bestHand = null;
				for(Hand hand : state.getAllCharacterHand()) {
					int current = hand.stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(state.getCurrentPlayerPosition().getDesease())).collect(Collectors.toSet()).size();
					max = current>max ? current:max;
					bestHand = bestHand == null || current>max ? hand:bestHand;
				}
				if(bestHand == state.getCurrentHand()) {
					return 1;
				}
			}
			return 40;
		} else if (gameAction.getClass().isAssignableFrom(Pass.class)) {
			return 0;
		} else if (gameAction.getClass().isAssignableFrom(Discard.class)) {
			if(((Discard)gameAction).getCard() instanceof CityCard) {
				if(state.isCured(((CityCard)((Discard)gameAction).getCard()).getCity().getDesease())) {
					return 50;
				} else {
					int max = 0;
					Hand bestHand = null;
					for(Hand hand : state.getAllCharacterHand()) {
						int current = hand.stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(((CityCard)((Discard)gameAction).getCard()).getCity().getDesease())).collect(Collectors.toSet()).size();
						max = current>max ? current:max;
						bestHand = bestHand == null || current>max ? hand:bestHand;
					}
					if(bestHand == state.getCurrentHand()) {
						return 1;
					} else {
						return 10;
					}
				}
			}
		}
		return 1;
	}

	public static PlayerCard getCard(int cityId) {
		Set<PlayerCard> result = Reserve.getInstance().getPlayerCardReserve().stream().filter(card -> card instanceof CityCard && ((CityCard)card).getCity().id==cityId).collect(Collectors.toSet());
		if(result!=null) {
			return result.iterator().next();
		}
		throw new AssertionError(cityId+" does not refer to any city ID.");
	}
	
}
