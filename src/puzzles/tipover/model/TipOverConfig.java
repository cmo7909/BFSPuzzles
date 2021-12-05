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
 * The configuration for the tipover puzzle, handles the functionality and the creation
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

    /**
     * Setter for numRows
     * @param val int value to set numRows
     */
    public void setNumRows(int val){
        this.numRows = val;
    }

    /**
     * Getter for the number of rows
     * @return an int which is the number of rows
     */
    public int getNumRows(){
        return this.numRows;
    }

    /**
     * Setter for numCols
     * @param val int value to set numCols
     */
    public void setNumCols(int val){
        this.numCols = val;
    }

    /**
     * Getter for the number of cols
     * @return and in which is the number of cols
     */
    public int getNumCols(){
        return this.numCols;
    }

    /**
     * Setter for the starting cords
     * @param pos and int array with the starting [row,col] to set to
     */
    public void setStartCords(int[] pos){
        this.startCords[0] = pos[0];
        this.startCords[1] = pos[1];
    }

    /**
     * Getter for the starting position
     * @return the staring position of the tipper
     */
    public int[] getStartCords(){
        return this.startCords;
    }

    /**
     * Getter for the goal cords
     * @return int[] containing the goals row col position
     */
    public int[] getGoalCords(){
        return this.goalCords;
    }

    /**
     * Setter for the goal cords
     * @param pos an int[] which is used to set the goal cords [row,col]
     */
    public void setGoalCords(int[] pos){
        this.goalCords[0] = pos[0];
        this.goalCords[1] = pos[1];
    }

    /**
     * Setter for the current position
     * @param pos int[] the new position to set the current pos to [row,col]
     */
    public void setCurrentPos(int[] pos){
        this.currentPos[0] = pos[0];
        this.currentPos[1] = pos[1];
    }

    /**
     * Getter for the current position
     * @return the current position of the tipper [row,col]
     */
    public int[] getCurrentPos(){
        return this.currentPos;
    }

    /**
     * Getter for the game board
     * @return a 2 dimensional array of the board and its contents
     */
    public char[][] getBoard(){
        return this.board;
    }

    /**
     * Setter to update the board
     * @param otherBoard the other board to update the current board with
     */
    public void setBoard(char[][] otherBoard){
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols;j++){
                this.board[i][j] = otherBoard[i][j];
            }
        }
    }

    /**
     * Method used to get the valid neighbors of the tippers current position
     * @return an arraylist of valid configurations
     */
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

    /**
     * Checks if the tipper can tip over a tower and not go out of bounds or hit another tower
     * @param check the configuration to check
     * @return and arraylist of cardinal directions the current position can tip in
     */
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

    /**
     * This method updates the board by tipping the tower in the given direction
     * @param toUpdate the board to update
     * @param direction the direction to tip in
     * @param amountToReplace the size of the tower - which is the amount of "1"'s we need to place
     * @return a new tipoverConfig with the changes made
     */
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

    /**
     * Checks to see if a string value is numeric
     * @param str the string to check
     * @return true or false if the string is numeric or not
     */
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

    /**
     * Checks to see if the this config is at the solution
     * @return true or false if the config is the solution
     */
    public boolean isSolution(){
        return this.currentPos[0] == this.goalCords[0] && this.currentPos[1] == this.goalCords[1];
    }

    /**
     * Overridden equals method to compare Tipover Configs
     * @param o the other object to compare to
     * @return boolean true or false if the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipOverConfig that = (TipOverConfig) o;
            return currentPos[0] == that.currentPos[0] && currentPos[1] == that.currentPos[1] && sameGrid(this.board, that.board)
                    && this.hashCode() == that.hashCode();
    }

    /**
     * Checker used in the equals method to compare two 2D arrays
     * @param firstGrid the first 2D array to compare
     * @param secondGrid the second 2D array to compare
     * @return true or false if the 2D arrays are the same or not
     */
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

    /**
     * Hash code method used by the equals method to compare Tipover objects
     * @return an integer value which is the hashcode of the Tipover config
     */
    @Override
    public int hashCode() {
        int start = startCords[0] + startCords[1];
        int current = currentPos[0] + currentPos[0];
        int goal = goalCords[0] + goalCords[1];
        int rowCol = numCols + numRows;
        return (start * current * goal) - rowCol;
    }

    /**
     * Gets the current state of the config
     * @return an arraylist of integer containing the current position
     */
    public ArrayList<Integer> getState() {
        ArrayList<Integer> currentState = new ArrayList<>();
        currentState.add(currentPos[0]);
        currentState.add(currentPos[1]);
        return currentState;
    }

    /**
     * Overridden to string
     * @return a string representation of the Tipover configs data
     */
    @Override
    public String toString(){
        String output = "   ";

        for(int i=0; i<numCols; i++){
            output += "  " + i;
        }
        output += "\n    ";

        int numUnderscores = numCols * 3;
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
