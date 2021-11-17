package puzzles.clock;
import solver.*;
import java.util.*;


/**
 * Configuration class for the "clock" puzzle.
 *
 * @author Andrew Moulton
 */
public class ClockConfig implements Configuration {
    private Integer start;
    private Integer end;
    private Integer numHours;
    private Integer time;
    
    public ClockConfig(Integer numHours, Integer start, Integer end, Integer time){
        this.start = start;
        this.end = end;
        this.numHours = numHours;
        this.time = time;
    }

    public int getTime(){
        return this.time;
    }

    public void setTime(Integer newTime){this.time = newTime;}

    public boolean isSolution(){
        return time.equals(this.end);
    }

    public ArrayList<Integer> getState(){
        ArrayList<Integer> currentState =
                new ArrayList<>(Arrays.asList(this.time));
        return currentState;
    }

    public ArrayList<Configuration> getSolutionSteps(){
        return Solver.solverBFS(this, this.end);
    }

    public ArrayList<Configuration> getNeighbors(){
        ClockConfig n1 = new ClockConfig(this.numHours, this.start, this.end, this.time + 1);
        ClockConfig n2 = new ClockConfig(this.numHours, this.start, this.end, this.time - 1);
        if(n1.getTime() > this.numHours){
            n1.setTime(1);
        }
        if(n2.getTime() < 1){
            n2.setTime(this.numHours);
        }
        ArrayList<Configuration> neighbors =
                new ArrayList<>(Arrays.asList(n1, n2));
        return neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClockConfig that = (ClockConfig) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(numHours, that.numHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, numHours, time);
    }

    @Override
    public String toString(){
        return "Hours: " + this.numHours + ", Start: " + this.start + ", End: " + this.end;
    }
}
