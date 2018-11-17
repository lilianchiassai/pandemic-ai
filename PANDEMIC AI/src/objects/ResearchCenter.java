package objects;

import java.io.Serializable;

public class ResearchCenter implements Serializable {
	private City city;
	
	public ResearchCenter() {
		
	}
	
	public void build(City city) {
		this.city = city;
		city.setResearchCenter(this);
	}
}
