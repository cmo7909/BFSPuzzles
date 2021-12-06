package puzzles.lunarlanding.model;

import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Observer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import puzzles.lunarlanding.ptui.LunarLandingPTUI;
import solver.Configuration;


/**
 * Model used by the GUI and the PTUI to give functionality to the commands
 * @author Andrew Moulton
 * November 2021
 */
public class LunarLandingModel {

    private LunarLandingConfig currentConfig;

    private LunarLandingConfig copyConfig;

    private List<Observer<LunarLandingModel, Object>> observers;

    private int[] selectedCoords = new int[2];

    /*
     * Code here includes...
     * Additional data variables for anything needed beyond what is in
     *   the config object to describe the current state of the puzzle
     * Methods to support the controller part of the GUI, e.g., load, move
     * Methods and data to support the "subject" side of the Observer pattern
     *
     * WARNING: To support the hint command, you will likely have to do
     *   a cast of Config to LunarLandingConfig somewhere, since the solve
     *   method works with, and returns, objects of type Configuration.
     */

    /**
     * Getter for the current config
     * @return the config that is found
     */
    public LunarLandingConfig getCurrentConfig(){
        return this.currentConfig;
    }

    /**
     * Constructior for a LunarLanding model
     * @param lunarLanding the first config that will be modified
     * @param copy the same as tipover but is not modified and used to reset tipover
     */
    public LunarLandingModel(LunarLandingConfig lunarLanding, LunarLandingConfig copy){
        this.observers = new LinkedList<>();
        this.currentConfig = lunarLanding;
        this.copyConfig = copy;
        this.reload(copyConfig);
    }

    /**
     * Function to add observers
     * @param obs object added
     */
    public void addObserver(Observer<LunarLandingModel, Object> obs){
        this.observers.add(obs);
    }

    /**
     * Loads a new file specified by the user
     */
    public void load(){
        Scanner in = new Scanner(System.in);
        System.out.println("Type a new file to load");
        System.out.println("data/lunarlanding/lula-0.txt");
        System.out.println("data/lunarlanding/lula-1.txt");
        System.out.println("data/lunarlanding/lula-2.txt");
        System.out.println("data/lunarlanding/lula-3.txt");
        System.out.println("data/lunarlanding/lula-4.txt");
        System.out.println("data/lunarlanding/lula-5.txt");
        System.out.println("data/lunarlanding/lula-6.txt");
        System.out.println("data/lunarlanding/lula-7.txt");
        System.out.println("data/lunarlanding/lula-8.txt");
        System.out.println("data/lunarlanding/lula-9.txt");
        System.out.println("data/lunarlanding/lula-a.txt");
        System.out.print("> ");
        String line = in.nextLine();
        String[] arg = {line};
        LunarLandingPTUI.main(arg);
    }

    /**
     * Resets data about the board
     * @param that the lunarLanding config to reset the board with
     */
    public void reload(LunarLandingConfig that){
        this.currentConfig.setNumRows(that.getNumRows());
        this.currentConfig.setNumCols(that.getNumCols());
        this.currentConfig.setExplorerCoords(that.getExplorerCoords());
        this.currentConfig.setLanderCoords(that.getLanderCoords());
        this.currentConfig.setBoard(that.getBoard());
    }

    /**
     * Used to get the explorer one step closer to the goal position
     * @return boolean value used to prevent an error when the user has made a move that doesn't
     * allow the puzzle to be solved
     */
    public boolean hint(){
        try {
            ArrayList<Configuration> solvableNeighbors = currentConfig.getSolutionSteps();
            for (int i = 0; i < solvableNeighbors.size() - 1; i++) {
                LunarLandingConfig that = (LunarLandingConfig) solvableNeighbors.get(i);
                if(that.sameGrid(currentConfig.getBoard(), that.getBoard())){
                    currentConfig = (LunarLandingConfig) solvableNeighbors.get(i + 1);
                    break;
                }
            }
        }catch (NullPointerException n){
            System.out.println("The puzzle cannot be solved from this position\nplease reload");
            return false;
        }
        return true;
    }

    /**
     * Selects the figure that will move next
     * @param row the row index of the figure to select
     * @param col the column index of the figure to select
     */
    public void choose(int row, int col){
        if(col >= currentConfig.getNumCols()
        || col < 0
        || row >= currentConfig.getNumRows()
        || row < 0
        || currentConfig.getBoard()[row][col] == '0') {
            System.out.println("Invalid choose command");
        }else{
            selectedCoords[0] = row;
            selectedCoords[1] = col;
        }
    }

    //fix no win message
    /**
     * Moves the selected figure in the direction specified if possible
     * @param direction the direction to try to move in (either "N", "E", "S", or "W")
     */
    public void go(String direction){
        boolean canGo = currentConfig.getMoveDirections(selectedCoords[0], selectedCoords[1]).contains(direction.toUpperCase());
        if(canGo){
            if(currentConfig.getBoard()[selectedCoords[0]][selectedCoords[1]] == 'E'){
                char[][] newBoard = currentConfig.updateBoard(currentConfig.getBoard(), selectedCoords[0], selectedCoords[1], direction);
                //find new coordinates of explorer
                int[] newExplorerCoords = new int[2];
                for(int x = 0; x < currentConfig.getBoard().length; x++){
                    for(int y = 0; y < currentConfig.getBoard()[0].length; y++){
                        if(currentConfig.getBoard()[x][y] == 'E'){
                            newExplorerCoords[0] = x;
                            newExplorerCoords[1] = y;
                        }
                    }
                }
                currentConfig = new LunarLandingConfig(currentConfig.getNumRows(), currentConfig.getNumCols(),
                        currentConfig.getLanderCoords(), newExplorerCoords, newBoard);
            }else{
                char[][] newBoard = currentConfig.updateBoard(currentConfig.getBoard(), selectedCoords[0], selectedCoords[1], direction);
                currentConfig = new LunarLandingConfig(currentConfig.getNumRows(), currentConfig.getNumCols(),
                        currentConfig.getLanderCoords(), currentConfig.getExplorerCoords(), newBoard);
            }
        }else{
            System.out.println("Invalid go command");
        }
    }

    /**
     * Checks if the current configuration is the solution
     * @return true if the config is the solution, false otherwise
     */
    public boolean configIsSolution(){
        return this.currentConfig.isSolution();
    }

    /**
     * Calls the LunarLandingConfig's toString
     * @return a string representation of the LunarLandingConfig's data
     */
    public String toString(){
        return this.currentConfig.toString();
    }
}
