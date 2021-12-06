package solver;
import java.util.ArrayList;

/**
 * Configuration abstraction for the solver algorithm
 *
 * @author Andrew Moulton
 * November 2021
 */
public interface Configuration {

    // Tips
    // Include methods
    // - for the solver: is-goal, get-successors
    // - for get-successors: a copy constructor (can't declare here)
    // - for equality comparison and hashing
    // - for creating a displayable version the configuration
    public ArrayList<Configuration> getNeighbors();

    public ArrayList<Integer> getState();

    public ArrayList<Configuration> getSolutionSteps();

    public boolean isSolution();
}
