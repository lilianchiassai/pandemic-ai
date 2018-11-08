package objects.card;

import objects.City;

public class PropagationCard implements Card {
	private City city;
	
	public PropagationCard(City city) {
		this.city = city;
	}
	
	public City getCity() {
		return this.city;
	}
}
