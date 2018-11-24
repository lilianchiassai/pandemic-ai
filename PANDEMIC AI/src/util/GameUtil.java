package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import game.GameProperties;
import game.GameStatus;
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.card.Card;
import objects.card.CityCard;
import objects.card.PlayerCard;

public class GameUtil {
	
	
	
	public static void log(GameStatus gameStatus, Logger logger, String message) {
		if(gameStatus.isSimulation()) {
			logger.debug(message);
		} else {
			logger.info(message);
		}
	}
	
	public static Predicate<Cube> getCubePredicate(Desease desease) {
		return (cube) -> (cube.getDesease() == desease);
	}
	
	public static Predicate<? super Card> getCityCardPredicate() {
		return (card) -> (card.getClass().isAssignableFrom(CityCard.class));
	}
	
	
	public static Predicate<? super Card> getCityCardPredicate(City city) {
		return (card) -> (card.getClass().isAssignableFrom(CityCard.class) && ((CityCard)card).getCity() == city );
	}

	public static Predicate<? super CityCard> getCityCardPredicate(Desease desease) {
		return (card) -> (((CityCard)card).getCity().getDesease() == desease);
	}

	public static Predicate<? super City> getCityNamePredicate(String name) {
		return (city) -> (city.getClass().isAssignableFrom(City.class) && ((City)city).getName().equals(name));
	}

	public static Predicate<? super Character> getCharacterNamePredicate(String name) {
		return (character) -> (character.getClass().isAssignableFrom(Character.class) && ((Character)character).getName().equals(name));
	}

	public static Predicate<? super Desease> getDeseaseNamePredicate(String name) {
		return (desease) -> (desease.getClass().isAssignableFrom(Desease.class) && ((Desease)desease).getName().equals(name));
	}
	
	public static Graph<City, DefaultEdge> makeBidirectionnal(Graph<City, DefaultEdge> graph) {
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
	}

	public static Predicate<? super Card> getCardTitlePredicate(String title) {
		return (card) -> (card instanceof Card && ((Card)card).getTitle().equals(title));
	}
	
	public static City getCity(String cityName) {
		Set<City> citySet = (Set<City>) GameProperties.map.stream().filter(GameUtil.getCityNamePredicate(cityName)).collect(Collectors.toSet());
		if(citySet != null) {
			Iterator<City> it = citySet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}
	
	public static Character getCardOwner(GameStatus gameStatus, Card card) {
		for(Character character : GameProperties.characterReserve) {
			if(gameStatus.getCharacterHand(character).getCardDeck().contains(card)) {
				return character;
			}
		}
		return null;
	}

	public static Character getPlayer(String playerName) {
		Set<Character> playerSet =  (Set<Character>) GameProperties.characterReserve.stream().filter(GameUtil.getCharacterNamePredicate(playerName)).collect(Collectors.toSet());
		if(playerSet != null) {
			Iterator<Character> it = playerSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}

	public static Desease getDesease(String deseaseName) {
		Set<Desease> deseaseSubSet = (Set<Desease>) GameProperties.deseaseSet.stream().filter(GameUtil.getDeseaseNamePredicate(deseaseName)).collect(Collectors.toSet());
		if(deseaseSubSet != null) {
			Iterator<Desease> it = deseaseSubSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}
	
	public static <T> T deepCopy(T object) throws Exception
    {
        //Serialization of object
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(object);
 
        //De-serialization of object
        ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        T copied = (T) in.readObject();
 
        //Verify that object is not corrupt
 
        //validateNameParts(fName);
        //validateNameParts(lName);
 
        return copied;
    }

	public static Card getCard(String title) {
		Set<PlayerCard> cardSet = (Set<PlayerCard>) GameProperties.playerCardReserve.stream().filter(GameUtil.getCardTitlePredicate(title)).collect(Collectors.toSet());
		if(cardSet != null) {
			Iterator<PlayerCard> it = cardSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}
	
}
