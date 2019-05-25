package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import game.action.CharterFlight;
import game.action.DirectFlight;
import game.action.GameAction;
import game.action.MultiDrive;
import game.action.ShuttleFlight;
import objects.card.CityCard;

public class City implements Serializable{
	private String name;
	private int population;
	private Desease desease;
	private CityCard cityCard;
	private Set<City> neighbourSet;

	private Map<City, Integer> cityToDistanceMap;
	private ArrayList<Set<City>> distanceToCityMap;
	
	private HashMap<City, MultiDrive> multiDriveActionMap;
	private ArrayList<Set<MultiDrive>> distanceToMultiDriveMap;
	private Set<ShuttleFlight> shuttleFlightActionSet;
	private Set<CharterFlight> charterFlightActionSet;
	private Set<DirectFlight> directFlightActionSet;
	
	/*Action to enter the city*/
	private CharterFlight charterFlightAction;
	private DirectFlight directFlightAction;
	private ShuttleFlight shuttleFlightAction;

	
	
	public City(String name, Desease desease, int population) {
		this.name = name;
		this.desease = desease;
		this.population = population;
		this.neighbourSet = new HashSet<City>();
		
		this.cityToDistanceMap = new HashMap<City, Integer>();
		this.cityToDistanceMap.put(this, 0);
		this.distanceToCityMap = new ArrayList<Set<City>>();
		this.distanceToCityMap.add(new HashSet<City>());
		this.distanceToCityMap.get(0).add(this);
		
		this.multiDriveActionMap = new HashMap<City, MultiDrive>();
		this.distanceToMultiDriveMap = new ArrayList<Set<MultiDrive>>();
		this.charterFlightActionSet = new HashSet<CharterFlight>();
		this.directFlightActionSet = new HashSet<DirectFlight>();
		this.shuttleFlightActionSet = new HashSet<ShuttleFlight>();
		
		this.charterFlightAction = new CharterFlight(this);
		this.directFlightAction = new DirectFlight(this);
		this.shuttleFlightAction = new ShuttleFlight(this);
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
	
	public CharterFlight getCharterFlightAction() {
		return this.charterFlightAction;
	}
	
	public DirectFlight getDirectFlightAction() {
		return this.directFlightAction;
	}
	
	public ShuttleFlight getShuttleFlightAction() {
		return this.shuttleFlightAction;
	}
	
	public Collection<MultiDrive> getMultiDriveActionSet(int distance) {
		return this.distanceToMultiDriveMap.get(distance);
	}
	
	public void initGameActions(Set<City> map) {
		for(City city : map) {
			if(this != city) {
				this.charterFlightActionSet.add(city.getCharterFlightAction());
				this.directFlightActionSet.add(city.getDirectFlightAction());
				this.shuttleFlightActionSet.add(city.getShuttleFlightAction());
			}
		}
	}
	
	public void addNeighbour(City city) {
		this.neighbourSet.add(city);
		this.charterFlightActionSet.remove(city.getCharterFlightAction());
		this.directFlightActionSet.remove(city.getDirectFlightAction());
		this.shuttleFlightActionSet.remove(city.getShuttleFlightAction());
	}
	
	public void initDistances(Set<City> map) {
		HashMap<City, Integer> cache = new HashMap<City, Integer>();
		int distance = 1;
		while(this.cityToDistanceMap.size()<map.size()) {
			for(City neighbour : this.cityToDistanceMap.keySet()) {
				for(City city : neighbour.getNeighbourSet()) {
					if(!this.cityToDistanceMap.keySet().contains(city) ) {
						cache.put(city, this.cityToDistanceMap.get(neighbour)+1);
					}
				}		
			}
			
			this.cityToDistanceMap.putAll(cache);
			this.distanceToCityMap.add(new HashSet<City>(cache.keySet()));
			distance++;
			cache.clear();
		}
		
		
		//Init multidrive actions
		for(int i = 1; i<5; i++) {
			HashSet multiDriveSet = new HashSet<MultiDrive>();
			for(City destination : this.getCitiesAtDistance(i)) {
				MultiDrive multiDrive = new MultiDrive(this, destination);
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

	public MultiDrive getMultiDrive(City destination) {
		return this.multiDriveActionMap.get(destination);
	}

	public Set<CharterFlight> getCharterFlightActionSet() {
		return this.charterFlightActionSet;
	}
	
	public Set<DirectFlight> getDirectFlightActionSet() {
		return this.directFlightActionSet;
	}
	
	public Set<ShuttleFlight> getShuttleFlightActionSet() {
		return this.shuttleFlightActionSet;
	}

}
