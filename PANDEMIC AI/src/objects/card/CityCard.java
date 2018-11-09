package objects.card;

import objects.City;
import objects.Desease;

public class CityCard extends PlayerCard{
	private City city;
	
	public CityCard(City city) {
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
