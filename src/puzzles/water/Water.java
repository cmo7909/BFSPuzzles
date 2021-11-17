package puzzles.water;
import solver.*;
import java.util.ArrayList;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Andrew Moulton
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main( String[] args ) {
        if ( args.length < 2 ) {
            System.out.println(
                    ( "Usage: java Water amount bucket1 bucket2 ..." )
            );
        }
        else {
            ArrayList<Integer> maxCaps = new ArrayList<>();
            for(String s : args){
                maxCaps.add(Integer.valueOf(s));
            }
            Integer goalAmt = maxCaps.remove(0);

            ArrayList<Integer> startAmts = new ArrayList<>();
            for(int i = 0; i < maxCaps.size(); i++){
                startAmts.add(0);
            }
            WaterConfig w = new WaterConfig(goalAmt, maxCaps, startAmts);
            System.out.println(w);
            ArrayList<Configuration> solutionSteps = w.getSolutionSteps();
            System.out.println("Total Configs: " + Solver.getNumTotalConfigs());
            System.out.println("Unique Configs: " + Solver.getNumUniqueConfigs());
            if(solutionSteps != null){
                for(int i = 0; i < solutionSteps.size(); i++){
                    System.out.println("Step " + i + ": " + solutionSteps.get(i).getState());
                }
            }
            else{
                System.out.println("No solution");
            }
        }
    }
}
