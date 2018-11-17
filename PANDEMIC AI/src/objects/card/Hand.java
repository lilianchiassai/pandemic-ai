package objects.card;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.Game;
import game.GameStatus;
import objects.City;
import util.GameUtil;

public class Hand extends Deck {
	
	public Hand() {
		super(PlayerCard.class);
	}
	
	public Set<Card> getCityCardSet() {
		return  ((Stream<Card>)cardDeck.stream().filter(GameUtil.getCityCardPredicate())).collect(Collectors.toSet());
	}
	
	public CityCard getCityCard(City city) {

		Set<Card> cityCardSet = (Set<Card>) cardDeck.stream().filter(GameUtil.getCityCardPredicate(city)).collect(Collectors.toSet());
		Iterator<Card> it = cityCardSet.iterator();
		if(it.hasNext()) {
			return (CityCard) it.next();
		}
		return null;
	}

	public void removeCard(CityCard card) {
		cardDeck.remove(card);
	}

	
	public void discard(GameStatus gameStatus, PlayerCard card) {
		gameStatus.getPlayerDeck().getDiscardPile().addOnTop(card);
		this.cardDeck.remove(card);
	}
	
	public void discard(GameStatus gameStatus, Set<Card> cardSetDesease) {
		gameStatus.getPlayerDeck().getDiscardPile().addOnTop(cardSetDesease);
		this.cardDeck.removeAll(cardSetDesease);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Card card : cardDeck) {
			sb.append(", ");
			sb.append(card.getTitle());
			
		}
		String result = sb.toString();
		return result != null & result.length()>2 ? result.substring(2) : "";
	}
	
}
