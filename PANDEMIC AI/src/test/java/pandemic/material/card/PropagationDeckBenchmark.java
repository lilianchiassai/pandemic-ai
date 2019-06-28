package pandemic.material.card;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import pandemic.BenchmarkTest;
import pandemic.Reserve;
import pandemic.util.GameUtil;

class PropagationDeckBenchmark extends BenchmarkTest {

  @State(Scope.Thread)
  public static class LocalState {

    public PropagationDeck propagationDeck;

    @Setup
    public void doSetup() {
      propagationDeck = new PropagationDeck(2 + GameUtil.random.nextInt(4),
          Reserve.getInstance().getPropagationCardReserve());
      for(int i = 0; i<10; i++) {
        propagationDeck.draw();
      }
    }

  }

  @Benchmark
  public void duplicate(LocalState localState) {
    localState.propagationDeck.duplicate();
  }

  @Benchmark
  public void draw(LocalState localState) {
    localState.propagationDeck.draw();
  }
  
  @Benchmark
  public void drawBottomAndIntensifies(LocalState localState) {
    localState.propagationDeck.drawBottomAndIntensify();
  }
}