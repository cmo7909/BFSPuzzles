package puzzles.lunarlanding.model;

import puzzles.lunarlanding.LunarLanding;
import puzzles.tipover.model.TipOverConfig;
import solver.Configuration;
import solver.Solver;

import java.util.ArrayList;
import java.util.Objects;

/**
 * DESCRIPTION
 * @author Andrew Moulton
 * November 2021
 */
public class LunarLandingConfig implements Configuration {
    private int numRows;
    private int numCols;
    private int[] landerCoords;
    private int[] explorerCoords;
    private char[][] board;

    /**
     * Constructor for a tipover object
     */
    public LunarLandingConfig(int numRows, int numCols, int[] landerCoords, int[] explorerCoords, char[][] board){
        this.numRows = numRows;
        this.numCols = numCols;
        this.landerCoords = new int[]{landerCoords[0], landerCoords[1]};
        this.explorerCoords = new int[]{explorerCoords[0], explorerCoords[1]};
        this.board = new char[numRows][numCols];
        for(int r=0; r<numRows; r++){
            for(int c =0; c<numCols; c++){
                this.board[r][c] = board[r][c];
            }
        }
    }

    public ArrayList<Configuration> getNeighbors(){

    }

    public ArrayList<Integer> getState(){
        ArrayList<Integer> currentState = new ArrayList<>();
        currentState.add(explorerCoords[0]);
        currentState.add(explorerCoords[1]);
        return currentState;
    }

    /**
     * The solution path for the puzzle
     * @return An array list containing the configurations to the solution
     */
    public ArrayList<Configuration> getSolutionSteps(){
        return Solver.solverBFS(this);
    }

    public boolean isSolution(){
        return this.explorerCoords[0] == this.landerCoords[0] && this.explorerCoords[1] == this.landerCoords[1];
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
        return Objects.hash(numRows, numCols, landerCoords, explorerCoords, board);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof LunarLandingConfig) {
            LunarLandingConfig that = (LunarLandingConfig) o;
            return numRows == that.numRows && numCols == that.numCols
                    && landerCoords[0] == that.landerCoords[0] && landerCoords[1] == that.landerCoords[1]
                    && explorerCoords[0] == that.explorerCoords[0] && explorerCoords[1] == that.explorerCoords[1]
                    && sameGrid(this.board, that.board)
                    /*&& this.hashCode() == that.hashCode()*/;
        }
        return false;
    }

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
                }else if(i == landerCoords[0] && j == landerCoords[1]){
                    if(toAdd == '0'){
                        output += "  !";
                    }
                    else{
                        output += " !" + toAdd;
                    }
                }else{
                    output += "  " + toAdd;
                }
            }
            output += "\n";
        }
        return output;
    }

}
