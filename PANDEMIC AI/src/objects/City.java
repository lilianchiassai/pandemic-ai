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
import game.action.Drive;
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

	public Map<City, Integer> cityToDistanceMap;
	public ArrayList<Set<City>> distanceToCityMap;
	
	private Drive driveAction;
	private Map<City, MultiDrive> multiDriveActionMap;
	private Set<Drive> driveActionSet;
	private Map<City, CharterFlight> charterFlightActionMap;
	private Map<City, DirectFlight> directFlightActionMap;
	private Map<City, ShuttleFlight> shuttleFlightActionMap;

	
	
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
		
		this.driveActionSet = new HashSet<Drive>();
		this.charterFlightActionMap = new HashMap<City,CharterFlight>();
		this.directFlightActionMap = new HashMap<City,DirectFlight>();
		this.shuttleFlightActionMap = new HashMap<City,ShuttleFlight>();
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
	
	public Drive getDriveAction() {
		return this.driveAction;
	}
	
	public Set<Drive> getDriveActionSet() {
		return this.driveActionSet;
	}
	
	public Collection<CharterFlight> getCharterFlightActionSet() {
		return this.charterFlightActionMap.values();
	}
	
	public GameAction getCharterFlightAction(City to) {
		return this.charterFlightActionMap.get(to);
	}
	
	public DirectFlight getDirectFlightAction(City to) {
		return this.directFlightActionMap.get(to);
	}
	
	public ShuttleFlight getShuttleFlightAction(City to) {
		return this.shuttleFlightActionMap.get(to);
	}
	
	public void initGameActions(Set<City> map) {
		for(City city : map) {
			if(this != city) {
				this.charterFlightActionMap.put(city, new CharterFlight(this, city));
				this.directFlightActionMap.put(city, new DirectFlight(this, city));
				this.shuttleFlightActionMap.put(city,new ShuttleFlight(this, city));
			}
		}
		
	}
	
	public void addNeighbour(City city) {
		this.neighbourSet.add(city);
		this.driveActionSet.add(new Drive(this, city));
		this.charterFlightActionMap.remove(city);
		this.directFlightActionMap.remove(city);
		this.shuttleFlightActionMap.remove(city);
	}
	
	public void initDistances(Set<City> map) {
		int distance = 1;
		HashMap<City, Integer> cache = new HashMap<City, Integer>();
		
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
		for(City destination : map) {
			this.multiDriveActionMap.put(destination, new MultiDrive(this, destination));
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

}
