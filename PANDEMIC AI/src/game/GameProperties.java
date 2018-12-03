package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.action.Build;
import game.action.CharterFlight;
import game.action.Cure;
import game.action.DirectFlight;
import game.action.Discard;
import game.action.Drive;
import game.action.GameAction;
import game.action.GiveKnowledge;
import game.action.Pass;
import game.action.ReceiveKnowledge;
import game.action.ShareKnowledge;
import game.action.ShuttleFlight;
import game.action.Treat;
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.ResearchCenter;
import objects.card.Card;
import objects.card.CityCard;
import objects.card.EpidemicCard;
import objects.card.Hand;
import objects.card.PlayerCard;
import objects.card.PropagationCard;
import util.GameUtil;

public class GameProperties {

	private static Logger logger = LogManager.getLogger(GameProperties.class.getName());
	
	public static Set<Class<? extends GameAction>> actionTypeSet;
	public static Build buildAction;
	public static List<Pass> passActionList;
	public static Map<Desease,List<Treat>> treatAction;
	public static Map<Desease,List<Treat>> treatCuredAction;
	
	static int maxEclosionCounter;	
	public static int actionCount;
	static int[] propagationSpeed;
	public static Set<Desease> deseaseSet;
	public static Set<City> map;
	static Map<Desease, Set<Cube>> cubeReserve;
	public static Set<ResearchCenter> researchCenterReserve;
	public static Set<PlayerCard> playerCardReserve;
	static Set<PropagationCard> propagationCardReserve;
	public static EpidemicCard epidemicCardReserve;
	public static LinkedList<Character> characterReserve;

	public static int visitCount = 0;

	public static int victoryCount = 0;
	
	public GameProperties() {
		logger.info("Instantiating new game.");
		
		// Counters
		maxEclosionCounter = 7;
		actionCount = 4;
		GameProperties.propagationSpeed = new int[]{2, 2, 2, 3, 3, 4, 4};
		
		// Instantiate deseases
		logger.info("Loading deseases.");
		Desease yellow = new Desease("Yellow");
		Desease red = new Desease("Red");
		Desease blue = new Desease("Blue");
		Desease black = new Desease("Black");
		GameProperties.deseaseSet = new HashSet<Desease>();
		GameProperties.deseaseSet.add(yellow);
		GameProperties.deseaseSet.add(blue);
		GameProperties.deseaseSet.add(red);
		GameProperties.deseaseSet.add(black);
		
		GameProperties.map = new HashSet<City>();
		
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
		map.add(sanFrancisco);
		map.add(chicago);
		map.add(atlanta);
		map.add(montreal);
		map.add(washington);
		map.add(newYork);
		map.add(madrid);
		map.add(london);
		map.add(paris);
		map.add(essen);
		map.add(milan);
		map.add(saintPetersbourg);

		map.add(losAngeles);
		map.add(mexicoCity);
		map.add(miami);
		map.add(bogota);
		map.add(lima);
		map.add(santiago);
		map.add(buenosAires);
		map.add(saoPaulo);
		map.add(lagos);
		map.add(khinshasa);
		map.add(johannesburg);
		map.add(khartoum);

		map.add(algers);
		map.add(cairo);
		map.add(istanbul);
		map.add(moscow);
		map.add(baghdad);
		map.add(ryadh);
		map.add(teheran);
		map.add(karachi);
		map.add(mumbai);
		map.add(delhi);
		map.add(chennai);
		map.add(kolkata);

		map.add(bangkok);
		map.add(jakarta);
		map.add(hoChiMinhCity);
		map.add(hongKong);
		map.add(shangai);
		map.add(beijing);
		map.add(seoul);
		map.add(tokyo);
		map.add(osaka);
		map.add(taipei);
		map.add(manila);
		map.add(sydney);
		
		for(City city : map) {
			city.initGameActions(map);
		}
		
		sanFrancisco.addNeighbour(losAngeles);
		losAngeles.addNeighbour(sanFrancisco);
		sanFrancisco.addNeighbour(tokyo);
		tokyo.addNeighbour(sanFrancisco);
		sanFrancisco.addNeighbour(manila);
		manila.addNeighbour(sanFrancisco);
		sanFrancisco.addNeighbour(chicago);
		chicago.addNeighbour(sanFrancisco);	
		chicago.addNeighbour(losAngeles);
		losAngeles.addNeighbour(chicago);
		chicago.addNeighbour(mexicoCity);
		mexicoCity.addNeighbour(chicago);
		chicago.addNeighbour(atlanta);
		atlanta.addNeighbour(chicago);
		chicago.addNeighbour(montreal);
		montreal.addNeighbour(chicago);	
		montreal.addNeighbour(washington);
		washington.addNeighbour(montreal);
		montreal.addNeighbour(newYork);
		newYork.addNeighbour(montreal);	
		newYork.addNeighbour(london);
		london.addNeighbour(newYork);
		newYork.addNeighbour(washington);
		washington.addNeighbour(newYork);
		newYork.addNeighbour(madrid);
		madrid.addNeighbour(newYork);	
		washington.addNeighbour(atlanta);
		atlanta.addNeighbour(washington);
		washington.addNeighbour(miami);
		miami.addNeighbour(washington);	
		atlanta.addNeighbour(miami);
		miami.addNeighbour(atlanta);	
		losAngeles.addNeighbour(mexicoCity);
		mexicoCity.addNeighbour(losAngeles);
		losAngeles.addNeighbour(sydney);
		sydney.addNeighbour(losAngeles);		
		mexicoCity.addNeighbour(miami);
		miami.addNeighbour(mexicoCity);
		mexicoCity.addNeighbour(bogota);
		bogota.addNeighbour(mexicoCity);
		mexicoCity.addNeighbour(lima);
		lima.addNeighbour(mexicoCity);		
		bogota.addNeighbour(miami);
		miami.addNeighbour(bogota);
		bogota.addNeighbour(lima);
		lima.addNeighbour(bogota);
		bogota.addNeighbour(buenosAires);
		buenosAires.addNeighbour(bogota);
		bogota.addNeighbour(saoPaulo);
		saoPaulo.addNeighbour(bogota);		
		lima.addNeighbour(santiago);
		santiago.addNeighbour(lima);		
		buenosAires.addNeighbour(saoPaulo);
		saoPaulo.addNeighbour(buenosAires);		
		saoPaulo.addNeighbour(lagos);
		lagos.addNeighbour(saoPaulo);
		saoPaulo.addNeighbour(madrid);
		madrid.addNeighbour(saoPaulo);		
		london.addNeighbour(madrid);
		madrid.addNeighbour(london);
		london.addNeighbour(paris);
		paris.addNeighbour(london);
		london.addNeighbour(essen);
		essen.addNeighbour(london);		
		madrid.addNeighbour(paris);
		paris.addNeighbour(madrid);
		madrid.addNeighbour(algers);
		algers.addNeighbour(madrid);		
		paris.addNeighbour(essen);
		essen.addNeighbour(paris);
		paris.addNeighbour(algers);
		algers.addNeighbour(paris);
		paris.addNeighbour(milan);
		milan.addNeighbour(paris);		
		essen.addNeighbour(milan);
		milan.addNeighbour(essen);
		essen.addNeighbour(saintPetersbourg);
		saintPetersbourg.addNeighbour(essen);		
		saintPetersbourg.addNeighbour(moscow);
		moscow.addNeighbour(saintPetersbourg);
		saintPetersbourg.addNeighbour(istanbul);
		istanbul.addNeighbour(saintPetersbourg);		
		milan.addNeighbour(istanbul);
		istanbul.addNeighbour(milan);	
		istanbul.addNeighbour(algers);
		algers.addNeighbour(istanbul);
		istanbul.addNeighbour(cairo);
		cairo.addNeighbour(istanbul);
		istanbul.addNeighbour(baghdad);
		baghdad.addNeighbour(istanbul);
		istanbul.addNeighbour(moscow);
		moscow.addNeighbour(istanbul);		
		cairo.addNeighbour(algers);
		algers.addNeighbour(cairo);
		cairo.addNeighbour(baghdad);
		baghdad.addNeighbour(cairo);
		cairo.addNeighbour(ryadh);
		ryadh.addNeighbour(cairo);
		cairo.addNeighbour(khartoum);
		khartoum.addNeighbour(cairo);		
		khartoum.addNeighbour(lagos);
		lagos.addNeighbour(khartoum);
		khartoum.addNeighbour(khinshasa);
		khinshasa.addNeighbour(khartoum);
		khartoum.addNeighbour(johannesburg);
		johannesburg.addNeighbour(khartoum);		
		khinshasa.addNeighbour(lagos);
		lagos.addNeighbour(khinshasa);
		khinshasa.addNeighbour(johannesburg);
		johannesburg.addNeighbour(khinshasa);		
		baghdad.addNeighbour(teheran);
		teheran.addNeighbour(baghdad);
		baghdad.addNeighbour(karachi);
		karachi.addNeighbour(baghdad);
		baghdad.addNeighbour(ryadh);
		ryadh.addNeighbour(baghdad);		
		teheran.addNeighbour(moscow);
		moscow.addNeighbour(teheran);
		teheran.addNeighbour(karachi);
		karachi.addNeighbour(teheran);
		teheran.addNeighbour(delhi);
		delhi.addNeighbour(teheran);		
		karachi.addNeighbour(ryadh);
		ryadh.addNeighbour(karachi);
		karachi.addNeighbour(delhi);
		delhi.addNeighbour(karachi);
		karachi.addNeighbour(mumbai);
		mumbai.addNeighbour(karachi);		
		delhi.addNeighbour(mumbai);
		mumbai.addNeighbour(delhi);
		delhi.addNeighbour(chennai);
		chennai.addNeighbour(delhi);
		delhi.addNeighbour(kolkata);
		kolkata.addNeighbour(delhi);		
		mumbai.addNeighbour(chennai);
		chennai.addNeighbour(mumbai);		
		chennai.addNeighbour(kolkata);
		kolkata.addNeighbour(chennai);
		chennai.addNeighbour(bangkok);
		bangkok.addNeighbour(chennai);
		chennai.addNeighbour(jakarta);
		jakarta.addNeighbour(chennai);		
		kolkata.addNeighbour(bangkok);
		bangkok.addNeighbour(kolkata);
		kolkata.addNeighbour(hongKong);
		hongKong.addNeighbour(kolkata);		
		bangkok.addNeighbour(jakarta);
		jakarta.addNeighbour(bangkok);
		bangkok.addNeighbour(hongKong);
		hongKong.addNeighbour(bangkok);
		bangkok.addNeighbour(hoChiMinhCity);
		hoChiMinhCity.addNeighbour(bangkok);		
		jakarta.addNeighbour(hoChiMinhCity);
		hoChiMinhCity.addNeighbour(jakarta);
		jakarta.addNeighbour(sydney);
		sydney.addNeighbour(jakarta);		
		hoChiMinhCity.addNeighbour(manila);
		manila.addNeighbour(hoChiMinhCity);
		hoChiMinhCity.addNeighbour(hongKong);
		hongKong.addNeighbour(hoChiMinhCity);		
		manila.addNeighbour(sydney);
		sydney.addNeighbour(manila);
		manila.addNeighbour(hongKong);
		hongKong.addNeighbour(manila);
		manila.addNeighbour(taipei);
		taipei.addNeighbour(manila);		
		hongKong.addNeighbour(taipei);
		taipei.addNeighbour(hongKong);
		hongKong.addNeighbour(shangai);
		shangai.addNeighbour(hongKong);		
		shangai.addNeighbour(taipei);
		taipei.addNeighbour(shangai);
		shangai.addNeighbour(beijing);
		beijing.addNeighbour(shangai);
		shangai.addNeighbour(seoul);
		seoul.addNeighbour(shangai);
		shangai.addNeighbour(tokyo);
		tokyo.addNeighbour(shangai);	
		taipei.addNeighbour(osaka);
		osaka.addNeighbour(taipei);		
		osaka.addNeighbour(tokyo);
		tokyo.addNeighbour(osaka);		
		tokyo.addNeighbour(seoul);
		seoul.addNeighbour(tokyo);
		seoul.addNeighbour(beijing);
		beijing.addNeighbour(seoul);
		
		
		//Init distances
		for(City city : map) {
			city.initDistances(map);
		}
		
		// Instantiate Reserve
		logger.info("Building reserve.");
		
		characterReserve = new LinkedList<Character>();
		//Create characters
		for(int i = 0; i<4; i++) {
			characterReserve.add(new Character("Player "+(i+1)));
		}
		//Create Cubes & research center	
		cubeReserve = new HashMap<Desease, Set<Cube>>();
		for(Desease desease : deseaseSet) {
			HashSet<Cube> cubeSet = new HashSet<Cube>();
			for(int i = 0; i<24 ; i++) {
				cubeSet.add(new Cube(desease));
			}
			cubeReserve.put(desease,cubeSet);
		}
		researchCenterReserve = new HashSet<ResearchCenter>();
		for(int i = 0; i<6 ; i++) {
			researchCenterReserve.add(new ResearchCenter());
		}
		
		//Create Cards
		logger.info("Creating Cards");
		propagationCardReserve = new HashSet<PropagationCard>();
		playerCardReserve = new HashSet<PlayerCard>();
		for(City city : map) {
			//Create propagation card
			propagationCardReserve.add(new PropagationCard(city));
			playerCardReserve.add(new CityCard(city));
			
		}
		epidemicCardReserve = new EpidemicCard();
		
		//Instantiate action rules
		actionTypeSet = new HashSet<Class<? extends GameAction>>();
		actionTypeSet.add(Drive.class);
		actionTypeSet.add(CharterFlight.class);
		actionTypeSet.add(DirectFlight.class);
		actionTypeSet.add(ShuttleFlight.class);
		actionTypeSet.add(Treat.class);
		actionTypeSet.add(Cure.class);
		actionTypeSet.add(Build.class);
		actionTypeSet.add(ShareKnowledge.class);
		actionTypeSet.add(Pass.class);
		
		//Instantiate default actions
		buildAction = new Build();
		passActionList = new ArrayList<Pass>();
		for(int i = 0; i<=actionCount; i++) {
			passActionList.add(new Pass(i));
		}
		treatAction = new HashMap<Desease, List<Treat>>();
		treatCuredAction = new HashMap<Desease, List<Treat>>();
		for(Desease desease : GameProperties.deseaseSet) {
			List treatActionList = new ArrayList<GameAction>();
			List treatCuredActionList = new ArrayList<GameAction>();
			for(int strength = 1; strength<=3;strength++) {
				treatActionList.add(new Treat(desease, strength,strength));
				treatCuredActionList.add(new Treat(desease, strength));
			}
			treatAction.put(desease, treatActionList);
			treatCuredAction.put(desease, treatActionList);
		}
		
	}
	
	public static int getPropagationSpeed(int epidemicCounter) {
		return propagationSpeed[epidemicCounter];
	}
	
	public static int getActionWeight(GameStatus gameStatus, GameAction gameAction) {
		if (gameAction.getClass().isAssignableFrom(Drive.class)) {
			return (gameStatus.getCityCubeSet(((Drive)gameAction).getDestination(), ((Drive)gameAction).getDestination().getDesease()).size()) *10 +10;
		} else if (gameAction.getClass().isAssignableFrom(DirectFlight.class)) {
			return gameStatus.getCityCubeSet(((DirectFlight)gameAction).getDestination(), ((DirectFlight)gameAction).getDestination().getDesease()).size()*5 +1;
		} else if (gameAction.getClass().isAssignableFrom(CharterFlight.class)) {
			return gameStatus.getCityCubeSet(((CharterFlight)gameAction).getDestination(), ((CharterFlight)gameAction).getDestination().getDesease()).size()*5 +1;
		} else if (gameAction.getClass().isAssignableFrom(ShuttleFlight.class)) {
			return (gameStatus.getCityCubeSet(((ShuttleFlight)gameAction).getDestination(), ((ShuttleFlight)gameAction).getDestination().getDesease()).size()) *10+5;
		} else if (gameAction.getClass().isAssignableFrom(Treat.class)) {
			return gameStatus.getCityCubeSet(gameStatus.getCurrentCharacterPosition(), gameStatus.getCurrentCharacterPosition().getDesease()).size() * 200;
		} else if (gameAction.getClass().isAssignableFrom(ShareKnowledge.class)) {
			if(gameStatus.getCuredDeseaseSet().contains(gameStatus.getCurrentCharacterPosition().getDesease())) {
				return 0;
			} else {
				int current = gameStatus.getCurrentHand().getCardDeck().stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(gameStatus.getCurrentCharacterPosition().getDesease())).collect(Collectors.toSet()).size();
				int other = gameStatus.getCharacterHand(((ShareKnowledge)gameAction).getCharacter()).getCardDeck().stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(gameStatus.getCurrentCharacterPosition().getDesease())).collect(Collectors.toSet()).size();
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
			for(City city : gameStatus.getCurrentCharacterPosition().getNeighbourSet()) {
				if(gameStatus.hasResearchCenter(city)) {
					return 0;
				}
			}
			if(!gameStatus.getCuredDeseaseSet().contains(gameStatus.getCurrentCharacterPosition().getDesease())) {
				int max = 0;
				Hand bestHand = null;
				for(Hand hand : gameStatus.getCharacterHandList()) {
					int current = hand.getCardDeck().stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(gameStatus.getCurrentCharacterPosition().getDesease())).collect(Collectors.toSet()).size();
					max = current>max ? current:max;
					bestHand = bestHand == null || current>max ? hand:bestHand;
				}
				if(bestHand == gameStatus.getCurrentHand()) {
					return 1;
				}
			}
			return 40;
		} else if (gameAction.getClass().isAssignableFrom(Pass.class)) {
			return 0;
		} else if (gameAction.getClass().isAssignableFrom(Discard.class)) {
			if(((Discard)gameAction).getCard() instanceof CityCard) {
				if(gameStatus.getCuredDeseaseSet().contains(((CityCard)((Discard)gameAction).getCard()).getCity().getDesease())) {
					return 50;
				} else {
					int max = 0;
					Hand bestHand = null;
					for(Hand hand : gameStatus.getCharacterHandList()) {
						int current = hand.getCardDeck().stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(((CityCard)((Discard)gameAction).getCard()).getCity().getDesease())).collect(Collectors.toSet()).size();
						max = current>max ? current:max;
						bestHand = bestHand == null || current>max ? hand:bestHand;
					}
					if(bestHand == gameStatus.getCurrentHand()) {
						return 1;
					} else {
						return 10;
					}
				}
			}
		}
		return 1;
	}
	
	
	
}
