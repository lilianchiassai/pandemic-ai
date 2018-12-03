package objects.card;

import java.util.Set;

import game.action.Build;
import game.action.CharterFlight;
import game.action.DirectFlight;
import game.action.Discard;
import game.action.Drive;
import game.action.ShareKnowledge;
import game.action.ShuttleFlight;
import objects.City;

public class CityCard extends PlayerCard{
	private City city;

	
	
	public CityCard(City city) {
		this.city = city;
		city.setCityCard(this);
	}

	public City getCity() {
		return this.city;
	}

	@Override
	public String getTitle() {
		return city.getName();
	}
	
}
