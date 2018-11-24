package objects;

import java.io.Serializable;

public class Desease implements Serializable {
	private String name;
	
	public Desease(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
