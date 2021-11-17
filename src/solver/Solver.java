package solver;

import java.util.*;

/**
 * This class contains a universal algorithm to find a path from a starting
 * configuration to a solution, if one exists
 *
 * @author Andrew Moulton
 */
public class Solver {
    private static int numUniqueConfigs = 0;
    private static int numTotalConfigs = 0;

    public static ArrayList<Configuration> solverBFS(Configuration puzzleConfig, Integer goalNum) {
        Map<Configuration, Configuration> visited = new HashMap<>();
        visited.put(puzzleConfig, null);
        Queue<Configuration> toVisit = new LinkedList<>();
        toVisit.offer(puzzleConfig);

        while (!toVisit.isEmpty() && !toVisit.peek().isSolution()) {
            Configuration currentVal = toVisit.remove();
            ArrayList<Configuration> neighbors = currentVal.getNeighbors();
            for(int i = 0; i < neighbors.size(); i++) {
                numTotalConfigs++;
                if (!visited.containsKey(neighbors.get(i))) {
                    visited.put(neighbors.get(i), currentVal);
                    toVisit.offer(neighbors.get(i));
                }
            }
            numUniqueConfigs++;
        }

        if (toVisit.isEmpty()) {
            return null;
        }else {
            Configuration last = toVisit.peek();
            ArrayList<Configuration> path = new ArrayList<>();
            path.add(last);
            Configuration prev = visited.get(last);
            numUniqueConfigs++;
            while (prev != null) {
                path.add(0, prev);
                prev = visited.get(prev);
            }
            return path;
        }
    }

    public static int getNumUniqueConfigs(){
        return numUniqueConfigs;
    }

    public static int getNumTotalConfigs(){
        return numTotalConfigs;
    }

}
