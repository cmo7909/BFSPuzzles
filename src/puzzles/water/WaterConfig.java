package puzzles.water;
import puzzles.clock.ClockConfig;
import solver.*;
import java.util.*;


/**
 * Configuration class for the "clock" puzzle.
 *
 * @author Andrew Moulton
 */
public class WaterConfig implements Configuration{
    private Integer goal;
    private ArrayList<Integer> maxCapacities = new ArrayList<>();
    private ArrayList<Integer> waterAmounts = new ArrayList<>();

    public WaterConfig(Integer goal, ArrayList<Integer> maxCaps, ArrayList<Integer> amounts){
        this.goal = goal;

        int numBuckets = maxCaps.size();
        for(int i = 0; i < numBuckets; i++){
            this.maxCapacities.add(maxCaps.get(i));
            this.waterAmounts.add(amounts.get(i));
        }
    }

    public ArrayList<Configuration> getNeighbors(){
        ArrayList<Configuration> neighbors = new ArrayList<>();
        int numBuckets = maxCapacities.size();
        for(int i = 0; i < numBuckets; i++){
            ArrayList<Integer> dumpI = new ArrayList<>(numBuckets);
            for(Integer amt : waterAmounts){
                dumpI.add(amt);
            }
            dumpI.set(i, 0);
            neighbors.add(new WaterConfig(goal, maxCapacities, dumpI));

            ArrayList<Integer> fillI = new ArrayList<>(numBuckets);
            for(Integer amt : waterAmounts){
                fillI.add(amt);
            }
            fillI.set(i, maxCapacities.get(i));
            neighbors.add(new WaterConfig(goal, maxCapacities, fillI));

            for(int j = 0; j < numBuckets; j++){
                if(j != i){
                    ArrayList<Integer> jIntoI = new ArrayList<>(numBuckets);
                    for(Integer amt : waterAmounts){
                        jIntoI.add(amt);
                    }
                    int iAfterPour = waterAmounts.get(i) + waterAmounts.get(j);
                    int iAfterPourCapped = iAfterPour;
                    if(iAfterPour > maxCapacities.get(i)){
                        iAfterPourCapped = maxCapacities.get(i);
                    }
                    jIntoI.set(i, iAfterPourCapped);

                    int jAfterPour = iAfterPour - iAfterPourCapped;
                    jIntoI.set(j, jAfterPour);
                    neighbors.add(new WaterConfig(goal, maxCapacities, jIntoI));
                }
            }
        }
        return neighbors;
    }

    public ArrayList<Integer> getState(){
        return this.waterAmounts;
    }

    public ArrayList<Configuration> getSolutionSteps(){
        return Solver.solverBFS(this, goal);
    }

    public boolean isSolution(){
        for(Integer a : waterAmounts){
            if(a.equals(goal)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterConfig that = (WaterConfig) o;
        return Objects.equals(goal, that.goal) && Objects.equals(maxCapacities, that.maxCapacities) && Objects.equals(waterAmounts, that.waterAmounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goal, maxCapacities, waterAmounts);
    }

    @Override
    public String toString(){
        return "Amount: " + this.goal + ", Buckets: " + this.maxCapacities;
    }

}
