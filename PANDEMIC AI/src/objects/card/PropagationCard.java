package objects.card;

import java.io.Serializable;

import objects.City;

public class PropagationCard implements Card,Serializable {
	private City city;
	
	public PropagationCard(City city) {
		this.city = city;
	}
	
	public City getCity() {
		return this.city;
	}

	@Override
	public String getTitle() {
		
		return city.getName();
	}
}
