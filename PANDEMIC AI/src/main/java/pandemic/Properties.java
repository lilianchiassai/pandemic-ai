package pandemic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.GameProperties;
import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.material.PlayedCharacter;

public final class Properties extends GameProperties{
	
	private static Logger logger = LogManager.getLogger(Properties.class.getName());

	public final int numberOfPlayers;
	public final int maxEclosionCounter;	
	public final int maxActionCount;
	public final int[] propagationSpeed;
	public final int maxResearchCenterCounter;
	public final int maxHandSize;
	public final int maxCubeCount;
	public final int difficulty;
	
	public final ArrayList<PlayedCharacter> characterList;
	public final ArrayList<Desease> deseaseList;
	public final Set<City> map;
	
	
	public Properties(int characterReserveSize, int difficulty) {
		logger.info("Instantiating new game.");
		
		
		numberOfPlayers = characterReserveSize;
		maxCubeCount=24;
		maxResearchCenterCounter=6;
		maxEclosionCounter = 7;
		maxHandSize=7;
		maxActionCount = 4;
		propagationSpeed = new int[]{2, 2, 2, 3, 3, 4, 4};
		this.difficulty=difficulty;
		deseaseList = new ArrayList<Desease>(Reserve.getInstance().getDeseaseSet().size());
		for(Desease desease : Reserve.getInstance().getDeseaseSet()) {
			deseaseList.add(desease.id,desease);
		}
		
		characterList = Reserve.getInstance().getCharacterList(numberOfPlayers);
		
		map = new HashSet<City> (Reserve.getInstance().getMap().values());
		
		
		logger.info("Game ready");
	}
	
	public int getPropagationSpeed(int epidemicCounter) {
		return propagationSpeed[epidemicCounter];
	}
	

}
