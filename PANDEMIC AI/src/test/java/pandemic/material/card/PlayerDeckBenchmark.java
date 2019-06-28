package pandemic.material.card;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import pandemic.BenchmarkTest;
import pandemic.Reserve;
import pandemic.util.GameUtil;

public class PlayerDeckBenchmark extends BenchmarkTest {

  @State(Scope.Thread)
  public static class LocalState {

    public PlayerDeck playerDeck;

    @Setup
    public void doSetup() {
      playerDeck = new PlayerDeck(2 + GameUtil.random.nextInt(4),
          Reserve.getInstance().getPlayerCardReserve());
    }

  }

  @Benchmark
  public void duplicate(LocalState localState) {
    localState.playerDeck.duplicate();
  }

  @Benchmark
  public void draw(LocalState localState) {
    localState.playerDeck.draw();
  }
}
