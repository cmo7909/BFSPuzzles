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
        //checking north
        boolean foundNeighbor = false;
        try{
            int[] newPos = new int[2];
            newPos[0] = currentPos[0];
            newPos[1] = currentPos[1];
            newPos[0] -= 1;
            if(this.board[newPos[0]][newPos[1]] != '0'){
                TipOverConfig t = new TipOverConfig(this.numRows, this.numCols, this.startCords, this.goalCords, newPos, this.board);
                neighbors.add(t);
                foundNeighbor = true;
            }
        }catch(IndexOutOfBoundsException i){}
        //Checking West
        try{
            int[] newPos = new int[2];
            newPos[0] = currentPos[0];
            newPos[1] = currentPos[1];
            newPos[1] -= 1;
            if(this.board[newPos[0]][newPos[1]] != '0'){
                TipOverConfig t = new TipOverConfig(this.numRows, this.numCols, this.startCords, this.goalCords, newPos, this.board);
                neighbors.add(t);
                foundNeighbor = true;
            }
        }catch(IndexOutOfBoundsException i){}
        //Checking south
        try{
            int[] newPos = new int[2];
            newPos[0] = currentPos[0];
            newPos[1] = currentPos[1];
            newPos[0] += 1;
            if(this.board[newPos[0]][newPos[1]] != '0'){
                TipOverConfig t = new TipOverConfig(this.numRows, this.numCols, this.startCords, this.goalCords, newPos, this.board);
                neighbors.add(t);
                foundNeighbor = true;
            }
        }catch(IndexOutOfBoundsException i){}
        //Checking East
        try{
            int[] newPos = new int[2];
            newPos[0] = currentPos[0];
            newPos[1] = currentPos[1];
            newPos[1] += 1;
            if(this.board[newPos[0]][newPos[1]] != '0'){
                TipOverConfig t = new TipOverConfig(this.numRows, this.numCols, this.startCords, this.goalCords, newPos, this.board);
                neighbors.add(t);
                foundNeighbor = true;
            }
        }catch(IndexOutOfBoundsException i){}
        int towerSize = Integer.parseInt(String.valueOf(this.board[this.currentPos[0]][this.currentPos[1]]));
        if(!(foundNeighbor) || towerSize > 1) {
                ArrayList<String> towerTipDirections = tipDirections(this);
                if (towerTipDirections != null) {
                    for (String str : towerTipDirections) {
                        neighbors.add(updateBoard(this, str, towerSize));
                    }
                }
        }
        return neighbors;
    }

    public ArrayList<String> tipDirections(TipOverConfig check){
        ArrayList<String> directions = new ArrayList<>();
        if(check.currentPos[0] < numRows && check.currentPos[0] >= 0 && check.currentPos[1] < numCols && check.currentPos[1] >= 0) {
            String currentVal = String.valueOf(check.board[check.currentPos[0]][check.currentPos[1]]);
            if (isNumeric(currentVal) && Integer.parseInt(currentVal) > 1) {
                int towerVal = Integer.parseInt(currentVal);
                //Checking up from tower;
                boolean willFit = true;
                for (int i = 1; i <= towerVal; i++) {

                    if (check.currentPos[0] - i < 0 || check.board[check.currentPos[0] - i][check.currentPos[1]] != '0') {
                        willFit = false;
                    }
                }
                    if (willFit) {
                        directions.add("N");
                    }

                //Checking left of tower
                willFit = true;
                for (int i = 1; i <= towerVal; i++) {
                    if (check.currentPos[1] - i < 0 || check.board[check.currentPos[0]][check.currentPos[1] - i] != '0') {
                        willFit = false;
                    }
                }
                    if (willFit) {
                        directions.add("W");
                    }
                //Checking below tower
                willFit = true;
                for (int i = 1; i <= towerVal; i++) {
                    if (check.currentPos[0] + i >= numRows || check.board[check.currentPos[0] + i][check.currentPos[1]] != '0') {
                        willFit = false;
                    }
                }
                    if (willFit) {
                        directions.add("S");
                    }
                //Checking to the right of the tower
                willFit = true;
                for (int i = 1; i <= towerVal; i++) {
                    if (check.currentPos[1] + i >= numCols || check.board[check.currentPos[0]][check.currentPos[1] + i] != '0') {
                        willFit = false;
                    }
                }
                    if (willFit) {
                        directions.add("E");
                    }
            } else {
                directions.add(null);
            }
        }
        return directions;
    }

    public TipOverConfig updateBoard(TipOverConfig toUpdate, String direction, int amountToReplace){
        TipOverConfig copy = new TipOverConfig(this.numRows, this.numCols, this.startCords, this.goalCords, this.currentPos, this.board);
        if(direction == "N"){
            copy.board[this.currentPos[0]][this.currentPos[1]] = '0';
            for(int i=1; i<=amountToReplace;i++){
                copy.board[toUpdate.currentPos[0] - i][toUpdate.currentPos[1]] = '1';
            }
            copy.currentPos[0] -= 1;
        }else if(direction == "W"){
            copy.board[this.currentPos[0]][this.currentPos[1]] = '0';
            for(int i=1; i<=amountToReplace;i++){
                copy.board[toUpdate.currentPos[0]][toUpdate.currentPos[1] - i] = '1';
            }
            copy.currentPos[1] -= 1;
        }else if(direction == "S"){
            copy.board[this.currentPos[0]][this.currentPos[1]] = '0';
            for(int i=1; i<=amountToReplace;i++){
                copy.board[toUpdate.currentPos[0] + i][toUpdate.currentPos[1]] = '1';
            }
            copy.currentPos[0] += 1;
        }else if(direction == "E"){
            copy.board[this.currentPos[0]][this.currentPos[1]] = '0';
            for(int i=1; i<=amountToReplace;i++){
                copy.board[toUpdate.currentPos[0]][toUpdate.currentPos[1] + i] = '1';
            }
            copy.currentPos[1] += 1;
        }
        return copy;
    }

    public boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException n) {
            return false;
        }
        return true;
    }

    /**
     * The solution path for the puzzle
     * @return An array list containing the configurations to the solution
     */
    public ArrayList<Configuration> getSolutionSteps(){
        return Solver.solverBFS(this);
    }

    public boolean isSolution(){
        return this.currentPos[0] == this.goalCords[0] && this.currentPos[1] == this.goalCords[1];
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof TipOverConfig) {
            TipOverConfig that = (TipOverConfig) o;
            return numRows == that.numRows && numCols == that.numCols && startCords[0] == that.startCords[0]
                    && startCords[1] == that.startCords[1] && goalCords[0] == that.goalCords[0] && goalCords[1] == that.goalCords[1]
                    && currentPos[0] == that.currentPos[0] && currentPos[1] == that.currentPos[1] && sameGrid(this.board, that.board)
                    && this.hashCode() == that.hashCode();
        }
        return false;
    }

    public boolean sameGrid(char[][] firstGrid, char[][]secondGrid){
        for(int i=0;i<numRows;i++){
            for(int j=0;j<numCols;j++){
                if(firstGrid[i][j] != secondGrid[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(numRows, numCols, startCords, goalCords, currentPos, board);
        System.out.println("Hash value: " + hash);
        return hash;
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
        output += "\n    ";

        int numUnderscores = numRows * 3;
        for(int i=0;i<numUnderscores;i++){
            output += "_";
        }
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
