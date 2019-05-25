package game.action.superaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import game.GameProperties;
import game.GameRules;
import game.LightGameStatus;
import game.action.Build;
import game.action.Cure;
import game.action.GameAction;
import game.action.GiveKnowledge;
import game.action.ReceiveKnowledge;
import game.action.ShareKnowledge;
import game.action.Treat;
import objects.City;
import objects.Desease;
import objects.card.Card;
import objects.card.CityCard;
import objects.card.Hand;
import util.GameUtil;

public class StaticSuperAction {
	List<GameAction> gameActionList;
	int actionCost;
	Set<Card> addedCard;
	Set<Card> removedCard;
	Set<City> builtResearchCenter;
	Set<Desease> curedDesease;
	
	public StaticSuperAction() {
		gameActionList = new ArrayList<GameAction>();
		addedCard = new HashSet<Card>();
		removedCard = new HashSet<Card>();
		builtResearchCenter = new HashSet<City>();
		curedDesease = new HashSet<Desease>();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(gameActionList.size()==1) {
			return gameActionList.get(0).toString();
		}
		for(GameAction gameAction : gameActionList) {
			sb.append(gameAction.toString()).append(" & ");
		}
		return sb.delete(sb.length()-2, sb.length()-1).toString();
	}
	
	public static Set<StaticSuperAction> getAllStaticSuperAction(LightGameStatus lightGameStatus, Set<City> visited) {
		return getAllStaticSuperAction(lightGameStatus, true, true, true, true, visited);
	}
	
	public static Set<StaticSuperAction> getAllStaticSuperAction(LightGameStatus lightGameStatus, boolean share, boolean build, boolean cure, boolean treat, Set<City> visited) {
		Set<StaticSuperAction> actionListSet = new HashSet<StaticSuperAction>();
		
		// retoune un set de listes d'actions sans bouger de la position indique pour le nombre d'actions indiques.
		if(lightGameStatus.actionCount == 0) {
			return actionListSet;
		}
		//Share
		if(share) {
			StaticSuperAction shareStatic = new StaticSuperAction();
			for(Hand hand : lightGameStatus.gameStatus.getCharacterHandList()) {
				if(hand != lightGameStatus.gameStatus.getCurrentHand() && lightGameStatus.gameStatus.getCharacterPosition(hand.getCharacter()) == lightGameStatus.position) {
					if(lightGameStatus.hand.contains(lightGameStatus.position.getCityCard())) {
						shareStatic.gameActionList.add(hand.getGiveKnowledgeAction());
						actionListSet.add(shareStatic);
						LightGameStatus lightClone = lightGameStatus.lightClone();
						hand.getGiveKnowledgeAction().perform(lightClone);
						for(StaticSuperAction staticActionList : getAllStaticSuperAction(lightClone, false, false, cure, treat, visited)) {
							staticActionList.gameActionList.add(0, hand.getGiveKnowledgeAction());
							actionListSet.add(staticActionList);
						}
						
					} else if(hand.getCardDeck().contains(lightGameStatus.position.getCityCard())) {
						shareStatic.gameActionList.add(hand.getReceiveKnowledgeAction());
						actionListSet.add(shareStatic);
						LightGameStatus lightClone = lightGameStatus.lightClone();
						hand.getReceiveKnowledgeAction().perform(lightClone);
						for(StaticSuperAction staticActionList : getAllStaticSuperAction(lightClone, false, true, cure, treat, visited)) {
							staticActionList.gameActionList.add(0, hand.getReceiveKnowledgeAction());
							actionListSet.add(staticActionList);
						}
					}
				}
			}
		}
		
		//Build
		if(build) {
			StaticSuperAction buildStatic = new StaticSuperAction();
			if(lightGameStatus.hand.contains(lightGameStatus.position.getCityCard()) && lightGameStatus.researchCenterCity.size()<GameProperties.researchCenterReserve.size()) {
				buildStatic.gameActionList.add(GameProperties.buildAction);
				actionListSet.add(buildStatic);
				LightGameStatus lightClone = lightGameStatus.lightClone();
				GameProperties.buildAction.perform(lightClone);
				for(StaticSuperAction staticActionList : getAllStaticSuperAction(lightClone, false, false,cure,treat, visited)) {
					staticActionList.gameActionList.add(0, GameProperties.buildAction);
					actionListSet.add(staticActionList);
				}
				
			}
		}
		
		
		//Cure
		if(cure) {
			StaticSuperAction cureStatic = new StaticSuperAction();
			if(lightGameStatus.researchCenterCity.contains(lightGameStatus.position)) {
				for(Desease desease : GameProperties.deseaseSet) {
					if(!lightGameStatus.curedDeseaseSet.contains(desease)) {
						Set<Card> cardSetDesease = lightGameStatus.hand.stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(desease)).collect(Collectors.toSet());
						if(cardSetDesease != null && cardSetDesease.size() >= 5) {
							Set<Set<Card>> combinationSet = Cure.getCombinations(cardSetDesease,5);
							for(Set<Card> cardSet : combinationSet) {
								Cure newCure = new Cure(desease, cardSet);
								cureStatic.gameActionList.add(newCure);
								actionListSet.add(cureStatic);
								LightGameStatus lightClone = lightGameStatus.lightClone();
								newCure.perform(lightClone);
								for(StaticSuperAction staticActionList : getAllStaticSuperAction(lightClone, share, build, false,treat, visited)) {
									staticActionList.gameActionList.add(0, newCure);
									actionListSet.add(staticActionList);
								}
								
							}
						}
					}
				}
			}
		}
		
		
		//Treat
		if(treat) {
			if(!visited.contains(lightGameStatus.position)) {
				for(Desease desease : GameProperties.deseaseSet) {
					int cityCubeSetSize = lightGameStatus.gameStatus.getCityCubeSet(lightGameStatus.position, desease).size();
					if(cityCubeSetSize>0) {
						if(lightGameStatus.curedDeseaseSet.contains(desease)) {
							StaticSuperAction treatStatic = new StaticSuperAction();
							treatStatic.gameActionList.add(GameProperties.treatCuredAction.get(desease).get(cityCubeSetSize-1));
							actionListSet.add(treatStatic);
						} else {
							for(int i = 0; i<cityCubeSetSize && i<lightGameStatus.actionCount; i++) {
								 {
									StaticSuperAction treatStatic = new StaticSuperAction();
									treatStatic.gameActionList.add(GameProperties.treatAction.get(desease).get(i));
									actionListSet.add(treatStatic);
								}
							}
						}
					}			
				}
			}
		}
		
		return actionListSet;
	}
}