package objects;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.action.Build;
import game.action.CharterFlight;
import game.action.DirectFlight;
import game.action.Discard;
import game.action.Drive;
import game.action.ShareKnowledge;
import game.action.ShuttleFlight;
import util.GameUtil;

public class City implements Serializable{
	private String name;
	private int population;
	private Desease desease;
	private Set<City> neighbourSet;

	private Drive driveAction;
	private Set<Drive> driveActionSet;
	private CharterFlight charterFlightAction;
	private DirectFlight directFlightAction;
	private ShuttleFlight shuttleFlightAction;

	
	
	public City(String name, Desease desease, int population) {
		this.name = name;
		this.desease = desease;
		this.population = population;
		this.neighbourSet = new HashSet<City>();
		
		this.driveAction = new Drive(this);
		this.driveActionSet = new HashSet<Drive>();
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
	
	public Drive getDriveAction() {
		return this.driveAction;
	}
	
	public Set<Drive> getDriveActionSet() {
		return this.driveActionSet;
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
	
	public void addNeighbour(City city) {
		this.neighbourSet.add(city);
		this.driveActionSet.add(new Drive(city));
	}
	
	public Set<City> getNeighbourSet() {
		return this.neighbourSet;
	}
}
