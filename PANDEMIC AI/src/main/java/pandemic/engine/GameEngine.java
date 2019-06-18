package pandemic.engine;

import java.util.Observable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.Engine;
import pandemic.Pandemic;

public class GameEngine extends Observable {
  private static Logger logger = LogManager.getLogger(GameEngine.class.getName());

  public static void main(String[] args) {
    int a = 3;
    int b = a;
    a++;
    System.out.println(b);

    PandemicRolloutPlayer rolloutPlayer = new PandemicRolloutPlayer();
    Engine randomEngine = new Engine(rolloutPlayer);
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
