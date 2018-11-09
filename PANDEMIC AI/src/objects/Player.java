package objects;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;

import game.Game;
import objects.card.Card;
import objects.card.CityCard;
import objects.card.Hand;
import objects.card.PlayerCard;
import util.GameUtil;

public class Player {
	
	private static Logger logger = LogManager.getLogger(Player.class.getName());
	
	City position;
	Hand hand;
	int actionCount;
	int currentActionCount;
	String name;
	Game game;
	
	public Player(Game game, City city, String name) {
		this.position = city;
		this.name = name;
		this.actionCount = 4;
		this.currentActionCount = 0;
		this.game=game;
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
	}
	
	public void newTurn() {
		this.currentActionCount = actionCount;
	}
	
	public boolean performAction(String str) {
		return false;
	}
	
	private boolean move(City city) {
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
	
	public boolean shareKnowledge(Player player) {
		CityCard cityCard = hand.getCityCard(position);
		if(cityCard != null) {
			giveCard(player, cityCard);
		} else {
			cityCard = player.getHand().getCityCard(position);
			if(cityCard != null) {
				takeCard(player, cityCard);
			}
		}
		return false;
	}
	
	private boolean giveCard(Player player, CityCard card) {
		this.hand.removeCard(card);
		player.getHand().addOnTop(card);
		currentActionCount--;
		return true;
	}
	
	private boolean takeCard(Player player, CityCard card) {
		player.getHand().removeCard(card);
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

}
