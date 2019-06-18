package pandemic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.material.PlayedCharacter;
import pandemic.material.card.CityCard;
import pandemic.material.card.EpidemicCard;
import pandemic.material.card.PlayerCard;
import pandemic.material.card.PropagationCard;

public final class Reserve {

  private volatile static Reserve instance = null;

  private static Logger logger = LogManager.getLogger(Reserve.class.getName());
  Set<PlayerCard> playerCardReserve;
  Set<PropagationCard> propagationCardReserve;
  EpidemicCard epidemicCardReserve;
  private LinkedList<PlayedCharacter> characterReserve;
  private ArrayList<Desease> deseaseSet;
  private Map<String, City> map;
  Map<Desease, ArrayList<City>> deseaseCityCardMap;


  private Reserve() {

    // Instantiate deseases
    logger.info("Loading deseases.");
    Desease yellow = new Desease("Yellow");
    Desease red = new Desease("Red");
    Desease blue = new Desease("Blue");
    Desease black = new Desease("Black");
    deseaseSet = new ArrayList<Desease>();
    getDeseaseSet().add(yellow);
    getDeseaseSet().add(red);
    getDeseaseSet().add(blue);
    getDeseaseSet().add(black);

    map = new HashMap<String, City>();

    logger.info("Creating city network.");
    City sanFrancisco = new City("San Francisco", blue);
    City chicago = new City("Chicago", blue);
    City atlanta = new City("Atlanta", blue);
    City montreal = new City("Montreal", blue);
    City washington = new City("Washington", blue);
    City newYork = new City("New York", blue);
    City madrid = new City("Madrid", blue);
    City london = new City("London", blue);
    City paris = new City("Paris", blue);
    City essen = new City("Essen", blue);
    City milan = new City("Milan", blue);
    City saintPetersbourg = new City("Saint Petersbourg", blue);

    City losAngeles = new City("Los Angeles", yellow);
    City mexicoCity = new City("Mexico City", yellow);
    City miami = new City("Miami", yellow);
    City bogota = new City("Bogota", yellow);
    City lima = new City("Lima", yellow);
    City santiago = new City("Santiago", yellow);
    City buenosAires = new City("Buenos Aires", yellow);
    City saoPaulo = new City("Sao Paulo", yellow);
    City lagos = new City("Lagos", yellow);
    City khinshasa = new City("Khinshasa", yellow);
    City johannesburg = new City("Johannesburg", yellow);
    City khartoum = new City("Khartoum", yellow);

    City algers = new City("Algers", black);
    City cairo = new City("Cairo", black);
    City istanbul = new City("Istanbul", black);
    City moscow = new City("Moscow", black);
    City baghdad = new City("Baghdad", black);
    City ryadh = new City("Ryadh", black);
    City teheran = new City("Teheran", black);
    City karachi = new City("Karachi", black);
    City mumbai = new City("Mumbai", black);
    City delhi = new City("Delhi", black);
    City chennai = new City("Chennai", black);
    City kolkata = new City("Kolkata", black);

    City bangkok = new City("Bangkok", red);
    City jakarta = new City("Jakarta", red);
    City hoChiMinhCity = new City("Ho Chi Minh City", red);
    City hongKong = new City("Hong Kong", red);
    City shangai = new City("Shangai", red);
    City beijing = new City("Beijing", red);
    City seoul = new City("Seoul", red);
    City tokyo = new City("Tokyo", red);
    City osaka = new City("Osaka", red);
    City taipei = new City("Taipei", red);
    City manila = new City("Manila", red);
    City sydney = new City("Sydney", red);

    // Create map
    getMap().put(sanFrancisco.getName(), sanFrancisco);
    getMap().put(chicago.getName(), chicago);
    getMap().put(atlanta.getName(), atlanta);
    getMap().put(montreal.getName(), montreal);
    getMap().put(washington.getName(), washington);
    getMap().put(newYork.getName(), newYork);
    getMap().put(madrid.getName(), madrid);
    getMap().put(london.getName(), london);
    getMap().put(paris.getName(), paris);
    getMap().put(essen.getName(), essen);
    getMap().put(milan.getName(), milan);
    getMap().put(saintPetersbourg.getName(), saintPetersbourg);

    getMap().put(losAngeles.getName(), losAngeles);
    getMap().put(mexicoCity.getName(), mexicoCity);
    getMap().put(miami.getName(), miami);
    getMap().put(bogota.getName(), bogota);
    getMap().put(lima.getName(), lima);
    getMap().put(santiago.getName(), santiago);
    getMap().put(buenosAires.getName(), buenosAires);
    getMap().put(saoPaulo.getName(), saoPaulo);
    getMap().put(lagos.getName(), lagos);
    getMap().put(khinshasa.getName(), khinshasa);
    getMap().put(johannesburg.getName(), johannesburg);
    getMap().put(khartoum.getName(), khartoum);

    getMap().put(algers.getName(), algers);
    getMap().put(cairo.getName(), cairo);
    getMap().put(istanbul.getName(), istanbul);
    getMap().put(moscow.getName(), moscow);
    getMap().put(baghdad.getName(), baghdad);
    getMap().put(ryadh.getName(), ryadh);
    getMap().put(teheran.getName(), teheran);
    getMap().put(karachi.getName(), karachi);
    getMap().put(mumbai.getName(), mumbai);
    getMap().put(delhi.getName(), delhi);
    getMap().put(chennai.getName(), chennai);
    getMap().put(kolkata.getName(), kolkata);

    getMap().put(bangkok.getName(), bangkok);
    getMap().put(jakarta.getName(), jakarta);
    getMap().put(hoChiMinhCity.getName(), hoChiMinhCity);
    getMap().put(hongKong.getName(), hongKong);
    getMap().put(shangai.getName(), shangai);
    getMap().put(beijing.getName(), beijing);
    getMap().put(seoul.getName(), seoul);
    getMap().put(tokyo.getName(), tokyo);
    getMap().put(osaka.getName(), osaka);
    getMap().put(taipei.getName(), taipei);
    getMap().put(manila.getName(), manila);
    getMap().put(sydney.getName(), sydney);



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



    // Instantiate Reserve
    logger.info("Building reserve.");
    characterReserve = new LinkedList<PlayedCharacter>();
    propagationCardReserve = new HashSet<PropagationCard>();
    playerCardReserve = new HashSet<PlayerCard>();


    // Create Cards
    logger.info("Creating Cards");
    for (City city : getMap().values()) {
      // Create propagation card
      propagationCardReserve.add(new PropagationCard(city));
      playerCardReserve.add(new CityCard(city));
    }

    deseaseCityCardMap = new HashMap<Desease, ArrayList<City>>();
    for (Desease desease : getDeseaseSet()) {
      ArrayList<City> cityCardSet = new ArrayList<City>();
      deseaseCityCardMap.put(desease, cityCardSet);
    }
    for (PlayerCard playerCard : playerCardReserve) {
      if (playerCard instanceof CityCard) {
        deseaseCityCardMap.get(((CityCard) playerCard).getCity().getDesease())
            .add((((CityCard) playerCard)).getCity());
      }
    }
    epidemicCardReserve = new EpidemicCard();

    // Updating map
    // Init distances
    for (City city : getMap().values()) {
      city.initDistances(getMap());
    }
    // Init city actions
    for (City city : getMap().values()) {
      city.initGameActions(getMap(), getDeseaseSet(), getCharacterList(4));
    }

  }

  public static Reserve getInstance() {
    if (instance == null) {
      instance = new Reserve();
    }
    return instance;
  }

  public ArrayList<Desease> getDeseaseSet() {
    return deseaseSet;
  }

  public Map<String, City> getMap() {
    return map;
  }

  public LinkedList<PlayedCharacter> getCharacterReserve() {
    return characterReserve;
  }

  public ArrayList<PlayedCharacter> getCharacterList(int numberOfPlayers) {
    if (characterReserve.size() < numberOfPlayers) {
      int toAdd = numberOfPlayers - characterReserve.size();
      for (int i = 0; i < toAdd; i++) {
        characterReserve.add(new PlayedCharacter("Player " + (i + 1)));
      }
    }
    return new ArrayList<PlayedCharacter>(characterReserve.subList(0, numberOfPlayers));
  }

  public EpidemicCard getEpidemicCardReserve() {
    return this.epidemicCardReserve;
  }

  public Set<PlayerCard> getPlayerCardReserve() {
    return this.playerCardReserve;
  }
}
