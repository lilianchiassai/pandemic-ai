package pandemic;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public abstract class BenchmarkTest {

  @Test
  public void testRunBenchmark() throws RunnerException {
    Options opt = new OptionsBuilder()

        .include(this.getClass().getSimpleName().subSequence(0,
            this.getClass().getSimpleName().length() - 4) + "Benchmark")
        .mode(Mode.AverageTime).timeUnit(TimeUnit.MICROSECONDS).warmupTime(TimeValue.seconds(1))
        .warmupIterations(2).measurementTime(TimeValue.seconds(1)).measurementIterations(2)
        .threads(1).forks(1)
        // .shouldFailOnError(true)
        // .shouldDoGC(true)
        // .jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+PrintInlining")
        // .addProfiler(WinPerfAsmProfiler.class)
        .build();


    new Runner(opt).run();
  }

}
