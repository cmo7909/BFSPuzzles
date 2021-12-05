package puzzles.tipover.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import puzzles.tipover.ptui.TipOverPTUI;
import solver.Configuration;
import util.Observer;

/**
 * Model used by the GUI and the PTUI to give functionality to the commands
 * @author Craig O'Connor
 * November 2021
 */
public class TipOverModel {

    private TipOverConfig currentConfig;

    private TipOverConfig copyConfig;

    private List<Observer<TipOverModel, Object> > observers;

    /*
     * Code here includes...
     * Additional data variables for anything needed beyond what is in
     *   the config object to describe the current state of the puzzle
     * Methods to support the controller part of the GUI, e.g., load, move
     * Methods and data to support the "subject" side of the Observer pattern
     *
     * WARNING: To support the hint command, you will likely have to do
     *   a cast of Config to TipOverConfig somewhere, since the solve
     *   method works with, and returns, objects of type Configuration.
     */

    /**
     * Getter for the current config
     * @return the config that is found
     */
    public TipOverConfig getCurrentConfig(){
        return this.currentConfig;
    }

    /**
     * Constructior for a tipover model
     * @param tipOver the first config that will be modified
     * @param copy the same as tipover but is not modified and used to reset tipover
     */
    public TipOverModel(TipOverConfig tipOver, TipOverConfig copy){
        this.observers = new LinkedList<>();
        this.currentConfig = tipOver;
        this.copyConfig = copy;
        this.reload(copyConfig);
    }

    /**
     * Function to add observers
     * @param obs object added
     */
    public void addObserver(Observer<TipOverModel, Object> obs){
        this.observers.add(obs);
    }

    /**
     * Method used to load a new File that the user wants
     */
    public void load(){
        Scanner in = new Scanner(System.in);
        System.out.println("Type a new file to load");
        System.out.println("data/tipover/tipover-0.txt");
        System.out.println("data/tipover/tipover-1.txt");
        System.out.println("data/tipover/tipover-2.txt");
        System.out.println("data/tipover/tipover-3.txt");
        System.out.println("data/tipover/tipover-4.txt");
        System.out.println("data/tipover/tipover-5.txt");
        System.out.println("data/tipover/tipover-6.txt");
        System.out.println("data/tipover/tipover-7.txt");
        System.out.println("data/tipover/tipover-8.txt");
        System.out.println("data/tipover/tipover-9.txt");
        System.out.println("data/tipover/tipover-a.txt");
        System.out.print("> ");
        String line = in.nextLine();
        String[] arg = {line};
        TipOverPTUI.main(arg);
    }

    /**
     * Used in the PTUI to reset the board
     * @param that the tipover config to reset the board with
     */
    public void reload(TipOverConfig that){
        this.currentConfig.setNumRows(that.getNumRows());
        this.currentConfig.setNumCols(that.getNumCols());
        this.currentConfig.setStartCords(that.getStartCords());
        this.currentConfig.setGoalCords(that.getGoalCords());
        this.currentConfig.setCurrentPos(that.getCurrentPos());
        this.currentConfig.setBoard(that.getBoard());
    }

    /**
     * Used to get one step closer to the goal position from the current position
     * @return boolean value used to prevent an error when the user has made a move that doesnt
     * allow the puzzle to be solved from that position
     */
    public boolean hint(){
        try {
            ArrayList<Configuration> movableNeighbors = currentConfig.getSolutionSteps();
            for (int i = 0; i < movableNeighbors.size() - 1; i++) {
                TipOverConfig that = (TipOverConfig) movableNeighbors.get(i);
                if (that.getCurrentPos()[0] == currentConfig.getCurrentPos()[0] && that.getCurrentPos()[1] == currentConfig.getCurrentPos()[1]) {
                    currentConfig = (TipOverConfig) movableNeighbors.get(i + 1);
                    break;
                }
            }
        }catch (NullPointerException n){
            System.out.println("The puzzle cannot be solved from this position\n please reload");
            return false;

        }
        return true;
    }

    /*Functions for moving based on user input */

    /**
     * Moves the tipper in the direvtion specified if possible
     * @param direction the direction to try to move in
     */
    public void move(String direction){
        try{
        if(direction.startsWith("n")){
            int[] updatedCords = new int[2];
            updatedCords[0] = -1;
            updatedCords[1] =  0;
            //Checking to see if the tower needs to be tipped
            int[] valCords = new int[2];
            valCords[0] = currentConfig.getCurrentPos()[0] + updatedCords[0];
            valCords[1] = currentConfig.getCurrentPos()[1] + updatedCords[1];
            if(currentConfig.getBoard()[valCords[0]][valCords[1]] == '0' &&
                    Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]])) > 1){
                moveFunctionTip(currentConfig.getCurrentPos(), "n");
            }else {
                try {
                    moveFunction(updatedCords);
                }catch(ArrayIndexOutOfBoundsException a){
                    System.out.println("Please Enter a Valid Move");
                }
            }
        }else if(direction.startsWith("w")){
            int[] updatedCords = new int[2];
            updatedCords[0] = 0;
            updatedCords[1] = -1;

            //Checking to see if the tower needs to be tipped
            int[] valCords = new int[2];
            valCords[0] = currentConfig.getCurrentPos()[0] + updatedCords[0];
            valCords[1] = currentConfig.getCurrentPos()[1] + updatedCords[1];
            if(currentConfig.getBoard()[valCords[0]][valCords[1]] == '0' &&
                    Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]])) > 1){
                moveFunctionTip(currentConfig.getCurrentPos(), "w");
            }else {
                try {
                    moveFunction(updatedCords);
                }catch(ArrayIndexOutOfBoundsException a){
                    System.out.println("Please Enter a Valid Move");
                }
            }
        }else if(direction.startsWith("s")){
            int[] updatedCords = new int[2];
            updatedCords[0] = 1;
            updatedCords[1] = 0;

            //Checking to see if the tower needs to be tipped
            int[] valCords = new int[2];
            valCords[0] = currentConfig.getCurrentPos()[0] + updatedCords[0];
            valCords[1] = currentConfig.getCurrentPos()[1] + updatedCords[1];
            if(currentConfig.getBoard()[valCords[0]][valCords[1]] == '0' &&
                    Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]])) > 1){
                moveFunctionTip(currentConfig.getCurrentPos(), "s");
            }else {
                try {
                    moveFunction(updatedCords);
                }catch(ArrayIndexOutOfBoundsException a){
                    System.out.println("Please Enter a Valid Move");
                }
            }
        }else if(direction.startsWith("e")) {
                int[] updatedCords = new int[2];
                updatedCords[0] = 0;
                updatedCords[1] = 1;

                //Checking to see if the tower needs to be tipped
                int[] valCords = new int[2];
                valCords[0] = currentConfig.getCurrentPos()[0];
                valCords[1] = currentConfig.getCurrentPos()[1] + 1;
                if (currentConfig.getBoard()[valCords[0]][valCords[1]] == '0' &&
                        Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]])) > 1) {
                    moveFunctionTip(currentConfig.getCurrentPos(), "e");
                } else {
                    moveFunction(updatedCords);
                }
            }
        }catch(ArrayIndexOutOfBoundsException a){
            System.out.println("Please Enter a Valid Move");
        }
    }

    /**
     * Actually moves the tipper(used in move)
     * @param cords the current position of the tipper
     */
    public void moveFunction(int[] cords){
        int[] newPos = new int[2];
        newPos[0] = currentConfig.getCurrentPos()[0] + cords[0];
        newPos[1] = currentConfig.getCurrentPos()[1] + cords[1];
        if(validMove(newPos)) {
            currentConfig = new TipOverConfig(currentConfig.getNumRows(), currentConfig.getNumCols(), currentConfig.getStartCords(), currentConfig.getGoalCords(), newPos, currentConfig.getBoard());
        }else{
            System.out.println("Please Enter a Valid Move");
        }
    }

    /**
     * The move function called if the tipper cannot move in a specific direction but can tip a tower
     * @param cords the current position of the tipper
     * @param direction the direction to tip
     */
    public void moveFunctionTip(int[] cords, String direction){
        int towerSize = Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]]));
        if(direction == "n" && canTip(cords, direction)){
            for(int i=1; i<= towerSize; i++){
                currentConfig.getBoard()[cords[0] - i][cords[1]] = '1';
            }
            currentConfig.getBoard()[cords[0]][cords[1]] = '0';
            cords[0] -= 1;
            currentConfig.setCurrentPos(cords);
        }else if(direction == "w" && canTip(cords, direction)){
            for(int i=1; i<= towerSize; i++){
                currentConfig.getBoard()[cords[0]][cords[1] - i] = '1';
            }
            currentConfig.getBoard()[cords[0]][cords[1]] = '0';
            cords[1] -= 1;
            currentConfig.setCurrentPos(cords);
        }else if(direction == "s" && canTip(cords, direction)){
            for(int i=1; i<= towerSize; i++){
                currentConfig.getBoard()[cords[0] + i][cords[1]] = '1';
            }
            currentConfig.getBoard()[cords[0]][cords[1]] = '0';
            cords[0] += 1;
            currentConfig.setCurrentPos(cords);
        }else if( direction == "e" && canTip(cords, direction)){
            for(int i=1; i<= towerSize; i++){
                currentConfig.getBoard()[cords[0]][cords[1] + i] = '1';
            }
            currentConfig.getBoard()[cords[0]][cords[1]] = '0';
            cords[1] += 1;
            currentConfig.setCurrentPos(cords);
        }else{
            System.out.println("Please Enter a Valid Move");
        }
    }

    /**
     * Checks to see if the tower can be tipper
     * @param cords the current position
     * @param direction the direction to tip in
     * @return true or false if the tipped tower will fit
     */
    public boolean canTip(int[] cords, String direction){
        int towerSize = Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]]));
        if(direction == "n"){
            for(int i=1; i<= towerSize; i++){
                if(currentConfig.getBoard()[cords[0] - i][cords[1]] != '0' || cords[0] - i <0){
                    return false;
                }
            }
        }else if(direction == "w"){
            for(int i=1; i<= towerSize; i++){
                if(currentConfig.getBoard()[cords[0]][cords[1] - i] != '0' || cords[1] - i <0){
                    return false;
                }
            }
        }else if(direction == "s"){
            for(int i=1; i<= towerSize; i++){
                if(currentConfig.getBoard()[cords[0] + i][cords[1]] != '0' || cords[0] + i >= currentConfig.getNumRows()){
                    return false;
                }
            }
        }else if( direction == "e"){
            for(int i=1; i<= towerSize; i++){
                if(currentConfig.getBoard()[cords[0]][cords[1] + i] != '0' || cords[1] + i >= currentConfig.getNumCols()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks to see if the movment in a direction is withing the bounds of the configs 2D array
     * @param loc the position that we are checking
     * @return true or false if the position is valid or not
     */
    public boolean validMove(int[] loc){
        if(loc[0] < currentConfig.getNumRows() && loc[0] >=0 && loc[1] < currentConfig.getNumCols() && loc[1] >= 0 && currentConfig.getBoard()[loc[0]][loc[1]] != '0'){
            return true;
        }
        return false;
    }

    /* End of move functions */

    /**
     * Checks if the current configuration is the solution
     * @return true or false if the config is the solution
     */
    public boolean configIsSolution(){
        return this.currentConfig.isSolution();
    }

    /**
     * Calls the tipoverConfigs toString
     * @return a string representation of the tipoverConfigs data
     */
    public String toString(){
        return this.currentConfig.toString();
    }

}
