package util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.EdgeSetFactory;
import org.jgrapht.graph.SimpleGraph;

import objects.City;
import objects.Cube;
import objects.Desease;
import objects.Character;
import objects.Reserve;
import objects.Road;
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
	
	
}
