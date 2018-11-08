package objects;

public class ResearchCenter {
	private City city;
	
	public ResearchCenter() {
		
	}
	
	public void build(City city) {
		this.city = city;
		city.setResearchCenter(this);
	}
}
