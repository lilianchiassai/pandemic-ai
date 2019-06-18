package pandemic.material;

public class Desease {
  private String name;
  private static int index = 0;
  public int id;

  public Desease(String name) {
    this.name = name;
    this.id = index;
    index++;
  }

  public String getName() {
    return name;
  }

}
