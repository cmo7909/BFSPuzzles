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

    public boolean configIsSolution(){
        return this.currentConfig.isSolution();
    }

    public String toString(){
        return this.currentConfig.toString();
    }

}
