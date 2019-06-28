package pandemic.engine;

import game.Engine;
import pandemic.Pandemic;

public class GameEngine {

  public static void main(String[] args) {
    int a = 3;
    int b = a;
    a++;
    System.out.println(b);

    PandemicRolloutPlayer rolloutPlayer = new PandemicRolloutPlayer();
    Engine<Pandemic> randomEngine = new Engine<Pandemic>(rolloutPlayer);
    /*
     * Pandemic pandemic = new Pandemic(2,3); HumanPlayer player = new HumanPlayer();
     * 
     * MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(pandemic,200,rolloutPlayer); AIPlayer ai
     * = new AIPlayer(mcts, 200, 10); Engine gameEngine = new Engine(ai);
     * 
     * gameEngine.run(pandemic);
     */

    int i = 0;
    while (i < 1000) {
      Pandemic pandemic = new Pandemic(2, 3);
      randomEngine.run(pandemic);
      i++;
    }
  }
}
