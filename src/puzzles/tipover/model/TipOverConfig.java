package puzzles.tipover.model;

import puzzles.clock.ClockConfig;
import solver.Configuration;
import solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.*;

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
    private char[][] board;

    /**
     * Constructor for a tipover object
     */
    public TipOverConfig(int numRows, int numCols, int[] startCords, int[] goalCords, int[] currentPos, char[][] board){
        this.numRows = numRows;
        this.numCols = numCols;
        this.startCords = startCords;
        this.goalCords = goalCords;
        this.currentPos = currentPos;
        this.board = board;
    }

    public ArrayList<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        try{
            int[] newPos = currentPos;
            newPos[0] -= 1;
            TipOverConfig t1 = new TipOverConfig(this.numRows, this.numCols, this.startCords, this.goalCords, newPos, this.board);
            neighbors.add(t1);
        }catch(NullPointerException n){
        }
        try{
            int[] newPos = currentPos;
            newPos[1] -= 1;
            TipOverConfig t1 = new TipOverConfig(this.numRows, this.numCols, this.startCords, this.goalCords, newPos, this.board);
            neighbors.add(t1);
        }catch(NullPointerException n){
        }
        try{
            int[] newPos = currentPos;
            newPos[1] += 1;
            TipOverConfig t1 = new TipOverConfig(this.numRows, this.numCols, this.startCords, this.goalCords, newPos, this.board);
            neighbors.add(t1);
        }catch(NullPointerException n){
        }
        try{
            int[] newPos = currentPos;
            newPos[1] += 1;
            TipOverConfig t1 = new TipOverConfig(this.numRows, this.numCols, this.startCords, this.goalCords, newPos, this.board);
            neighbors.add(t1);
        }catch(NullPointerException n){
        }
        return neighbors;
    }

    public boolean isSolution(){
        return this.currentPos[0] == this.goalCords[0] && this.currentPos[1] == this.goalCords[1];
    }

    /**
     * The solution path for the puzzle
     * @return An array list containing the configurations to the solution
     */
    public ArrayList<Configuration> getSolutionSteps(){
        return Solver.solverBFS(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipOverConfig that = (TipOverConfig) o;
        return Objects.equals(numRows, that.numRows) && Objects.equals(numCols, that.numCols) && Objects.equals(startCords[0], that.startCords[0])
                && Objects.equals(startCords[1], that.startCords[1]) && Objects.equals(goalCords[0], that.goalCords[0]) && Objects.equals(goalCords[1], that.goalCords[1])
                && Objects.equals(currentPos[0], that.currentPos[0]) && Objects.equals(currentPos[1], that.currentPos[1]) && Objects.equals(board[1], that.board[1]);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numRows, numCols, startCords, goalCords, currentPos, board);
    }

    public ArrayList<Integer> getState() {
        ArrayList<Integer> currentState = new ArrayList<>();
        currentState.add(currentPos[0]);
        currentState.add(currentPos[1]);
        return currentState;
    }
}
