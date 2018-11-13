package objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;

import gameStatus.Game;
import gameStatus.GameStatus;
import objects.card.Card;
import objects.card.CityCard;
import objects.card.Hand;
import objects.card.PlayerCard;
import util.GameUtil;

public class Character {
	
	private static Logger logger = LogManager.getLogger(Character.class.getName());
	private static Map<String, Object> notifyMap;
	City position;
	Hand hand;
	int actionCount;
	int currentActionCount;
	String name;
	GameStatus gameStatus;
	
	
	
	public Character(GameStatus gameStatus, City city, String name) {
		this.position = city;
		this.name = name;
		this.actionCount = 4;
		this.currentActionCount = 0;
		this.gameStatus=gameStatus;
		this.hand= new Hand();
	}
	
	public void draw() {
		
	}
	
	public City getPosition() {
		return this.position;
	}

	public void hand(PlayerCard card) {
		logger.info(this.name +" draws "+card.getTitle());
		hand.addOnTop(card);
		if(this.getHand().getCardDeck().size()>7) {
			notifyMap = new HashMap<String, Object>();
			notifyMap.put("discard", this);
			game.notifyObservers(notifyMap);
		}
	}
	
	public void newTurn() {
		this.currentActionCount = actionCount;
	}
	
	public boolean performAction(String str) {
		return false;
	}
	
	public boolean move(City city) {
		this.position = city;
		currentActionCount--;
		return true;
	}
	
	public boolean drive(City city) {
		
		for(DefaultEdge edge : game.getMap().outgoingEdgesOf(this.position)) {
			City target = game.getMap().getEdgeTarget(edge);
			if(target == city) {
				return move(city);
			}
		}
		return false;
	}
	
	public boolean directFlight(City city) {
		CityCard cityCard = hand.getCityCard(city);
		if(cityCard != null) {
			this.hand.discard(cityCard);
			return move(city);
		}
		return false;
	}
	
	public boolean charterFlight(City city) {
		CityCard cityCard = hand.getCityCard(position);
		if(cityCard != null) {
			this.hand.discard(cityCard);
			return move(city);
		}
		return false;
	}
	
	public boolean shuttleFlight(City city) {
		if(this.position.hasResearchCenter() && city.hasResearchCenter()) {
			return move(city);
		}
		return false;
	}
	
	public boolean buildResearchCenter() {
		CityCard cityCard = hand.getCityCard(position);
		if(cityCard != null && !position.hasResearchCenter()) {
			this.hand.discard(cityCard);
			ResearchCenter researchCenter = game.getReserve().getResearchCenter();
			if(researchCenter != null) {
				researchCenter.build(position);
				currentActionCount--;
				return true;
			}
		}
		return false;
	}
	
	public boolean treatDesease() {
		return treatDesease(this.position.getDesease());
	}
	
	public boolean treatDesease(Desease desease) {
		Set<Cube> cubeSet = this.position.getCubeSet(desease);
		if(cubeSet != null) {
			Iterator<Cube> it = cubeSet.iterator();
			if(it.hasNext()) {
				Cube cube = it.next();
				this.position.removeCube(cube);
				game.getReserve().addCube(cube);
				currentActionCount--;
				return true;
			}
		}
		return false;
	}
	
	public boolean shareKnowledge(Character character) {
		CityCard cityCard = hand.getCityCard(position);
		if(cityCard != null) {
			giveCard(character, cityCard);
		} else {
			cityCard = character.getHand().getCityCard(position);
			if(cityCard != null) {
				takeCard(character, cityCard);
			}
		}
		return false;
	}
	
	public boolean giveCard(Character character, CityCard card) {
		this.hand.removeCard(card);
		character.getHand().addOnTop(card);
		currentActionCount--;
		return true;
	}
	
	public boolean takeCard(Character character, CityCard card) {
		character.getHand().removeCard(card);
		this.hand.addOnTop(card);
		currentActionCount--;
		return true;
	}
	
	public boolean discoverCure(Desease desease, Set<Card> set) {
		if(!desease.isCured() && this.position.hasResearchCenter()) {
			Set<Card> cardSetDesease = set.stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(desease)).collect(Collectors.toSet());
			if(cardSetDesease != null && cardSetDesease.size()>=5) {
				this.hand.discard(game, cardSetDesease);
				desease.findCure();
				currentActionCount--;
				return true;
			}
		}
		return false;
	}
	
	public Hand getHand() {
		return this.hand;
	}

	public String getName() {
		return this.name;
	}
	
	public int getCurrentActionCount() {
		return this.currentActionCount;
	}

	public void setCurrentActionCount(int i) {
		this.currentActionCount = i;
		
	}

	public boolean canPlay() {
		return this.currentActionCount>0;
	}
	
	

}
