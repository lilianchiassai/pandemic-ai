package pandemic.material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pandemic.State;
import pandemic.action.Build;
import pandemic.action.Cancel;
import pandemic.action.CharterFlight;
import pandemic.action.Cure;
import pandemic.action.DirectFlight;
import pandemic.action.Drive;
import pandemic.action.GameAction;
import pandemic.action.GiveKnowledge;
import pandemic.action.MoveAction;
import pandemic.action.Pass;
import pandemic.action.ReceiveKnowledge;
import pandemic.action.ShareKnowledge;
import pandemic.action.ShuttleFlight;
import pandemic.action.StaticAction;
import pandemic.action.Treat;
import pandemic.material.card.CityCard;

public class City implements Comparable<City> {
	public static int index = 0;
	public int id;
	private String name;
	private int population;
	private Desease desease;
	private CityCard cityCard;
	private Set<City> neighbourSet;

	private Map<City, Integer> cityToDistanceMap;
	private ArrayList<Set<City>> distanceToCityMap;

	// Move Actions
	private HashMap<City, Drive> multiDriveActionMap;
	private ArrayList<Set<Drive>> distanceToMultiDriveMap;
	private Map<City, ShuttleFlight> shuttleFlightActionSet;
	private Map<City, CharterFlight> charterFlightActionSet;
	private Map<City, DirectFlight> directFlightActionSet;

	// Static Actions
	private Pass[] passActions;
	private Build buildAction;
	private Map<Desease, List<Treat>> treatActionMap;
	private Map<Desease, List<Treat>> treatCuredActionMap;
	private Map<Desease, Set<Cure>> cureActionMap;
	private Map<PlayedCharacter, ShareKnowledge> shareKnowledgeActionSet;

	public Set<GameAction> allActions;
	public Set<MoveAction> allMoveActions;
	public Set<StaticAction> allStaticActions;
	private GameAction cancelAction;

	public City(String name, Desease desease) {
		this.id = index;
		index++;
		this.name = name;
		this.desease = desease;
		this.neighbourSet = new HashSet<City>();

		this.cityToDistanceMap = new HashMap<City, Integer>();
		this.cityToDistanceMap.put(this, 0);
		this.distanceToCityMap = new ArrayList<Set<City>>();
		this.distanceToCityMap.add(new HashSet<City>());
		this.distanceToCityMap.get(0).add(this);

		this.multiDriveActionMap = new HashMap<City, Drive>();
		this.distanceToMultiDriveMap = new ArrayList<Set<Drive>>();
		this.charterFlightActionSet = new HashMap<City, CharterFlight>();
		this.directFlightActionSet = new HashMap<City, DirectFlight>();
		this.shuttleFlightActionSet = new HashMap<City, ShuttleFlight>();

		this.passActions = new Pass[4];
		this.buildAction = new Build(this);
		this.treatActionMap = new HashMap<Desease, List<Treat>>();
		this.treatCuredActionMap = new HashMap<Desease, List<Treat>>();
		this.cureActionMap = new HashMap<Desease, Set<Cure>>();
		this.shareKnowledgeActionSet = new HashMap<PlayedCharacter, ShareKnowledge>();

		this.allActions = new HashSet<GameAction>();
		this.allMoveActions = new HashSet<MoveAction>();
		this.allStaticActions = new HashSet<StaticAction>();
	}

	public String getName() {
		return name;
	}

	public Desease getDesease() {
		return desease;
	}

	public int getPopulation() {
		return population;
	}

	public Collection<Drive> getMultiDriveActionSet(int distance) {
		return this.distanceToMultiDriveMap.get(distance);
	}

	public void initGameActions(Map<String, City> map, ArrayList<Desease> arrayList, List<PlayedCharacter> characterReserve) {
		this.cancelAction= new Cancel(this);
		
		for (int i = 0; i < this.passActions.length; i++) {
			this.passActions[i] = new Pass(this, i + 1);
		}

		for (City city : map.values()) {
			if (this != city && !this.neighbourSet.contains(city)) {
				this.charterFlightActionSet.put(city, new CharterFlight(this, city));
				this.directFlightActionSet.put(city, new DirectFlight(this, city));
				this.shuttleFlightActionSet.put(city, new ShuttleFlight(this, city));
			}
		}

		for (Desease localDesease : arrayList) {
			treatActionMap.put(localDesease, new ArrayList<Treat>());
			treatActionMap.get(localDesease).add(new Treat(this, localDesease));
			treatActionMap.get(localDesease).add(new Treat(this, localDesease, 2));
			treatActionMap.get(localDesease).add(new Treat(this, localDesease, 3));
			treatCuredActionMap.put(localDesease, new ArrayList<Treat>());
			treatCuredActionMap.get(localDesease).add(new Treat(this, localDesease, 2, 1));
			treatCuredActionMap.get(localDesease).add(new Treat(this, localDesease, 3, 1));

			// Set<Set<CityCard>> combinationSet =
			// Cure.getCombinations(GameProperties.deseaseCityCardMap.get(desease),5);
			cureActionMap.put(localDesease, new HashSet<Cure>());
			/*
			 * for(Set<CityCard> cardSet : combinationSet) {
			 * cureActionMap.get(localDesease).add(new Cure(this, localDesease, cardSet)); }
			 */
		}

		for (PlayedCharacter playedCharacter : characterReserve) {
			this.shareKnowledgeActionSet.put(playedCharacter, new GiveKnowledge(this, playedCharacter));
			this.shareKnowledgeActionSet.put(playedCharacter, new ReceiveKnowledge(this, playedCharacter));
		}

		// Regroup actions in common sets
		this.allMoveActions.addAll(this.directFlightActionSet.values());
		this.allMoveActions.addAll(this.multiDriveActionMap.values());
		this.allMoveActions.addAll(this.shuttleFlightActionSet.values());
		this.allMoveActions.addAll(this.charterFlightActionSet.values());

		this.allStaticActions.add(this.buildAction);
		this.allStaticActions.addAll(this.shareKnowledgeActionSet.values());
		for (Desease localDesease : cureActionMap.keySet()) {
			this.allStaticActions.addAll(cureActionMap.get(localDesease));
			this.allStaticActions.addAll(this.treatActionMap.get(localDesease));
			this.allStaticActions.addAll(this.treatCuredActionMap.get(localDesease));
		}

		this.allActions.addAll(this.allMoveActions);
		this.allActions.addAll(this.allStaticActions);

	}

	public void addNeighbour(City city) {
		this.neighbourSet.add(city);
	}

	public void initDistances(Map<String, City> map) {
		HashMap<City, Integer> cache = new HashMap<City, Integer>();
		while (this.cityToDistanceMap.size() < map.size()) {
			for (City neighbour : this.cityToDistanceMap.keySet()) {
				for (City city : neighbour.getNeighbourSet()) {
					if (!this.cityToDistanceMap.keySet().contains(city)) {
						cache.put(city, this.cityToDistanceMap.get(neighbour) + 1);
					}
				}
			}

			this.cityToDistanceMap.putAll(cache);
			this.distanceToCityMap.add(new HashSet<City>(cache.keySet()));
			cache.clear();
		}

		// Init multidrive actions
		for (int i = 1; i < 5; i++) {
			HashSet<Drive> multiDriveSet = new HashSet<Drive>();
			for (City destination : this.getCitiesAtDistance(i)) {
				Drive multiDrive = new Drive(this, destination);
				this.multiDriveActionMap.put(destination, multiDrive);
				multiDriveSet.add(multiDrive);
			}
			this.distanceToMultiDriveMap.add(multiDriveSet);
		}
	}

	public Set<City> getNeighbourSet() {
		return this.neighbourSet;
	}

	public CityCard getCityCard() {
		return this.cityCard;
	}

	public void setCityCard(CityCard cityCard2) {
		this.cityCard = cityCard2;
	}

	public int getDistance(City city) {
		return this.cityToDistanceMap.get(city);
	}

	public Set<City> getCitiesAtDistance(int distance) {
		return this.distanceToCityMap.get(distance);
	}

	public String toString() {
		return this.name;
	}

	public Drive getMultiDrive(City destination) {
		return this.multiDriveActionMap.get(destination);
	}

	public Collection<CharterFlight> getCharterFlightActionSet() {
		return this.charterFlightActionSet.values();
	}

	public Collection<DirectFlight> getDirectFlightActionSet() {
		return this.directFlightActionSet.values();
	}

	public Collection<ShuttleFlight> getShuttleFlightActionSet() {
		return this.shuttleFlightActionSet.values();
	}

	public int getId() {
		return this.id;
	}

	public GameAction getDirectFlight(City city) {
		return this.directFlightActionSet.get(city);
	}

	public GameAction getCharterFlight(City city) {
		return this.charterFlightActionSet.get(city);
	}

	public GameAction getShuttleFlight(City city) {
		return this.shuttleFlightActionSet.get(city);
	}

	public GameAction getTreat(int cubeCount, boolean healAll) {
		if (!healAll || cubeCount <= 2) {
			return this.treatActionMap.get(this.desease).get(0);
		} else {
			return this.treatActionMap.get(this.desease).get(cubeCount - 1);
		}
	}

	public GameAction getShareKnowledge(PlayedCharacter otherPlayer) {
		return this.shareKnowledgeActionSet.get(otherPlayer);
	}

	public GameAction getPass(State state) {
		return this.passActions[state.getCurrentActionCount() - 1];
	}

	@Override
	public int compareTo(City city) {
		return this.id - city.id;
	}

	public GameAction getBuildAction() {
		return this.buildAction;
	}

	public GameAction getCancel(State state) {
		return this.cancelAction;
	}
}
