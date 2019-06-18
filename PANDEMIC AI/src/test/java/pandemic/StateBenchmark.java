package pandemic;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;


public class StateBenchmark {
	
	@org.openjdk.jmh.annotations.State(Scope.Thread)
	public static class LocalState {
		
		public State state;
		@Setup
		public void doSetup() {
			state = State.Builder.randomStateBuilder().build();
		}
		
		
	}
	
    @Benchmark 
    public void duplicate(LocalState localState) {    	
    	localState.state.duplicate();
    }

}
