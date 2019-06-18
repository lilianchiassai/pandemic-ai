package pandemic.material.card;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import pandemic.State;
import pandemic.material.City;
import pandemic.material.PlayedCharacter;
import pandemic.util.GameUtil;

public class Hand extends AbstractCollection<CityCard> implements Collection<CityCard>{
	
	PlayedCharacter playedCharacter;
	TreeSet<CityCard>[] cityCardHand;
	int size;
	
	public Hand(PlayedCharacter playedCharacter) {
		this.playedCharacter = playedCharacter;
		this.cityCardHand = new TreeSet[4];
		for(int i=0; i<this.cityCardHand.length; i++) {
			this.cityCardHand[i]= new TreeSet<CityCard>();
		}

	}
	
	public PlayedCharacter getCharacter() {
		return this.playedCharacter;
	}

	public Set<Card> getCityCardSet() {
		return  this.stream().filter(GameUtil.getCityCardPredicate()).collect(Collectors.toSet());
	}
	
	public CityCard getCityCard(City city) {

		Set<Card> cityCardSet = this.cityCardHand[city.getDesease().id].stream().filter(GameUtil.getCityCardPredicate(city)).collect(Collectors.toSet());
		Iterator<Card> it = cityCardSet.iterator();
		if(it.hasNext()) {
			return (CityCard) it.next();
		}
		return null;
	}
	
	public boolean removeAndDiscard(State state, PlayerCard card) {
		state.getPlayerDeck().getDiscardPile().addOnTop(card);
		return this.remove(card);
	}
	
	public void drawBack(State state, CityCard card) {
		state.getPlayerDeck().getDiscardPile().remove(card);
		add(card);
	}
	
	public void removeAndDiscard(State state, Set<CityCard> cardSetDesease) {
		this.removeAll(cardSetDesease);
		state.getPlayerDeck().getDiscardPile().addOnTop((PlayerCard) cardSetDesease);
	}
	
	public void drawBack(State state, Set<CityCard> cardSet) {
		state.getPlayerDeck().getDiscardPile().removeAll(cardSet);
		this.addAll(cardSet);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(TreeSet<CityCard> subHand : this.cityCardHand) {
			sb.append(subHand.toString());
		}
		return sb.toString();
	}
	
	public Hand duplicate() {
		Hand clone = new Hand(this.playedCharacter);
		for(int i=0; i<this.cityCardHand.length; i++) {
			clone.cityCardHand[i].addAll(this.cityCardHand[i]);
		}
		return clone;
	}

	@Override
	public boolean add(CityCard cityCard) {
		if(this.cityCardHand[cityCard.getCity().getDesease().id].add(cityCard)) {
			this.size++;
			return true;
		}
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends CityCard> cityCardCollection) {
		boolean modified = false;
		for(CityCard cityCard : cityCardCollection) {
			if(this.add(cityCard)) modified = true;
		}
		return modified;
	}

	@Override
	public void clear() {
		for(int i=0; i<this.cityCardHand.length; i++) {
			cityCardHand[i].clear();
		}
		this.size=0;
	}

	@Override
	public boolean contains(Object obj) {
		if(obj instanceof CityCard) {
			return this.cityCardHand[((CityCard)obj).getCity().getDesease().id].contains(obj);
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		for(Object obj : arg0) {
			if(!this.contains(obj)) return false;
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		for(int i=0; i<this.cityCardHand.length; i++) {
			if(!cityCardHand[i].isEmpty()) return false;
		}
		return true;
	}

	@Override
	public Iterator<CityCard> iterator() {
		Iterator<CityCard> it = new Iterator<CityCard>() {
			@Override
			public boolean hasNext() {
				throw new UnsupportedOperationException();
			}
			
            @Override
            public CityCard next() {
            	throw new UnsupportedOperationException();
            }
			
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
		};
        return it; 
	}

	@Override
	public boolean remove(Object obj) {
		if(obj instanceof CityCard) {
			if(this.cityCardHand[((CityCard)obj).getCity().getDesease().id].remove(obj)) {
				this.size--;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		boolean modified = false;
		for(Object obj : arg0) {
			if(this.remove(obj)) modified = true;
		}
		return modified;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		for(int i=0; i<this.cityCardHand.length; i++) {
			if(!cityCardHand[i].retainAll(arg0)) return true;
		}
		return false;
	}

	@Override
	public int size() {
		return this.size;
	}
}
