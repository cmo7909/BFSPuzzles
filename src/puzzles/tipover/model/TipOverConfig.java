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
        this.startCords = new int[2];
        this.startCords[0] = startCords[0];
        this.startCords[1] = startCords[1];
        this.goalCords = new int[2];
        this.goalCords[0] = goalCords[0];
        this.goalCords[1] = goalCords[1];
        this.currentPos = new int[2];
        this.currentPos[0] = currentPos[0];
        this.currentPos[1] = currentPos[1];
        this.board = new char[numRows][numCols];
        for(int r=0; r<numRows; r++){
            for(int c =0; c<numCols; c++){
                this.board[r][c] = board[r][c];
            }
        }
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

    public String canTip(TipOverConfig check){
        String directions = "";
        String currentVal = String.valueOf(check.board[check.currentPos[0]][check.currentPos[1]]);
        if(isNumeric(currentVal) && Integer.parseInt(currentVal) > 1){
            int towerVal = Integer.parseInt(currentVal);
            //Checking up from tower;
            for(int i=1; i<=towerVal; i++){
                    boolean willFit = true;
                    if(check.board[check.currentPos[0 + i]][check.currentPos[1]] != 0 || check.currentPos[0 + i] <= 0){
                        willFit = false;
                    }
                    if(willFit) {
                        directions += " N";
                    }
            }
            //Checking left of tower
            for(int i=1; i<+towerVal; i++) {
                boolean willFit = true;
                if (check.board[check.currentPos[0]][check.currentPos[1 - i]] != 0 || check.currentPos[1 - i] <=0) {
                    willFit = false;
                }
                if(willFit){
                    directions += " W";
                }
            }
        }
        return "false";
    }

    public boolean isNumeric(String str){
        if(str == null){
            return false;
        }
        try{
            double d = Double.parseDouble(str);
        }catch (NumberFormatException n){
            return false;
        }
        return true;
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

    public String toString(){
        String output = "   ";

        for(int i=0; i<numCols; i++){
            output += "  " + i;
        }
        output += "\n";
        output += "    _____________________";
        output += "\n";
        for(int i=0; i<numRows; i++){
            output += i + " |";
            for(int j=0; j<numCols; j++){
                char toAdd = board[i][j];
                if(toAdd == '0'){
                    output += "  _";
                }else if(i == currentPos[0] && j == currentPos[1]){
                    output += " *" + toAdd;
                }else if(i == goalCords[0] && j == goalCords[1]){
                    output += " !" + toAdd;
                }else{
                    output += "  " + toAdd;
                }
            }
            output += "\n";
        }
        return output;
    }
}
