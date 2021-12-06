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
            System.out.println("The puzzle cannot be solved from this position\n please reload");
            return false;
        }
        return true;
    }

    /**
     * Selects the figure that will move next
     * @param row the row index of the figure to select
     * @param col the column index of the figure to select
     */
    public void choose(int row, int col){}

    /**
     * Moves the selected figure in the direction specified if possible
     * @param direction the direction to try to move in
     */
    public void go(String direction){}

//    /**
//     * Moves the tipper in the direvtion specified if possible
//     * @param direction the direction to try to move in
//     */
//    public void move(String direction){
//        try{
//            if(direction.startsWith("n")){
//                int[] updatedCords = new int[2];
//                updatedCords[0] = -1;
//                updatedCords[1] =  0;
//                //Checking to see if the tower needs to be tipped
//                int[] valCords = new int[2];
//                valCords[0] = currentConfig.getCurrentPos()[0] + updatedCords[0];
//                valCords[1] = currentConfig.getCurrentPos()[1] + updatedCords[1];
//                if(currentConfig.getBoard()[valCords[0]][valCords[1]] == '0' &&
//                        Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]])) > 1){
//                    moveFunctionTip(currentConfig.getCurrentPos(), "n");
//                }else {
//                    try {
//                        moveFunction(updatedCords);
//                    }catch(ArrayIndexOutOfBoundsException a){
//                        System.out.println("Please Enter a Valid Move");
//                    }
//                }
//            }else if(direction.startsWith("w")){
//                int[] updatedCords = new int[2];
//                updatedCords[0] = 0;
//                updatedCords[1] = -1;
//
//                //Checking to see if the tower needs to be tipped
//                int[] valCords = new int[2];
//                valCords[0] = currentConfig.getCurrentPos()[0] + updatedCords[0];
//                valCords[1] = currentConfig.getCurrentPos()[1] + updatedCords[1];
//                if(currentConfig.getBoard()[valCords[0]][valCords[1]] == '0' &&
//                        Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]])) > 1){
//                    moveFunctionTip(currentConfig.getCurrentPos(), "w");
//                }else {
//                    try {
//                        moveFunction(updatedCords);
//                    }catch(ArrayIndexOutOfBoundsException a){
//                        System.out.println("Please Enter a Valid Move");
//                    }
//                }
//            }else if(direction.startsWith("s")){
//                int[] updatedCords = new int[2];
//                updatedCords[0] = 1;
//                updatedCords[1] = 0;
//
//                //Checking to see if the tower needs to be tipped
//                int[] valCords = new int[2];
//                valCords[0] = currentConfig.getCurrentPos()[0] + updatedCords[0];
//                valCords[1] = currentConfig.getCurrentPos()[1] + updatedCords[1];
//                if(currentConfig.getBoard()[valCords[0]][valCords[1]] == '0' &&
//                        Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]])) > 1){
//                    moveFunctionTip(currentConfig.getCurrentPos(), "s");
//                }else {
//                    try {
//                        moveFunction(updatedCords);
//                    }catch(ArrayIndexOutOfBoundsException a){
//                        System.out.println("Please Enter a Valid Move");
//                    }
//                }
//            }else if(direction.startsWith("e")) {
//                int[] updatedCords = new int[2];
//                updatedCords[0] = 0;
//                updatedCords[1] = 1;
//
//                //Checking to see if the tower needs to be tipped
//                int[] valCords = new int[2];
//                valCords[0] = currentConfig.getCurrentPos()[0];
//                valCords[1] = currentConfig.getCurrentPos()[1] + 1;
//                if (currentConfig.getBoard()[valCords[0]][valCords[1]] == '0' &&
//                        Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]])) > 1) {
//                    moveFunctionTip(currentConfig.getCurrentPos(), "e");
//                } else {
//                    moveFunction(updatedCords);
//                }
//            }
//        }catch(ArrayIndexOutOfBoundsException a){
//            System.out.println("Please Enter a Valid Move");
//        }
//    }
//
//    /**
//     * Actually moves the tipper(used in move)
//     * @param cords the current position of the tipper
//     */
//    public void moveFunction(int[] cords){
//        int[] newPos = new int[2];
//        newPos[0] = currentConfig.getCurrentPos()[0] + cords[0];
//        newPos[1] = currentConfig.getCurrentPos()[1] + cords[1];
//        if(validMove(newPos)) {
//            currentConfig = new TipOverConfig(currentConfig.getNumRows(), currentConfig.getNumCols(), currentConfig.getStartCords(), currentConfig.getGoalCords(), newPos, currentConfig.getBoard());
//        }else{
//            System.out.println("Please Enter a Valid Move");
//        }
//    }
//
//    /**
//     * The move function called if the tipper cannot move in a specific direction but can tip a tower
//     * @param cords the current position of the tipper
//     * @param direction the direction to tip
//     */
//    public void moveFunctionTip(int[] cords, String direction){
//        int towerSize = Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]]));
//        if(direction == "n" && canTip(cords, direction)){
//            for(int i=1; i<= towerSize; i++){
//                currentConfig.getBoard()[cords[0] - i][cords[1]] = '1';
//            }
//            currentConfig.getBoard()[cords[0]][cords[1]] = '0';
//            cords[0] -= 1;
//            currentConfig.setCurrentPos(cords);
//        }else if(direction == "w" && canTip(cords, direction)){
//            for(int i=1; i<= towerSize; i++){
//                currentConfig.getBoard()[cords[0]][cords[1] - i] = '1';
//            }
//            currentConfig.getBoard()[cords[0]][cords[1]] = '0';
//            cords[1] -= 1;
//            currentConfig.setCurrentPos(cords);
//        }else if(direction == "s" && canTip(cords, direction)){
//            for(int i=1; i<= towerSize; i++){
//                currentConfig.getBoard()[cords[0] + i][cords[1]] = '1';
//            }
//            currentConfig.getBoard()[cords[0]][cords[1]] = '0';
//            cords[0] += 1;
//            currentConfig.setCurrentPos(cords);
//        }else if( direction == "e" && canTip(cords, direction)){
//            for(int i=1; i<= towerSize; i++){
//                currentConfig.getBoard()[cords[0]][cords[1] + i] = '1';
//            }
//            currentConfig.getBoard()[cords[0]][cords[1]] = '0';
//            cords[1] += 1;
//            currentConfig.setCurrentPos(cords);
//        }else{
//            System.out.println("Please Enter a Valid Move");
//        }
//    }
//
//    /**
//     * Checks to see if the tower can be tipper
//     * @param cords the current position
//     * @param direction the direction to tip in
//     * @return true or false if the tipped tower will fit
//     */
//    public boolean canTip(int[] cords, String direction){
//        int towerSize = Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]]));
//        if(direction == "n"){
//            for(int i=1; i<= towerSize; i++){
//                if(currentConfig.getBoard()[cords[0] - i][cords[1]] != '0' || cords[0] - i <0){
//                    return false;
//                }
//            }
//        }else if(direction == "w"){
//            for(int i=1; i<= towerSize; i++){
//                if(currentConfig.getBoard()[cords[0]][cords[1] - i] != '0' || cords[1] - i <0){
//                    return false;
//                }
//            }
//        }else if(direction == "s"){
//            for(int i=1; i<= towerSize; i++){
//                if(currentConfig.getBoard()[cords[0] + i][cords[1]] != '0' || cords[0] + i >= currentConfig.getNumRows()){
//                    return false;
//                }
//            }
//        }else if( direction == "e"){
//            for(int i=1; i<= towerSize; i++){
//                if(currentConfig.getBoard()[cords[0]][cords[1] + i] != '0' || cords[1] + i >= currentConfig.getNumCols()){
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks to see if the movment in a direction is withing the bounds of the configs 2D array
//     * @param loc the position that we are checking
//     * @return true or false if the position is valid or not
//     */
//    public boolean validMove(int[] loc){
//        if(loc[0] < currentConfig.getNumRows() && loc[0] >=0 && loc[1] < currentConfig.getNumCols() && loc[1] >= 0 && currentConfig.getBoard()[loc[0]][loc[1]] != '0'){
//            return true;
//        }
//        return false;
//    }

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
