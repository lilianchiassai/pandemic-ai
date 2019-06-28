package pandemic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;
import game.GameState;
import pandemic.action.ActionSerie;
import pandemic.action.GameAction;
import pandemic.material.City;
import pandemic.material.Desease;
import pandemic.material.PlayedCharacter;
import pandemic.material.card.CityCard;
import pandemic.material.card.Hand;
import pandemic.material.card.PlayerCard;
import pandemic.material.card.PlayerDeck;
import pandemic.material.card.PropagationCard;
import pandemic.material.card.PropagationDeck;
import pandemic.util.GameUtil;

public class State extends GameState<Properties> {

  public boolean equivalent(State other) {
    if (!Arrays.equals(characterHand, other.characterHand)) {
      return false;
    }
    if (!Arrays.equals(characterPositionMap, other.characterPositionMap)) {
      return false;
    }
    if (!Arrays.equals(cityBuilt, other.cityBuilt)) {
      return false;
    }
    if (!Arrays.deepEquals(cityCubeQuantity, other.cityCubeQuantity)) {
      return false;
    }
    if (curedDeseaseCount != other.curedDeseaseCount) {
      return false;
    }
    if (!Arrays.equals(curedDeseases, other.curedDeseases)) {
      return false;
    }
    if (currentActionCount != other.currentActionCount) {
      return false;
    }
    if (currentHand == null) {
      if (other.currentHand != null) {
        return false;
      }
    } else if (!currentHand.equals(other.currentHand)) {
      return false;
    }
    if (!Arrays.equals(deseaseCubeReserve, other.deseaseCubeReserve)) {
      return false;
    }
    if (eclosionCount != other.eclosionCount) {
      return false;
    }
    if (eradicatedDeseaseCount != other.eradicatedDeseaseCount) {
      return false;
    }
    if (!Arrays.equals(eradicatedDeseases, other.eradicatedDeseases)) {
      return false;
    }
    if (gameStep != other.gameStep) {
      return false;
    }
    if (playerDeck == null) {
      if (other.playerDeck != null) {
        return false;
      }
    } else if (!playerDeck.equivalent(other.playerDeck)) {
      return false;
    }
    if (previousActionList == null) {
      if (other.previousActionList != null) {
        return false;
      }
    } else if (!previousActionList.equals(other.previousActionList)) {
      return false;
    }
    if (propagationDeck == null) {
      if (other.propagationDeck != null) {
        return false;
      }
    } else if (!propagationDeck.equivalent(other.propagationDeck)) {
      return false;
    }
    if (researchCenterCount != other.researchCenterCount) {
      return false;
    }
    if (turnCount != other.turnCount) {
      return false;
    }
    return true;
  }

  PropagationDeck propagationDeck;
  PlayerDeck playerDeck;

  // Number of turned played (+1)
  int turnCount;
  // Eclosion ladder
  int eclosionCount;
  // Hand of the current player
  Hand currentHand;
  // Number of actions left tothe current player
  int currentActionCount;
  // Current step of the game
  Pandemic.GameStep gameStep;

  // Hand of each playedCharacter for easy access : a PlayedCharacter object game
  // be shared by multiple games, while a hand depends of the game it belongs
  public Hand[] characterHand;
  // Position of all the charaters on the map
  // characterPositionMap[playedCharacter.id]
  City[] characterPositionMap;
  // Number of cube of a desease on a city cityCubeMap[city.id][desease.id]
  int[][] cityCubeQuantity;
  // Number of remaining cubes for each Desease.id
  int[] deseaseCubeReserve;
  // Research center in a city
  boolean[] cityBuilt;

  int researchCenterCount;

  boolean[] curedDeseases;
  boolean[] eradicatedDeseases;

  int curedDeseaseCount;
  int eradicatedDeseaseCount;

  // Rules material
  ActionSerie previousActionList;
  int value;

  public State(Properties properties) {
    super();
    gameProperties = properties;
  }

  public State(Builder builder) {
    super();
    gameProperties = builder.gameProperties;
    propagationDeck = builder.propagationDeck;
    playerDeck = builder.playerDeck;
    turnCount = builder.turnCount;
    eclosionCount = builder.eclosionCount;
    currentHand = builder.currentHand;
    currentActionCount = builder.currentActionCount;
    gameStep = builder.gameStep;
    characterHand = builder.characterHand;
    characterPositionMap = builder.characterPositionMap;
    cityCubeQuantity = builder.cityCubeQuantity;
    deseaseCubeReserve = builder.deseaseCubeReserve;
    cityBuilt = builder.cityBuilt;
    researchCenterCount = builder.researchCenterCount;
    curedDeseases = builder.curedDeseases;
    eradicatedDeseases = builder.eradicatedDeseases;
    curedDeseaseCount = builder.curedDeseaseCount;
    eradicatedDeseaseCount = builder.eradicatedDeseaseCount;
    previousActionList = builder.previousActionList;
  }

  public static class Builder {
    Properties gameProperties;
    PropagationDeck propagationDeck;
    PlayerDeck playerDeck;
    int turnCount;
    int eclosionCount;
    Hand currentHand;
    int currentActionCount;
    Pandemic.GameStep gameStep;
    Hand[] characterHand;
    City[] characterPositionMap;
    int[][] cityCubeQuantity;
    int[] deseaseCubeReserve;
    boolean[] cityBuilt;
    int researchCenterCount;
    boolean[] curedDeseases;
    boolean[] eradicatedDeseases;
    int curedDeseaseCount;
    int eradicatedDeseaseCount;
    ActionSerie previousActionList;
    ArrayList<CityCard> cityCardReserve;

    public Builder(Properties properties, City start) {
      super();
      gameProperties = properties;
      previousActionList = new ActionSerie();
      cityCubeQuantity = new int[gameProperties.map.size()][gameProperties.deseaseList.size()];
      cityBuilt = new boolean[gameProperties.map.size()];
      cityBuilt = new boolean[gameProperties.map.size()];
      deseaseCubeReserve = new int[gameProperties.deseaseList.size()];
      curedDeseases = new boolean[gameProperties.deseaseList.size()];
      eradicatedDeseases = new boolean[gameProperties.deseaseList.size()];
      turnCount = 1;
      eclosionCount = 0;
      gameStep = Pandemic.GameStep.ready;
      curedDeseaseCount = 0;
      eradicatedDeseaseCount = 0;
      currentActionCount = gameProperties.maxActionCount;
      characterHand = new Hand[gameProperties.numberOfPlayers];
      characterPositionMap = new City[gameProperties.numberOfPlayers];
      propagationDeck =
          new PropagationDeck(properties.difficulty, Reserve.getInstance().propagationCardReserve);
      cityCardReserve = new ArrayList<CityCard>(Reserve.getInstance().playerCardReserve);
      Collections.shuffle(cityCardReserve);

      // Create players

      for (final PlayedCharacter character : gameProperties.characterList) {
        characterPositionMap[character.id] = start;
        final Hand hand = new Hand(character);
        characterHand[character.id] = hand;
      }
      currentHand = characterHand[0];
      cityBuilt[start.id] = true;
      researchCenterCount = 1;
    }

    public Builder propagationDeck(PropagationDeck propagationDeck) {
      this.propagationDeck = propagationDeck;
      return this;
    }

    public Builder playerDeck(PlayerDeck playerDeck) {
      this.playerDeck = playerDeck;
      return this;
    }

    public Builder turnCount(int turnCount) {
      this.turnCount = turnCount;
      return this;
    }

    public Builder eclosionCount(int eclosionCount) {
      this.eclosionCount = eclosionCount;
      return this;
    }

    public Builder currentHand(Hand currentHand) {
      this.currentHand = currentHand;
      return this;
    }

    public Builder currentActionCount(int currentActionCount) {
      this.currentActionCount = currentActionCount;
      return this;
    }

    public Builder gameStep(Pandemic.GameStep gameStep) {
      this.gameStep = gameStep;
      return this;
    }

    public Builder characterHand(Hand[] characterHand) {
      this.characterHand = characterHand;
      return this;
    }

    public Builder characterPositionMap(City[] characterPositionMap) {
      this.characterPositionMap = characterPositionMap;
      return this;
    }

    public Builder cityCubeQuantity(int[][] cityCubeQuantity) {
      this.cityCubeQuantity = cityCubeQuantity;
      return this;
    }

    public Builder deseaseCubeReserve(int[] deseaseCubeReserve) {
      this.deseaseCubeReserve = deseaseCubeReserve;
      return this;
    }

    public Builder cityBuilt(boolean[] cityBuilt) {
      this.cityBuilt = cityBuilt;
      return this;
    }

    public Builder researchCenterCount(int researchCenterCount) {
      this.researchCenterCount = researchCenterCount;
      return this;
    }

    public Builder curedDeseases(boolean[] curedDeseases) {
      this.curedDeseases = curedDeseases;
      return this;
    }

    public Builder eradicatedDeseases(boolean[] eradicatedDeseases) {
      this.eradicatedDeseases = eradicatedDeseases;
      return this;
    }

    public Builder curedDeseaseCount(int curedDeseaseCount) {
      this.curedDeseaseCount = curedDeseaseCount;
      return this;
    }

    public Builder eradicatedDeseaseCount(int eradicatedDeseaseCount) {
      this.eradicatedDeseaseCount = eradicatedDeseaseCount;
      return this;
    }

    public Builder previousActionList(ActionSerie previousActionList) {
      this.previousActionList = previousActionList;
      return this;
    }

    public Builder deal() {
      for (final Hand hand : characterHand) {
        for (int i = 0; i < 6 - gameProperties.numberOfPlayers; i++) {
          CityCard card = cityCardReserve.remove(0);
          hand.add(card);
        }
      }
      return this;
    }

    public Builder infect() {
      for (int i = 3; i > 0; i--) {
        for (int k = 0; k < 3; k++) {
          final PropagationCard card = propagationDeck.draw();
          cityCubeQuantity[card.getCity().id][card.getCity().getDesease().id] += i;
        }
      }
      return this;
    }

    public static Builder randomStateBuilder() {
      final Properties gameProperties =
          new Properties(1 + GameUtil.random.nextInt(4), 3 + GameUtil.random.nextInt(3));
      final Builder builder =
          new Builder(gameProperties, GameUtil.getCity("Atlanta")).gameStep(Pandemic.GameStep.play);

      builder.turnCount(GameUtil.random.nextInt(24) + 1);
      builder.eclosionCount(GameUtil.random.nextInt(gameProperties.maxEclosionCounter) + 1);
      builder.currentActionCount(GameUtil.random.nextInt(gameProperties.maxActionCount) + 1);
      builder.eclosionCount(GameUtil.random.nextInt(gameProperties.maxEclosionCounter) + 1);

      for (int i = 0; i < builder.curedDeseases.length; i++) {
        if (GameUtil.random.nextDouble() > 0.4f) {
          builder.curedDeseases[i] = true;
          builder.curedDeseaseCount++;
          if (GameUtil.random.nextDouble() > 0.4f) {
            builder.eradicatedDeseases[i] = true;
            builder.eradicatedDeseaseCount++;
          }
        }
      }

      final int researchCenterCount = builder.researchCenterCount + GameUtil.random
          .nextInt(1 + gameProperties.maxResearchCenterCounter - builder.researchCenterCount);
      for (int i = 1; i < researchCenterCount; i++) {
        final int cityId = (int) (Math.random() * gameProperties.map.size());
        if (cityId != GameUtil.getCity("Atlanta").id && !builder.cityBuilt[cityId]) {
          builder.cityBuilt[cityId] = true;
          builder.researchCenterCount++;
          builder.cityCardReserve.remove(GameUtil.getCard(cityId));
        }
      }

      for (final Desease desease : gameProperties.deseaseList) {
        int cubeInPlay = GameUtil.random.nextInt(gameProperties.maxCubeCount + 1);
        while (cubeInPlay > 0) {
          final City city =
              Reserve.getInstance().deseaseCityCardMap.get(desease).get(GameUtil.random
                  .nextInt(Reserve.getInstance().deseaseCityCardMap.get(desease).size()));
          if (builder.cityCubeQuantity[city.id][desease.id] < 3) {
            builder.cityCubeQuantity[city.id][desease.id]++;
            builder.deseaseCubeReserve[desease.id]++;
            cubeInPlay--;
          }
        }
      }

      for (int i = 0; i < builder.characterPositionMap.length; i++) {
        final Desease desease = gameProperties.deseaseList
            .get(GameUtil.random.nextInt(gameProperties.deseaseList.size()));
        final City position = Reserve.getInstance().deseaseCityCardMap.get(desease).get(
            (int) (Math.random() * Reserve.getInstance().deseaseCityCardMap.get(desease).size()));
        builder.characterPositionMap[i] = position;
      }
      builder
          .currentHand(builder.characterHand[(int) (Math.random() * builder.characterHand.length)]);

      for (final Hand element : builder.characterHand) {
        final int handSize = GameUtil.random.nextInt((gameProperties.maxHandSize + 1));
        int k = 0;
        while (k < handSize) {
          final PlayerCard card = builder.cityCardReserve.remove(0);
          if (card != null && card instanceof CityCard) {
            element.add((CityCard) card);
            k++;
          }
        }
      }

      final int discardedCards = GameUtil.random.nextInt(builder.cityCardReserve.size() + 1);
      for (int i = 0; i < discardedCards; i++) {
        builder.cityCardReserve.remove(0);
      }

      final int propagationDiscarded = GameUtil.random.nextInt(builder.propagationDeck.size() + 1);
      for (int i = 0; i < propagationDiscarded; i++) {
        builder.propagationDeck.draw();
      }
      return builder;
    }

    public static Builder startStateBuilder(Properties gameProperties) {
      return new Builder(gameProperties, GameUtil.getCity("Atlanta")).deal().infect()
          .gameStep(Pandemic.GameStep.play);
    }

    public static Builder blankStateBuilder(Properties gameProperties) {
      return new Builder(gameProperties, GameUtil.getCity("Atlanta"))
          .gameStep(Pandemic.GameStep.play);
    }

    public State build() {


      playerDeck = new PlayerDeck(this.gameProperties.difficulty, this.cityCardReserve);
      return new State(this);
    }
  }

  // Getters
  public PlayerDeck getPlayerDeck() {
    return playerDeck;
  }

  protected int getPropagationSpeed() {
    return gameProperties.getPropagationSpeed(playerDeck.getEpidemicCount());
  }

  public Pandemic.GameStep getGameStep() {
    return gameStep;
  }

  public PlayedCharacter getCurrentPlayer() {
    return currentHand.getCharacter();
  }

  public Hand getCurrentHand() {
    return currentHand;
  }

  public City getCurrentPlayerPosition() {
    return characterPositionMap[currentHand.getCharacter().id];
  }

  protected int getTurnCount() {
    return turnCount;
  }

  public City getCharacterPosition(PlayedCharacter character) {
    return character.id < characterPositionMap.length ? characterPositionMap[character.id] : null;
  }

  public void nextPlayer() {
    currentHand =
        characterHand[(currentHand.getCharacter().id + 1) % gameProperties.numberOfPlayers];
    currentActionCount = gameProperties.maxActionCount;
  }

  public void increaseEclosionCounter() {
    eclosionCount++;
  }

  public void increaseTurnCounter() {
    turnCount++;
  }

  public Hand getCharacterHand(PlayedCharacter character) {
    return characterHand[character.id];
  }

  public int getCityCubeCount(City city, Desease desease) {
    return cityCubeQuantity[city.id][desease.id];
  }

  public int getCubeCurrentReserve(Desease desease) {
    return deseaseCubeReserve[desease.id];
  }

  public int getCurrentActionCount() {
    return currentActionCount;
  }

  public Hand[] getAllCharacterHand() {
    return characterHand;
  }

  public Set<City> getResearchCenters() {
    final HashSet<City> result = new HashSet<City>();
    for (final City city : gameProperties.map) {
      if (cityBuilt[city.id]) {
        result.add(city);
      }
    }
    return result;
  }

  public int getResearchCenterCount() {
    return researchCenterCount;
  }

  public int getEpidemicCount() {
    return playerDeck.getEpidemicCount();
  }

  public LinkedList<GameAction> getPreviousActionList() {
    return previousActionList;
  }

  public boolean hasResearchCenter(City city) {
    return cityBuilt[city.id];
  }

  public boolean isCubeReserveFull(Desease desease) {
    return deseaseCubeReserve[desease.id] == 0;
  }

  public boolean isCured(Desease desease) {
    return curedDeseases[desease.id];
  }

  public boolean isEradicated(Desease desease) {
    return eradicatedDeseases[desease.id];
  }

  public boolean setCharacterPosition(PlayedCharacter currentPlayer, City destination) {
    characterPositionMap[currentPlayer.id] = destination;
    return true;
  }

  public void addToActionList(GameAction gameAction) {
    previousActionList.add(gameAction);
  }

  public boolean removeFromActionList(GameAction gameAction) {
    if (previousActionList.getLast() == gameAction) {
      return previousActionList.removeLast() != null;
    }
    return false;
  }

  public boolean eradicateDesease(Desease desease) {
    if (!eradicatedDeseases[desease.id]) {
      eradicatedDeseaseCount++;
      eradicatedDeseases[desease.id] = true;
      return true;
    }
    return false;
  }

  public boolean unEradicateDesease(Desease desease) {
    if (eradicatedDeseases[desease.id]) {
      eradicatedDeseaseCount--;
      eradicatedDeseases[desease.id] = false;
      return true;
    }
    return false;
  }

  public boolean cureDesease(Desease desease) {
    if (!curedDeseases[desease.id]) {
      curedDeseaseCount++;
      curedDeseases[desease.id] = true;
      return true;
    }
    return false;
  }

  public boolean unCureDesease(Desease desease) {
    if (curedDeseases[desease.id]) {
      curedDeseaseCount--;
      curedDeseases[desease.id] = false;
      return true;
    }
    return false;
  }

  public void decreaseCurrentActionCount(int i) {
    currentActionCount = currentActionCount - i;
  }

  public void increaseCurrentActionCount(int actionCost) {
    currentActionCount += actionCost;
  }

  public boolean addResearchCenter(City city) {
    if (!cityBuilt[city.id] && researchCenterCount < gameProperties.maxResearchCenterCounter) {
      researchCenterCount++;
      cityBuilt[city.id] = Boolean.TRUE;
      return true;
    } else {
      return false;
    }
  }

  public boolean removeResearchCenter(City city) {
    if (cityBuilt[city.id]) {
      researchCenterCount--;
      cityBuilt[city.id] = Boolean.FALSE;
      return true;
    } else {
      return false;
    }
  }

  public boolean addCube(City city, Desease desease) {
    return addCube(city, desease, 1);
  }

  public boolean addCube(City city, Desease desease, int strength) {
    if (deseaseCubeReserve[desease.id] + strength <= gameProperties.maxCubeCount) {
      deseaseCubeReserve[desease.id] += strength;
      cityCubeQuantity[city.id][desease.id] += strength;
      return true;
    } else {
      return false;
    }
  }

  public boolean removeCube(City city, Desease desease) {
    return removeCube(city, desease, 1);
  }

  public boolean removeCube(City city, Desease desease, int strength) {
    if (cityCubeQuantity[city.id][desease.id] - strength >= 0) {
      deseaseCubeReserve[desease.id] -= strength;
      cityCubeQuantity[city.id][desease.id] -= strength;
      return true;
    } else {
      return false;
    }
  }

  @Override
  public State duplicate() {
    final State state = new State(gameProperties);
    state.previousActionList = new ActionSerie(previousActionList);
    state.value = value;

    state.turnCount = turnCount;
    state.eclosionCount = eclosionCount;
    state.currentActionCount = currentActionCount;
    state.gameStep = gameStep;
    state.curedDeseaseCount = curedDeseaseCount;
    state.researchCenterCount = researchCenterCount;
    state.eradicatedDeseaseCount = eradicatedDeseaseCount;

    state.propagationDeck = propagationDeck.duplicate();
    state.playerDeck = playerDeck.duplicate();

    state.characterHand = new Hand[gameProperties.numberOfPlayers];
    for (Hand hand : characterHand) {
      Hand handClone = hand.duplicate();
      state.characterHand[handClone.getCharacter().id] = handClone;
      if (hand == currentHand) {
        state.currentHand = handClone;
      }
    }

    state.characterPositionMap = characterPositionMap.clone();
    state.cityCubeQuantity = new int[48][4];
    for (int i = 0; i < cityCubeQuantity.length; i++) {
      state.cityCubeQuantity[i] = cityCubeQuantity[i].clone();
    }
    state.cityBuilt = cityBuilt.clone();
    state.deseaseCubeReserve = deseaseCubeReserve.clone();
    state.curedDeseases = curedDeseases.clone();
    state.eradicatedDeseases = eradicatedDeseases.clone();
    return state;
  }

  public LightGameStatus lightClone() {
    return new LightGameStatus(this);
  }

  public int updateValue() {
    value = 0;
    value += (gameProperties.maxEclosionCounter - eclosionCount) * 500;
    value += 300 * gameProperties.map.size();
    /*
     * for (final Card propagationCard : propagationDeck.getDiscardPile()) { switch
     * (getCityCubeCount(((PropagationCard) propagationCard).getCity(), ((PropagationCard)
     * propagationCard).getCity().getDesease())) { case 0: break; case 1: value -= 40; break; case
     * 2: value -= 80; break; case 3: value -= 160; break; default: value -= 300; break; } }
     * 
     * for (final LinkedList<PropagationCard> memory : propagationDeck.getMemories()) { for (final
     * PropagationCard memoryCard : memory) { switch (getCityCubeCount(memoryCard.getCity(),
     * memoryCard.getCity().getDesease())) { case 0: break; case 1: value -= 50; break; case 2:
     * value -= 100; break; case 3: value -= 300; break; default: value -= 300; break; } } }
     */

    value += 1000;
    for (final Desease desease : gameProperties.deseaseList) {
      value -= Math.max(getCubeCurrentReserve(desease) * 50, 250);
    }
    value += 1000;
    int neighbourResearchCenter = 0;
    for (final City city : gameProperties.map) {
      if (cityBuilt[city.id]) {
        neighbourResearchCenter = 0;
        for (final City neighbour : city.getNeighbourSet()) {
          if (cityBuilt[neighbour.id]) {
            neighbourResearchCenter++;
          }
        }
        value += 200 / (neighbourResearchCenter + 1);
      }
    }
    value -= gameProperties.maxResearchCenterCounter - researchCenterCount * 200;

    value += curedDeseaseCount * 1000;
    value += eradicatedDeseaseCount * 250;

    for (final Desease desease : gameProperties.deseaseList) {
      int max = 0;
      if (!curedDeseases[desease.id]) {
        for (final Hand hand : characterHand) {
          final int current = hand.stream().filter(GameUtil.getCityCardPredicate(desease))
              .collect(Collectors.toSet()).size();
          max = current > max ? current : max;
        }
      }
      value += 300 * max;
    }
    value += getCurrentCharacterPositionValue() * 10 / 10;
    return value;
  }

  private double getCurrentCharacterPositionValue() {
    double value = 0;
    for (final City city : getCurrentPlayerPosition().getNeighbourSet()) {
      if (getCityCubeCount(city, city.getDesease()) > 0) {
        value++;
      } else {
        for (final City neighbour : city.getNeighbourSet()) {
          if (getCityCubeCount(neighbour, neighbour.getDesease()) > 0) {
            value += 0.5;
          } else {
            for (final City neighbour2 : neighbour.getNeighbourSet()) {
              if (getCityCubeCount(neighbour2, neighbour2.getDesease()) > 0) {
                value += 0.2;
              }
            }
          }
        }
      }

    }
    return value;
  }

  public PlayedCharacter getNextPlayer() {
    return characterHand[(currentHand.getCharacter().id + 1) % gameProperties.numberOfPlayers]
        .getCharacter();
  }
}
