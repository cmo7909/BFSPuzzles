package puzzles.tipover.model;

import java.util.LinkedList;
import java.util.List;

import util.Observer;

/**
 * DESCRIPTION
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

    public TipOverModel(TipOverConfig tipOver, TipOverConfig copy){
        this.observers = new LinkedList<>();
        this.currentConfig = tipOver;
        this.copyConfig = copy;
        this.reload(copyConfig);
    }
    public void addObserver(Observer<TipOverModel, Object> obs){
        this.observers.add(obs);
    }

    public void load(){
        System.out.println("test");
    }

    public void reload(TipOverConfig that){
        this.currentConfig.setNumRows(that.getNumRows());
        this.currentConfig.setNumCols(that.getNumCols());
        this.currentConfig.setStartCords(that.getStartCords());
        this.currentConfig.setGoalCords(that.getGoalCords());
        this.currentConfig.setCurrentPos(that.getCurrentPos());
        this.currentConfig.setBoard(that.getBoard());
    }

    public void move(String direction){
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
                moveFunctionTip(valCords, "n");
            }else {
                moveFunction(updatedCords);
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
                moveFunctionTip(valCords, "w");
            }else {
                moveFunction(updatedCords);
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
                moveFunctionTip(valCords, "s");
            }else {
                moveFunction(updatedCords);
            }
        }else if(direction.startsWith("e")){
            int[] updatedCords = new int[2];
            updatedCords[0] = 0;
            updatedCords[1] = 1;

            //Checking to see if the tower needs to be tipped
            int[] valCords = new int[2];
            valCords[0] = currentConfig.getCurrentPos()[0] + updatedCords[0];
            valCords[1] = currentConfig.getCurrentPos()[1] + updatedCords[1];
            if(currentConfig.getBoard()[valCords[0]][valCords[1]] == '0' &&
                    Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]])) > 1){
                moveFunctionTip(currentConfig.getCurrentPos(), "e");
            }else {
                moveFunction(updatedCords);
            }
        }
    }

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

    public void moveFunctionTip(int[] cords, String direction){
        int towerSize = Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]]));
        if(direction == "n" && canTip(cords, direction)){
            for(int i=0; i<= towerSize; i++){
                currentConfig.getBoard()[cords[0] -i][cords[1]] = '1';
            }
            currentConfig.getBoard()[cords[0] + 1][cords[1]] = '0';
            currentConfig.setCurrentPos(cords);
        }else if(direction == "w" && canTip(cords, direction)){
            for(int i=0; i<= towerSize; i++){
                currentConfig.getBoard()[cords[0]][cords[1] - i] = '1';
            }
            currentConfig.getBoard()[cords[0]][cords[1] + 1] = '0';
            currentConfig.setCurrentPos(cords);
        }else if(direction == "s" && canTip(cords, direction)){
            for(int i=0; i<= towerSize; i++){
                currentConfig.getBoard()[cords[0] + i][cords[1]] = '1';
            }
            currentConfig.getBoard()[cords[0] - 1][cords[1]] = '0';
            currentConfig.setCurrentPos(cords);
        }else if( direction == "e" && canTip(cords, direction)){
            for(int i=0; i<= towerSize; i++){
                currentConfig.getBoard()[cords[0]][cords[1] + i] = '1';
            }
            currentConfig.getBoard()[cords[0]][cords[1] - 1] = '0';
            currentConfig.setCurrentPos(cords);
        }else{
            System.out.println("Please Enter a Valid Move");
        }
    }

    public boolean canTip(int[] cords, String direction){
        int towerSize = Integer.parseInt(String.valueOf(currentConfig.getBoard()[currentConfig.getCurrentPos()[0]][currentConfig.getCurrentPos()[1]]));
        if(direction == "n"){
            for(int i=0; i< towerSize; i++){
                if(currentConfig.getBoard()[cords[0] - i][cords[1]] != '0' || cords[0] - i <0){
                    return false;
                }
            }
        }else if(direction == "w"){
            for(int i=0; i< towerSize; i++){
                if(currentConfig.getBoard()[cords[0]][cords[1] - i] != '0' || cords[1] - i <0){
                    return false;
                }
            }
        }else if(direction == "s"){
            for(int i=0; i< towerSize; i++){
                if(currentConfig.getBoard()[cords[0] + i][cords[1]] != '0' || cords[0] + i >= currentConfig.getNumRows()){
                    return false;
                }
            }
        }else if( direction == "e"){
            for(int i=0; i< towerSize; i++){
                if(currentConfig.getBoard()[cords[0]][cords[1] + i] != '0' || cords[1] + i >= currentConfig.getNumCols()){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validMove(int[] loc){
        if(loc[0] < currentConfig.getNumRows() && loc[0] >=0 && loc[1] < currentConfig.getNumCols() && loc[0] >= 0 && currentConfig.getBoard()[loc[0]][loc[1]] != '0'){
            return true;
        }
        return false;
    }


    public boolean configIsSolution(){
        return this.currentConfig.isSolution();
    }

    public String toString(){
        return this.currentConfig.toString();
    }

}
