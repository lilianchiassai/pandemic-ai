package pandemic.material.card;

import pandemic.material.City;

public class CityCard extends PlayerCard implements Comparable<CityCard> {
  private City city;



  public CityCard(City city) {
    this.city = city;
    city.setCityCard(this);
  }

  public City getCity() {
    return this.city;
  }

  @Override
  public String getTitle() {
    return city.getName();
  }

  @Override
  public int compareTo(CityCard cityCard) {
    return this.city.compareTo(cityCard.city);
  }

}
