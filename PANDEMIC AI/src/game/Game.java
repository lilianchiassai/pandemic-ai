package game;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.SimpleGraph;

import objects.City;
import objects.Cube;
import objects.Desease;
import objects.Player;
import objects.Reserve;
import objects.card.CityCard;
import objects.card.Deck;
import objects.card.EpidemicCard;
import objects.card.PlayerCard;
import objects.card.PropagationCard;
import util.GameUtil;

public class Game {
	
	private static Logger logger = LogManager.getLogger(Game.class.getName());
	
	private boolean over;
	private boolean win;
	private int eclosionCounter;
	private int propagationSpeed;
	private int epidemicCounter;
	private int maxEclosionCounter = 7;
	private Set<City> eclosionCities;
	private City chainEclosion;
	
	private Graph<City, DefaultEdge> map;
	private Reserve reserve;
	private Set<Desease> deseaseSet;
	private Deck propagationDeck;
	private Deck playerDeck;
	private List<Player> players;
	private int numberOfPlayers;
	private int currentPlayerIndex;
	
	
	public Game(int numberOfPlayers, int difficulty){
		logger.info("Instantiating new game.");
		
		//Game status
		over = false;
		win = false;
		eclosionCounter=0;
		propagationSpeed=2;
		epidemicCounter=0;
		eclosionCities = new HashSet<City>();
		
		// Instantiate deseases
		logger.info("Loading deseases.");
		Desease yellow = new Desease("Yellow");
		Desease red = new Desease("Red");
		Desease blue = new Desease("Blue");
		Desease black = new Desease("Black");
		this.deseaseSet = new HashSet<Desease>();
		this.deseaseSet.add(yellow);
		this.deseaseSet.add(blue);
		this.deseaseSet.add(red);
		this.deseaseSet.add(black);
		
		// Instantiate Reserve
		logger.info("Building reserve.");
		this.reserve = new Reserve(this.deseaseSet);
		
		// Instantiate Cities
		logger.info("Creating city network.");
		City sanFrancisco = new City("San Francisco", blue, 21000);
		City chicago = new City("Chicago", blue, 21000);
		City atlanta = new City("Atlanta", blue, 21000);
		City montreal = new City("Montreal", blue, 21000);
		City washington = new City("Washington", blue, 21000);
		City newYork = new City("New York", blue, 21000);
		City madrid = new City("Madrid", blue, 21000);
		City london = new City("London", blue, 21000);
		City paris = new City("Paris", blue, 21000);
		City essen = new City("Essen", blue, 21000);
		City milan = new City("Milan", blue, 21000);
		City saintPetersbourg = new City("Saint Petersbourg", blue, 21000);
		
		City losAngeles = new City("Los Angeles", yellow, 21000);
		City mexicoCity = new City("Mexico City", yellow, 21000);
		City miami = new City("Miami", yellow, 21000);
		City bogota = new City("Bogota", yellow, 21000);
		City lima = new City("Lima", yellow, 21000);
		City santiago = new City("Santiago", yellow, 21000);
		City buenosAires = new City("Buenos Aires", yellow, 21000);
		City saoPaulo = new City("Sao Paulo", yellow, 21000);
		City lagos = new City("Lagos", yellow, 21000);
		City khinshasa = new City("Khinshasa", yellow, 21000);
		City johannesburg = new City("Johannesburg", yellow, 21000);
		City khartoum = new City("Khartoum", yellow, 21000);
		
		City algers = new City("Algers", black, 21000);
		City cairo = new City("Cairo", black, 21000);
		City istanbul = new City("Istanbul", black, 21000);
		City moscow = new City("Moscow", black, 21000);
		City baghdad = new City("Baghdad", black, 21000);
		City ryadh = new City("Ryadh", black, 21000);
		City teheran = new City("Teheran", black, 21000);
		City karachi = new City("Karachi", black, 21000);
		City mumbai = new City("Mumbai", black, 21000);
		City delhi = new City("Delhi", black, 21000);
		City chennai = new City("Chennai", black, 21000);
		City kolkata = new City("Kolkata", black, 21000);
		
		City bangkok = new City("Bangkok", red, 21000);
		City jakarta = new City("Jakarta", red, 21000);
		City hoChiMinhCity = new City("Ho Chi Minh City", red, 21000);
		City hongKong = new City("Hong Kong", red, 21000);
		City shangai = new City("Shangai", red, 21000);
		City beijing = new City("Beijing", red, 21000);
		City seoul = new City("Seoul", red, 21000);
		City tokyo = new City("Tokyo", red, 21000);
		City osaka = new City("Osaka", red, 21000);
		City taipei = new City("Taipei", red, 21000);
		City manila = new City("Manila", red, 21000);
		City sydney = new City("Sydney", red, 21000);
		
		
		
		// Create map
		map = new DirectedMultigraph<>(DefaultEdge.class);
		map.addVertex(sanFrancisco);
		map.addVertex(chicago);
		map.addVertex(atlanta);
		map.addVertex(montreal);
		map.addVertex(washington);
		map.addVertex(newYork);
		map.addVertex(madrid);
		map.addVertex(london);
		map.addVertex(paris);
		map.addVertex(essen);
		map.addVertex(milan);
		map.addVertex(saintPetersbourg);

		map.addVertex(losAngeles);
		map.addVertex(mexicoCity);
		map.addVertex(miami);
		map.addVertex(bogota);
		map.addVertex(lima);
		map.addVertex(santiago);
		map.addVertex(buenosAires);
		map.addVertex(saoPaulo);
		map.addVertex(lagos);
		map.addVertex(khinshasa);
		map.addVertex(johannesburg);
		map.addVertex(khartoum);

		map.addVertex(algers);
		map.addVertex(cairo);
		map.addVertex(istanbul);
		map.addVertex(moscow);
		map.addVertex(baghdad);
		map.addVertex(ryadh);
		map.addVertex(teheran);
		map.addVertex(karachi);
		map.addVertex(mumbai);
		map.addVertex(delhi);
		map.addVertex(chennai);
		map.addVertex(kolkata);

		map.addVertex(bangkok);
		map.addVertex(jakarta);
		map.addVertex(hoChiMinhCity);
		map.addVertex(hongKong);
		map.addVertex(shangai);
		map.addVertex(beijing);
		map.addVertex(seoul);
		map.addVertex(tokyo);
		map.addVertex(osaka);
		map.addVertex(taipei);
		map.addVertex(manila);
		map.addVertex(sydney);
		
		map.addEdge(sanFrancisco, losAngeles);
		map.addEdge(sanFrancisco, tokyo);
		map.addEdge(sanFrancisco, manila);
		map.addEdge(sanFrancisco, chicago);	
		map.addEdge(chicago, losAngeles);
		map.addEdge(chicago, mexicoCity);
		map.addEdge(chicago, atlanta);
		map.addEdge(chicago, montreal);	
		map.addEdge(montreal, washington);
		map.addEdge(montreal, newYork);	
		map.addEdge(newYork, london);
		map.addEdge(newYork, washington);
		map.addEdge(newYork, madrid);	
		map.addEdge(washington, atlanta);
		map.addEdge(washington, miami);	
		map.addEdge(atlanta, miami);	
		map.addEdge(losAngeles, mexicoCity);
		map.addEdge(losAngeles, sydney);		
		map.addEdge(mexicoCity, miami);
		map.addEdge(mexicoCity, bogota);
		map.addEdge(mexicoCity, lima);		
		map.addEdge(bogota, miami);
		map.addEdge(bogota, lima);
		map.addEdge(bogota, buenosAires);
		map.addEdge(bogota, saoPaulo);		
		map.addEdge(lima, santiago);		
		map.addEdge(buenosAires, saoPaulo);		
		map.addEdge(saoPaulo, lagos);
		map.addEdge(saoPaulo, madrid);		
		map.addEdge(london, madrid);
		map.addEdge(london, paris);
		map.addEdge(london, essen);		
		map.addEdge(madrid, paris);
		map.addEdge(madrid, algers);		
		map.addEdge(paris, essen);
		map.addEdge(paris, algers);
		map.addEdge(paris, milan);		
		map.addEdge(essen, milan);
		map.addEdge(essen, saintPetersbourg);		
		map.addEdge(saintPetersbourg, moscow);
		map.addEdge(saintPetersbourg, istanbul);		
		map.addEdge(milan, istanbul);	
		map.addEdge(istanbul, algers);
		map.addEdge(istanbul, cairo);
		map.addEdge(istanbul, baghdad);
		map.addEdge(istanbul, moscow);		
		map.addEdge(cairo, algers);
		map.addEdge(cairo, baghdad);
		map.addEdge(cairo, ryadh);
		map.addEdge(cairo, khartoum);		
		map.addEdge(khartoum, lagos);
		map.addEdge(khartoum, khinshasa);
		map.addEdge(khartoum, johannesburg);		
		map.addEdge(khinshasa, lagos);
		map.addEdge(khinshasa, johannesburg);		
		map.addEdge(baghdad, teheran);
		map.addEdge(baghdad, karachi);
		map.addEdge(baghdad, ryadh);		
		map.addEdge(teheran, moscow);
		map.addEdge(teheran, karachi);
		map.addEdge(teheran, delhi);		
		map.addEdge(karachi, ryadh);
		map.addEdge(karachi, delhi);
		map.addEdge(karachi, mumbai);		
		map.addEdge(delhi, mumbai);
		map.addEdge(delhi, chennai);
		map.addEdge(delhi, kolkata);		
		map.addEdge(mumbai, chennai);		
		map.addEdge(chennai, kolkata);
		map.addEdge(chennai, bangkok);
		map.addEdge(chennai, jakarta);		
		map.addEdge(kolkata, bangkok);
		map.addEdge(kolkata, hongKong);		
		map.addEdge(bangkok, jakarta);
		map.addEdge(bangkok, hongKong);
		map.addEdge(bangkok, hoChiMinhCity);		
		map.addEdge(jakarta, hoChiMinhCity);
		map.addEdge(jakarta, sydney);		
		map.addEdge(hoChiMinhCity, manila);
		map.addEdge(hoChiMinhCity, hongKong);		
		map.addEdge(manila, sydney);
		map.addEdge(manila, hongKong);
		map.addEdge(manila, taipei);		
		map.addEdge(hongKong, taipei);
		map.addEdge(hongKong, shangai);		
		map.addEdge(shangai, taipei);
		map.addEdge(shangai, beijing);
		map.addEdge(shangai, seoul);
		map.addEdge(shangai, tokyo);	
		map.addEdge(taipei, osaka);		
		map.addEdge(osaka, tokyo);		
		map.addEdge(tokyo, seoul);
		map.addEdge(seoul, beijing);
		
		map = GameUtil.makeBidirectionnal(map);
		// Create players
		logger.info(numberOfPlayers+" new players in Atlanta.");
		this.numberOfPlayers = numberOfPlayers;
		this.players = new LinkedList<Player>();
		for(int i=0; i< this.numberOfPlayers; i++) {
			players.add(new Player(this, atlanta, "Player "+(i+1)));
		}
		this.currentPlayerIndex = 0;
		
		// Create decks
		logger.info("Shuffling decks");
		propagationDeck = new Deck(PropagationCard.class); 
		playerDeck = new Deck(PlayerCard.class); 
		for(City city : map.vertexSet()) {
			//Create propagation card
			PropagationCard propagationCard = new PropagationCard(city);
			propagationDeck.addOnTop(propagationCard);
			CityCard cityCard = new CityCard(city);
			playerDeck.addOnTop(cityCard);
		}
		propagationDeck.shuffle();
		
		// Deal cards to players
		logger.info("Dealing two cards to each player.");
		playerDeck.shuffle();
		for(Player player : players) {
			for(int i = 0; i<6-numberOfPlayers; i++) {
				player.hand((PlayerCard) playerDeck.draw());
			}
		}
		
		// Add Epidemic cards and rebuild deck
		logger.info("Splitting player deck.");
		List<Deck> deckList = playerDeck.split(difficulty);
		for(Deck subdeck : deckList) {
			subdeck.addOnTop(new EpidemicCard());
			subdeck.shuffle();
			playerDeck.addOnTop(subdeck);
		}
		
		// Add research center on atlanta
		logger.info("Building a new Research Center in Atlanta.");
		this.reserve.getResearchCenter().build(atlanta);
		
		// Draw 9 propagation card
		for (int i = 3; i>0; i--) {
			for (int k = 0; k<3; k++) {
				PropagationCard card = (PropagationCard) this.propagationDeck.draw();
				this.infect(card.getCity(), i);
				this.propagationDeck.discard(card);
			}
		}
		
	}
	
	private void infect(City city) {
		infect(city, 1, city.getDesease());
	}
	
	private void infect(City city, int strength) {
		infect(city, strength, city.getDesease());
	}
	
	private void infect(City city, int strength, Desease desease) {
		//Check if eclosion
		logger.info("The "+desease.getName()+" desease spreads in "+city.getName()+".");
		boolean eclosion = false;
		int cubeCounter = 0;
		while(cubeCounter<strength && !eclosion) {
			Set<Cube> cubeSet = city.getCubeSet(desease);
			if(cubeSet.size() == 3) {
				eclosion = true;
				eclosion(city, desease);
			} else {
				Cube cube = reserve.getCube(desease);
				if(cube != null) {
					city.addCube(cube);
				} else {
					lose();
				}
			}
			cubeCounter++;
		}
	}
	
	private void eclosion(City city, Desease desease) {
		logger.info("Eclosion in "+city.getName());
		eclosionCounter++;
		if(eclosionCounter > maxEclosionCounter) {
			lose();
		} else {
			if(chainEclosion == null) {
				chainEclosion = city;
			}
			eclosionCities.add(city);
			for(DefaultEdge edge : map.outgoingEdgesOf(city)) {
				City target = map.getEdgeTarget(edge);
				if(chainEclosion == null || !eclosionCities.contains(target)) {
					logger.info("Desease is spreading "+city.getName());
					infect(target, 1, desease);
				}
			}
		}
		if(chainEclosion == city) {
			eclosionCities.clear();
		}
	}
	
	public void endTurn() {
		logger.info(getCurrentPlayer().getName()+" ends his turn.");
		//current Player draws
		for(int i = 0; i<2; i++) {
			PlayerCard card = (PlayerCard) playerDeck.draw();
			if(card == null) {
				lose();
			}
			if(card instanceof EpidemicCard) {
				//do Epidemy
				logger.info("New Epidemic... The world will soon come to an end !");
				PropagationCard infectedCard = (PropagationCard) propagationDeck.drawBottomCard();
				infect(infectedCard.getCity(), 3);
				epidemicCounter++;
				if(epidemicCounter == 3 || epidemicCounter == 5) {
					propagationSpeed++;
				}
				propagationDeck.discard(infectedCard);
				Deck propagationDiscardPile = propagationDeck.getDiscardPile();
				propagationDiscardPile.shuffle();
				propagationDeck.addOnTop(propagationDiscardPile);
			} else {
				getCurrentPlayer().hand(card);
			}
		}
		
		
		//propagation
		for(int i = 0; i<propagationSpeed; i++) {
			PropagationCard card = (PropagationCard) this.propagationDeck.draw();
			this.infect(card.getCity());
			this.propagationDeck.discard(card);
		}
	}

	
	private void lose() {
		this.over = true;
		this.win = false;
	}
	
	public boolean isOver() {
		return this.over;
	}
	
	public boolean isWin() {
		return this.win;
	}
	
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	public Reserve getReserve() {
		return this.reserve;
	}

	public Graph<City, DefaultEdge> getMap() {
		return this.map;
	}

	public Deck getPlayerDeck() {
		return this.playerDeck;
	}

	public Player nextPlayer() {
		currentPlayerIndex = (currentPlayerIndex+1) % numberOfPlayers;
		logger.info(getCurrentPlayer().getName()+" starts a new turn.");
		getCurrentPlayer().newTurn();
		return getCurrentPlayer();
	}

	public City getCity(String cityName) {
		Set<City> citySet = (Set<City>) this.map.vertexSet().stream().filter(GameUtil.getCityNamePredicate(cityName)).collect(Collectors.toSet());
		if(citySet != null) {
			Iterator<City> it = citySet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}

	public Player getPlayer(String playerName) {
		Set<Player> playerSet =  (Set<Player>) players.stream().filter(GameUtil.getPlayerNamePredicate(playerName)).collect(Collectors.toSet());
		if(playerSet != null) {
			Iterator<Player> it = playerSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}

	public Desease getDesease(String deseaseName) {
		Set<Desease> deseaseSubSet = (Set<Desease>) deseaseSet.stream().filter(GameUtil.getDeseaseNamePredicate(deseaseName)).collect(Collectors.toSet());
		if(deseaseSubSet != null) {
			Iterator<Desease> it = deseaseSubSet.iterator();
			if(it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}
	
	
}
