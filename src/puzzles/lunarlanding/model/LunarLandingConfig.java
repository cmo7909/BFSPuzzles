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
    public int getNumRows(){
        return this.numRows;
    }
    public void setNumRows(int val){
        this.numRows = val;
    }

    public int getNumCols(){
        return this.numCols;
    }
    public void setNumCols(int val){
        this.numCols = val;
    }

    public int[] getLanderCoords(){
        return this.landerCoords;
    }
    public void setLanderCoords(int[] pos){
        this.landerCoords[0] = pos[0];
        this.landerCoords[1] = pos[1];
    }

    public int[] getExplorerCoords(){
        return this.explorerCoords;
    }
    public void setExplorerCoords(int[] pos){
        this.landerCoords[0] = pos[0];
        this.landerCoords[1] = pos[1];
    }

    public char[][] getBoard(){
        return this.board;
    }
    public void setBoard(char[][] otherBoard){
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols;j++){
                this.board[i][j] = otherBoard[i][j];
            }
        }
    }

    public ArrayList<Configuration> getNeighbors(){
        ArrayList<Configuration> neighbors = new ArrayList<>();
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[0].length; col++){
                if(board[row][col] != '0'){
                    if(board[row][col] != 'E'){
                        ArrayList<String> moveDirections = getMoveDirections(row, col);
                        for (int i = 0; i < moveDirections.size(); i++) {
                            char[][] updatedBoard = updateBoard(board, row, col, moveDirections.get(i));
                            neighbors.add(new LunarLandingConfig(numRows, numCols, landerCoords, explorerCoords, updatedBoard));
                        }
                    } else { //current figure is the explorer
                        ArrayList<String> moveDirections = getMoveDirections(row, col);
                        for (int i = 0; i < moveDirections.size(); i++) {
                            char[][] updatedBoard = updateBoard(board, row, col, moveDirections.get(i));
                            //find new coordinates of explorer
                            int[] newExplorerCoords = new int[2];
                            for(int x = 0; x < updatedBoard.length; x++){
                                for(int y = 0; y < updatedBoard[0].length; y++){
                                    if(updatedBoard[x][y] == 'E'){
                                        newExplorerCoords[0] = x;
                                        newExplorerCoords[1] = y;
                                    }
                                }
                            }
                            neighbors.add(new LunarLandingConfig(numRows, numCols, landerCoords, newExplorerCoords, updatedBoard));
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    public char[][] updateBoard(char[][] toUpdate, int row, int col, String direction){
        char[][] returnBoard = new char[toUpdate.length][toUpdate[0].length];
        for(int i = 0; i < returnBoard.length; i++){
            for(int j = 0; j < returnBoard[0].length; j++){
                returnBoard[i][j] = toUpdate[i][j];
            }
        }
        char toMove = returnBoard[row][col];
        returnBoard[row][col] = '0';
        if(direction.equals("N")){
            for(int i = 1; i <= row; i++){
                if(board[row-i][col] != '0'){
                    returnBoard[row-i+1][col] = toMove;
                    break;
                }
            }
        }
        if(direction.equals("S")){
            for(int i = 1; i < numRows-row; i++){
                if(board[row+i][col] != '0'){
                    returnBoard[row+i-1][col] = toMove;
                    break;
                }
            }
        }
        if(direction.equals("W")){
            for(int i = 1; i <= col; i++){
                if(board[row][col-i] != '0'){
                    returnBoard[row][col-i+1] = toMove;
                    break;
                }
            }
        }
        if(direction.equals("E")){
            for(int i = 1; i < numCols-col; i++){
                if(board[row][col+i] != '0'){
                    returnBoard[row][col+i-1] = toMove;
                    break;
                }
            }
        }
        return returnBoard;
    }

    public ArrayList<String> getMoveDirections(int row, int col){
        ArrayList<String> directions = new ArrayList<>();
        //Check North
        for(int i = 1; i <= row; i++){
            if(board[row-i][col] != '0'){
                directions.add("N");
                break;
            }
        }
        //Check South
        for(int i = 1; i < numRows-row; i++){
            if(board[row+i][col] != '0'){
                directions.add("S");
                break;
            }
        }
        //Check West
        for(int i = 1; i <= col; i++){
            if(board[row][col-i] != '0'){
                directions.add("W");
                break;
            }
        }
        //Check East
        for(int i = 1; i < numCols-col; i++){
            if(board[row][col+i] != '0'){
                directions.add("E");
                break;
            }
        }
        return directions;
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
        String conCat = "";
        for(int row = 0; row<board.length; row++){
            for(int col = 0; col<board[0].length; col++){
                if(board[row][col] != '0') {
                    int sum = row+col;
                    conCat += String.valueOf(sum);
                }
            }
        }
        if(conCat.length()>9){
            conCat = conCat.substring(0, 8);
        }
        return Integer.parseInt(conCat)/3;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof LunarLandingConfig) {
            LunarLandingConfig that = (LunarLandingConfig) o;
            return sameGrid(this.board, that.board)
                    && this.hashCode() == that.hashCode();
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
                    if(i == landerCoords[0] && j == landerCoords[1]){
                        output += "  !";
                    }
                    else {
                        output += "  _";
                    }
                }else if(i == landerCoords[0] && j == landerCoords[1]){
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
