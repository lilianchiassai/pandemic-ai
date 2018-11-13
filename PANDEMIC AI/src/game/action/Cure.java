package game.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import gameStatus.Game;
import objects.Desease;
import objects.card.Card;
import objects.card.CityCard;
import util.GameUtil;

public class Cure extends GameAction {
	Desease desease;
	Set<Card> set;
	
	public Cure(Game game, Desease desease, Set<Card> set) {
		super(game);
		this.desease=desease;
		this.set=set;
	}
	
	public boolean perform() {
		if(!desease.isCured() && game.getCurrentPlayer().getPosition().hasResearchCenter()) {
			Set<Card> cardSetDesease = set.stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(desease)).collect(Collectors.toSet());
			if(cardSetDesease != null && cardSetDesease.size()==5 && super.perform()) {
				game.getCurrentPlayer().getHand().discard(game, cardSetDesease);
				desease.findCure();
				return true;
			}
		}
		return false;
	}

	public boolean isValid() {
		if(!desease.isCured() && game.getCurrentPlayer().getPosition().hasResearchCenter()) {
			Set<Card> cardSetDesease = set.stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(desease)).collect(Collectors.toSet());
			return cardSetDesease != null && cardSetDesease.size()>=5;
		}
		return false;
	}

	public static Set<Cure> getValidGameActionSet(Game game) {
		Set<Cure> cureSet = new HashSet<Cure>();
		for(Desease desease : game.getDeseaseSet()) {
			Set<Card> cardSetDesease = game.getCurrentPlayer().getHand().getCardDeck().stream().filter((Predicate<? super Card>) GameUtil.getCityCardPredicate(desease)).collect(Collectors.toSet());
			if(cardSetDesease != null && cardSetDesease.size() >= 5) {
				for(Set<Card> cardSet : getCombinations(cardSetDesease,5)) {
					cureSet.add(new Cure(game, desease, cardSet));
				}
			}
		}
		return cureSet;
	}
	
	private static Set<Set> getCombinations(Set set, int combinationSize) {
		if(combinationSize == 1) {
			Set<Set> resultSet = new HashSet<Set>();
			for(Object o : set) {
				Set subResultSet = new HashSet<>();
				subResultSet.add(o);
				resultSet.add(subResultSet);
			}
			return resultSet;
		} else {
			Set<Set> result =  new HashSet<Set>();
			Set subset = new HashSet<>(set);
			for(Object o : set) {
				subset.remove(o);
				Set<Set> combinationSet = getCombinations(subset, combinationSize - 1);
				for(Set resultSet : combinationSet) {
					resultSet.add(o);
				}
				result.add(combinationSet);
			}
			return result;
		}
	}
}