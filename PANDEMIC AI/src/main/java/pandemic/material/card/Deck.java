package pandemic.material.card;

public interface Deck<T extends Card> {

  public abstract T draw();

  public abstract void shuffle();

  public abstract Deck<T> duplicate();

  public abstract boolean equivalent(Deck<T> deck);

  public abstract int size();
}
