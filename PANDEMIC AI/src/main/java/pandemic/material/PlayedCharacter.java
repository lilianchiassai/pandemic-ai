package pandemic.material;

import pandemic.State;

public class PlayedCharacter implements Comparable<PlayedCharacter> {

	private static int index = 0;
	public int id;
	String name;

	public PlayedCharacter(String name) {
		this.name = name;
		this.id = index;
		index++;
	}

	public City getPosition(State state) {
		return state.getCharacterPosition(this);
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int compareTo(PlayedCharacter character) {
		return this.id - character.id;
	}
}
