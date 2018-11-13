package util;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import gameStatus.Game;
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.card.Card;
import objects.card.CityCard;

public class GameUtil {
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
		return (card) -> (card.getClass().isAssignableFrom(CityCard.class) && ((CityCard)card).getCity().getDesease() == desease);
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
	
	public static City getCity(Game game, String cityName) {
		Set<City> citySet = (Set<City>) game.getGameStatus().getMap().vertexSet().stream().filter(GameUtil.getCityNamePredicate(cityName)).collect(Collectors.toSet());
		if(citySet != null) {
			Iterator<City> it = citySet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}

	public static Character getPlayer(Game game, String playerName) {
		Set<Character> playerSet =  (Set<Character>) game.getGameStatus().getCharacterList().stream().filter(GameUtil.getCharacterNamePredicate(playerName)).collect(Collectors.toSet());
		if(playerSet != null) {
			Iterator<Character> it = playerSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}

	public static Desease getDesease(Game game, String deseaseName) {
		Set<Desease> deseaseSubSet = (Set<Desease>) game.getGameStatus().getDeseaseSet().stream().filter(GameUtil.getDeseaseNamePredicate(deseaseName)).collect(Collectors.toSet());
		if(deseaseSubSet != null) {
			Iterator<Desease> it = deseaseSubSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}
	
}
