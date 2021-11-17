package puzzles.clock;
import solver.*;
import java.util.ArrayList;

/**
 * Main class for the "clock" puzzle.
 *
 * @author Andrew Moulton
 */
public class Clock {

    /**
     * Run an instance of the clock puzzle.
     * @param args [0]: number of hours on the clock;
     *             [1]: starting time on the clock;
     *             [2]: goal time to which the clock should be set.
     */
    public static void main( String[] args ) {
        if (args.length != 3) {
            System.out.println( "Usage: java Clock hours start end" );
        }
        else {
            ClockConfig c = new ClockConfig(Integer.valueOf(args[0]),
                                            Integer.valueOf(args[1]),
                                            Integer.valueOf(args[2]),
                                            Integer.valueOf(args[1]));
            System.out.println(c);
            ArrayList<Configuration> solution = c.getSolutionSteps();
            System.out.println("Total configs: " + Solver.getNumTotalConfigs());
            System.out.println("Unique configs: " + Solver.getNumUniqueConfigs());
            if(solution != null){
                for(int i = 0; i < solution.size(); i++){
                    System.out.println("Step " + i + ": " + solution.get(i).getState());
                }
            }
            else{
                System.out.println("No solution");
            }
        }
    }
}
