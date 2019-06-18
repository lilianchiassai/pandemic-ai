package pandemic.material.card;

public abstract class Card {
	public abstract String getTitle();
	public String toString() {
		return this.getTitle();
	}
	
}
