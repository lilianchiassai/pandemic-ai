package objects;

import java.io.Serializable;

public class Cube implements Serializable {
	private Desease desease;
	private City city;
	
	public Cube(Desease desease) {
		this.desease = desease;
	}

	public Desease getDesease() {
		return desease;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

}
