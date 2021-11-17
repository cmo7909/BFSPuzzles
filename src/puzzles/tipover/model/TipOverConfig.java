package puzzles.tipover.model;

import solver.Configuration;
import solver.Solver;

import java.util.ArrayList;

/**
 * DESCRIPTION
 * @author Craig O'Connor
 * November 2021
 */
public class TipOverConfig implements Configuration {
    private int numRows;
    private int numCols;
    private int[] startCords;
    private int[] goalCords;
    private int[] currentPos;

    /**
     * Constructor for a tipover object
     * @param numRows The number of rows in the board
     * @param numCols The number of cols in the board
     * @param startCords The starting coordinates stored in an array of the tipper(Character)
     * @param goalCords The ending pos coordinates stored in an array (the goal)
     */
    public TipOverConfig(int numRows, int numCols, int[] startCords, int[] goalCords){
        this.numRows = numRows;
        this.numCols = numCols;
        this.startCords = startCords;
        this.goalCords = goalCords;
        this.currentPos = startCords;
    }

    /**
     * Getter for the number of rows
     * @return the number of rows(int)
     */
    public int getNumRows(){
        return this.numRows;
    }

    /**
     * Getter for the number of cols
     * @return the number of cols(int)
     */
    public int getNumCols(){
        return this.getNumCols();
    }

    /**
     * Getter for the starting coordinates
     * @return an array containing the row and col of the starting location
     */
    public int[] getStartCords(){
        return this.startCords;
    }

    /**
     * Getter for the goal location
     * @return an array containing the row and col of the goal destination
     */
    public int[] getGoalCords(){
        return this.goalCords;
    }

    /**
     * Getter for the current position of the tipper
     * @return array containing the coordinates of the current loc of the tipper
     */
    public int[] getCurrentPos(){
        return this.currentPos;
    }

    /**
     * The solution path for the puzzle
     * @return An array list containing the configurations to the solution
     */
    public ArrayList<Configuration> getSolutionSteps(){
        return Solver.solverBFS(this);
    }

    @Override
    public ArrayList<Configuration> getNeighbors(){

    }
}
