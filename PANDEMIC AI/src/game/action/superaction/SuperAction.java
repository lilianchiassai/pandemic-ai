package game.action.superaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.GameStatus;
import game.LightGameStatus;
import game.action.GameAction;
import game.action.MultiDrive;
import objects.City;

public class SuperAction {
	MultiDrive multiDrive;
	StaticSuperAction staticSuperAction;
	Set<City> visit;
	int actionCost;
	
	public SuperAction(MultiDrive multiDrive, StaticSuperAction staticSuperAction) {
		this.multiDrive = multiDrive;
		this.staticSuperAction = staticSuperAction;
		actionCost = multiDrive != null ? multiDrive.getCost() : 0;
		if(staticSuperAction != null) {
			for(GameAction gameAction : staticSuperAction.gameActionList) {
				actionCost += gameAction.getCost();
			}
		}
	}
	
	public String toString() {
		if(multiDrive != null && staticSuperAction == null) {
			return multiDrive.toString();
		}
		if(multiDrive == null && staticSuperAction != null) {
			return staticSuperAction.toString();
		}
		
		return multiDrive.toString() +" - "+staticSuperAction.toString();
	}

	public boolean perform(GameStatus gameStatus) {
		if(multiDrive != null && !multiDrive.perform(gameStatus)) {
			return false;
		}
		if(staticSuperAction != null) {
			for(GameAction gameAction : staticSuperAction.gameActionList) {
				if(!gameAction.perform(gameStatus)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void perform(LightGameStatus lightGameStatus) {
		if(multiDrive != null) {
			multiDrive.perform(lightGameStatus);
		}
		if(staticSuperAction != null) {
			for(GameAction gameAction : staticSuperAction.gameActionList) {
				gameAction.perform(lightGameStatus);
			}
		}
	}
	
	public City getOrigin() {
		return multiDrive.getOrigin();
	}
	
	public City getDestination() {
		return multiDrive.getDestination();
	}
	
	public int getCost() {
		return this.actionCost;
	}
	
	public static List<List<SuperAction>> getAllPossibleSuperActionLists(GameStatus gameStatus) {
		return getAllPossibleSuperActionLists(gameStatus.lightClone());
	}
	
	public static List<List<SuperAction>> getAllPossibleSuperActionLists(LightGameStatus lightGameStatus) {
		List<List<SuperAction>> result = new ArrayList<List<SuperAction>>();
		Set<StaticSuperAction> startingStaticGameActionListSet = StaticSuperAction.getAllStaticSuperAction(lightGameStatus, new HashSet<City>());
		HashSet<City> visited = new HashSet<City>();
		visited.add(lightGameStatus.position);
		for(StaticSuperAction staticSuperAction : startingStaticGameActionListSet) {
			SuperAction superAction = new SuperAction(null, staticSuperAction);
			for(List<SuperAction> superActionList : getAllPossibleMovingSuperActionLists(lightGameStatus, visited)) {
				superActionList.add(0, superAction);
				result.add(superActionList);
			}
		}
		HashSet<City> visited2 = new HashSet<City>();
		visited2.add(lightGameStatus.position);
		result.addAll(getAllPossibleMovingSuperActionLists(lightGameStatus, visited2));
		
		//Drive only
		if(result.size() == 0) {
			for(int i = 1; i<=lightGameStatus.actionCount; i++) {
				for(City destination : lightGameStatus.position.getCitiesAtDistance(i)) {
					List<SuperAction> superActionList = new ArrayList<SuperAction>();
					superActionList.add(new SuperAction(lightGameStatus.position.getMultiDrive(destination), null));
					result.add(superActionList);
				}
			}
		}
		return result;
	}

	public static Set<List<SuperAction>> getAllPossibleMovingSuperActionLists(LightGameStatus lightGameStatus, Set<City> visited) {
		Set<List<SuperAction>> superActionListSet = new HashSet<List<SuperAction>>();
		
		//Get all actions series on origin position
		Set<SuperAction> superActionSet = getAllSuperGameActionsFromPosition(lightGameStatus, visited);
		for(SuperAction superAction : superActionSet) {
			visited.add(superAction.getDestination());
		}
		for(SuperAction superAction : superActionSet) {
			LightGameStatus lightClone = lightGameStatus.lightClone();
			superAction.perform(lightClone);
			Set<List<SuperAction>> movingSuperActionListSet = getAllPossibleMovingSuperActionLists(lightGameStatus,visited);
			if(movingSuperActionListSet.size()>0) {
				for(List<SuperAction> superActionList : movingSuperActionListSet) {
					superActionList.add(0, superAction);
					superActionListSet.add(superActionList);
				}
			} else {
				List<SuperAction> superActionList = new ArrayList<SuperAction>();
				superActionList.add(0, superAction);
				superActionListSet.add(superActionList);
			}
		}
		
		return superActionListSet;
	}
	


	public static Set<SuperAction> getAllSuperGameActionsFromPosition(LightGameStatus lightGameStatus, Set<City> visited) {
		HashSet<SuperAction> superActionSet = new HashSet<SuperAction>();
		
		for(int i = 1; i<lightGameStatus.actionCount; i++) {
			Set<City> destinationSet = lightGameStatus.position.getCitiesAtDistance(i);
			for(City destination : destinationSet) {
				if(!visited.contains(destination)) {
					LightGameStatus lightClone = lightGameStatus.lightClone();
					MultiDrive multiDrive = lightClone.position.getMultiDrive(destination);
					multiDrive.perform(lightClone);
					for(StaticSuperAction staticSuperAction : StaticSuperAction.getAllStaticSuperAction(lightClone, visited)) {
						superActionSet.add(new SuperAction(multiDrive, staticSuperAction));
					}
				}
			}
		}		
		return superActionSet;
	}
	
	
	

	
	
}
