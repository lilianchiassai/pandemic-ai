package game.action;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import game.GameProperties;
import game.GameRules;
import game.GameStatus;
import game.LightGameStatus;
import objects.Desease;
import objects.card.Card;
import objects.card.CityCard;
import objects.card.PlayerCard;
import util.GameUtil;

public class Cure extends GameAction {
	Desease desease;
	private Set<Card> set;
	
	public Cure(Desease desease, Set<Card> set) {
		super();
		this.desease=desease;
		this.setSet(set);
	}
	
	public boolean perform(GameStatus gameStatus) {
		if(!GameRules.isCured(gameStatus, desease) && gameStatus.hasResearchCenter(gameStatus.getCurrentCharacterPosition())) {
			Set<Card> cardSetDesease = getSet().stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(desease)).collect(Collectors.toSet());
			if(cardSetDesease != null && cardSetDesease.size()==5 && super.perform(gameStatus)) {
				gameStatus.getCurrentHand().removeAndDiscard(gameStatus, cardSetDesease);
				GameUtil.log(gameStatus, GameAction.logger, gameStatus.getCurrentPlayer().getName()+" finds a Cure in "+gameStatus.getCurrentCharacterPosition().getName()+" for the "+desease.getName()+" desease.");
				return GameRules.findCure(gameStatus, desease);
			}
		}
		return false;
	}
	
	public void perform(LightGameStatus lightGameStatus) {
		lightGameStatus.hand.removeAll(this.set);
		lightGameStatus.curedDeseaseSet.add(this.desease);
		lightGameStatus.actionCount-=this.actionCost;
	}
	
	public static Set<Cure> getValidGameActionSet(GameStatus gameStatus) {
		Set<Cure> cureSet = new HashSet<Cure>();
		if(gameStatus.hasResearchCenter(gameStatus.getCurrentCharacterPosition())) {
			for(Desease desease : GameProperties.deseaseSet) {
				if(!GameRules.isCured(gameStatus, desease)) {
					Set<Card> cardSetDesease = gameStatus.getCurrentHand().getCardDeck().stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(desease)).collect(Collectors.toSet());
					if(cardSetDesease != null && cardSetDesease.size() >= 5) {
						Set<Set<Card>> combinationSet = getCombinations(cardSetDesease,5);
						for(Set<Card> cardSet : combinationSet) {
							cureSet.add(new Cure(desease, cardSet));
						}
					}
				}
			}
		}
		return cureSet;
	}
	
	public static Set<Cure> getDefaultGameActionSet() {
		Set<Cure> cureSet = new HashSet<Cure>();
		for(Desease desease : GameProperties.deseaseSet) {
			cureSet.addAll(GameProperties.cureActionMap.get(desease));
		}
		return cureSet;
	}
	
	
	public static Set<Set<Card>> getCombinations(Set set, int combinationSize) {
		if(combinationSize == 1) {
			Set<Set<Card>> resultSet = new HashSet<Set<Card>>();
			for(Object o : set) {
				Set subResultSet = new HashSet<>();
				subResultSet.add(o);
				resultSet.add(subResultSet);
			}
			return resultSet;
		} else {
			Set<Set<Card>> result =  new HashSet<Set<Card>>();
			Set subset = new HashSet<>(set);
			for(Object o : set) {
				subset.remove(o);
				Set<Set<Card>> combinationSet = getCombinations(subset, combinationSize - 1);
				for(Set resultSet : combinationSet) {
					resultSet.add(o);
					result.add(resultSet);
				}
			}
			return result;
		}
	}

	public Set<Card> getSet() {
		return set;
	}

	public void setSet(Set<Card> set) {
		this.set = set;
	}
	
	public Desease getDesease() {
		return this.desease;
	}
}