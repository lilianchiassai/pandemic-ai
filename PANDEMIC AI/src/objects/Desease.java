package objects;

import java.io.Serializable;

public class Desease implements Serializable {
	private boolean eradicated;
	private boolean cured;
	private String name;
	
	public Desease(String name) {
		this.eradicated = false;
		this.cured = false;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isCured() {
		return cured;
	}

	public void findCure() {
		this.cured = true;
	}

	public boolean isEradicated() {
		return eradicated;
	}

	public void eradicate() {
		this.eradicated = true;
	}
}
