package objects;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.GameUtil;

public class City {
	private String name;
	private int population;
	private Desease desease;
	private ResearchCenter researchCenter;
	private Set<Cube> cubeSet;
	
	public City(String name, Desease desease, int population) {
		this.name = name;
		this.desease = desease;
		this.population = population;
		this.cubeSet = new HashSet<Cube>();
	}
	
	public City(String name, Desease desease, int population, ResearchCenter researchCenter) {
		this.name = name;
		this.desease = desease;
		this.population = population;
		this.cubeSet = new HashSet<Cube>();
		this.setResearchCenter(researchCenter);
	}

	public String getName() {
		return name;
	}

	public Desease getDesease() {
		return desease;
	}

	public ResearchCenter getResearchCenter() {
		return researchCenter;
	}

	public void setResearchCenter(ResearchCenter researchCenter) {
		this.researchCenter = researchCenter;
	}

	public int getPopulation() {
		return population;
	}
	
	public Set<Cube> getCubeSet(Desease desease) {
		return ((Stream<Cube>) cubeSet.stream().filter(GameUtil.getCubePredicate(desease))).collect(Collectors.toSet());
	}

	public void addCube(Cube cube) {
		cubeSet.add(cube);
	}

	public boolean hasResearchCenter() {
		return this.researchCenter != null;
	}

	public void removeCube(Cube cube) {
		cubeSet.remove(cube);
	}
}
