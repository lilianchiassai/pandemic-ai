package pandemic;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;

public class PandemicBenchmark {
  @org.openjdk.jmh.annotations.State(Scope.Thread)
  public static class LocalState {
    @Setup
    public void doSetup() {
      pandemic = new Pandemic(State.Builder.randomStateBuilder().build());
      pandemic.setDebugLog(true);
    }

    public Pandemic pandemic;
  }

  @Benchmark
  public void duplicate(LocalState localState) {
    localState.pandemic.duplicate();
  }
}
