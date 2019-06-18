package pandemic.material.card;

import pandemic.material.City;

public class PropagationCard extends Card {
  private City city;

  public PropagationCard(City city) {
    this.city = city;
  }

  public City getCity() {
    return this.city;
  }

  @Override
  public String getTitle() {
    return city.getName();
  }
}
