package objects.card;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import game.GameProperties;
import game.action.Discard;
import game.action.GameAction;
import objects.Character;

public abstract class PlayerCard implements Card, Serializable{
	private Map<Character,Discard> discard;

	public PlayerCard() {
		this.discard = new HashMap<Character,Discard>();
		for(Character character : GameProperties.characterReserve) {
			this.discard.put(character, new Discard(character, this));
		}
	}
	
	
	public Discard getCharacterDiscard(Character character) {
		return this.discard.get(character);
	}
}
