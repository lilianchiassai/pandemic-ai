package objects;

public class Cube {
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
