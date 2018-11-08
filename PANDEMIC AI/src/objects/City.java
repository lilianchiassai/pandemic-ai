package objects;

public class City {
	private String name;
	private int population;
	private Desease desease;
	private ResearchCenter researchCenter;
	
	public City(String name, Desease desease, int population) {
		this.name = name;
		this.desease = desease;
		this.population = population;
	}
	
	public City(String name, Desease desease, int population, ResearchCenter researchCenter) {
		this.name = name;
		this.desease = desease;
		this.population = population;
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
}
