package game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;

import game.action.Build;
import game.action.CharterFlight;
import game.action.Cure;
import game.action.DirectFlight;
import game.action.Drive;
import game.action.GameAction;
import game.action.ShareKnowledge;
import game.action.ShuttleFlight;
import game.action.Treat;
import objects.Character;
import objects.City;
import objects.Cube;
import objects.Desease;
import objects.ResearchCenter;
import objects.Reserve;
import objects.card.CityCard;
import objects.card.Deck;
import objects.card.EpidemicCard;
import objects.card.PlayerCard;
import objects.card.PropagationCard;
import util.GameUtil;

public class GameProperties {

	private static Logger logger = LogManager.getLogger(GameProperties.class.getName());
	
	public static Set<Class<? extends GameAction>> actionTypeSet;
	static int maxEclosionCounter;	
	public static int actionCount;
	static int[] propagationSpeed;
	public static Set<Desease> deseaseSet;
	public static Graph<City, DefaultEdge> map;
	static Map<Desease, Set<Cube>> cubeReserve;
	static Set<ResearchCenter> researchCenterReserve;
	static Set<PlayerCard> playerCardReserve;
	static Set<PropagationCard> propagationCardReserve;
	static Set<EpidemicCard> epidemicCardReserve;
	
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
		
		GameProperties.map = new DirectedMultigraph<>(DefaultEdge.class);
		
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
		
		// Instantiate Reserve
		logger.info("Building reserve.");
		for(Desease desease : deseaseSet) {
			HashSet<Cube> cubeSet = new HashSet<Cube>();
			for(int i = 0; i<24 ; i++) {
				cubeSet.add(new Cube(desease));
			}
			cubeReserve.put(desease,cubeSet);
		}
		for(int i = 0; i<6 ; i++) {
			researchCenterReserve.add(new ResearchCenter());
		}
		
		//Create Cards
		logger.info("Creating Cards");
		for(City city : GameProperties.map.vertexSet()) {
			//Create propagation card
			propagationCardReserve.add(new PropagationCard(city));
			playerCardReserve.add(new CityCard(city));
			
		}
		for(int i = 0; i<10; i++) {
			epidemicCardReserve.add(new EpidemicCard());
		}
		
		
		for(City city : GameProperties.map.vertexSet()) {
			//Create propagation card
			propagationCardReserve.add(new PropagationCard(city));
			playerCardReserve.add(new CityCard(city));
			
		}
		
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
		
	}
	
	public static int getPropagationSpeed(int epidemicCounter) {
		return propagationSpeed[epidemicCounter];
	}
	
}
