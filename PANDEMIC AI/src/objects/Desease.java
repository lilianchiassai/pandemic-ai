package objects;

public class Desease {
	private boolean eradicated;
	private boolean remedyFound;
	private String name;
	
	public Desease(String name) {
		this.eradicated = false;
		this.remedyFound = false;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isRemedyFound() {
		return remedyFound;
	}

	public void findRemedy() {
		this.remedyFound = true;
	}

	public boolean isEradicated() {
		return eradicated;
	}

	public void eradicate() {
		this.eradicated = true;
	}
}
