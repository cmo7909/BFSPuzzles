package puzzles.tipover.ptui;

import puzzles.tipover.TipOver;
import puzzles.tipover.model.TipOverConfig;
import puzzles.tipover.model.TipOverModel;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Plain Text User Interface for playing the Tipover Game
 * @author Craig O'Connor
 * November 2021
 */
public class TipOverPTUI implements Observer<TipOverModel, Object> {

    /**
     * Model for view controller
     */
    private TipOverModel model;
    private TipOverConfig copy;

    public TipOverPTUI(int rows, int cols, int[] start, int[] end, int[] current, char[][] game){
        TipOverConfig modelConfig = new TipOverConfig(rows, cols, start, end, current, game);
        TipOverConfig copyModel = new TipOverConfig(rows, cols, start, end, current, game);
        this.copy = copyModel;

        this.model = new TipOverModel(modelConfig, copyModel);

        initializeView();
    }

    /**
     * Method that checks what to do based on the command that the user enters
     */
    private void run(){
        Scanner in = new Scanner(System.in);
        for( ; ; ){
            System.out.print("game command: ");
            String line = in.nextLine();
            line.toLowerCase(Locale.ROOT);
            String[] words = line.split("\\s+");
            if(words.length == 1) {
                if (words[0].startsWith("q")) {
                    System.exit(1);
                } else if (words[0].startsWith("r")) {
                    this.model.reload(copy);
                    update(this.model, null);
                } else if (words[0].startsWith("l")) {
                    this.model.load();
                    update(this.model, null);
                } else if (words[0].startsWith("h")) {
                    this.model.hint();
                    update(this.model, null);
                } else if (words[0].startsWith("s")) {
                    System.out.println(this.model.toString());
                } else {
                    displayHelp();
                }
            }else if(words.length > 0 && words[0].startsWith("m")){
                String direction = words[1];
                this.model.move(direction);
                update(this.model, null);
            }else{
                displayHelp();
            }
        }
    }

    /**
     * Checks if the current position is the solution and updates the 2D array of the board
     * @param o the model to update
     * @param arg always null
     */
    public void update(TipOverModel o, Object arg){
        System.out.println(o);
        if(o.configIsSolution()){
            System.out.println("You Win!");
            System.exit(1);
        }
    }

    /**
     * Initialize view method adds an observer
     */
    public void initializeView(){
        this.model.addObserver(this);
        update(this.model, null);
    }

    /**
     * Method for displaying help
     */
    public void displayHelp(){
        System.out.println("* -- current position of tipper");
        System.out.println("! -- goal position");
        System.out.println("_ -- Unmovable position");
        System.out.println("1 -- Can move to but not tip");
        System.out.println("any number greater than 1 -- can move to or tip");
        System.out.println("l(oad) -- Abandon game, and load a new game");
        System.out.println("r(eload) -- Reload with the same file");
        System.out.println(("m(ove) direction-- move in the direction given if valid"));
        System.out.println("h(int) -- moves the tipper a step towards the goal");
        System.out.println("s(how) -- displays the board");
        System.out.println("q(uit) -- terminates the program");
    }

    /**
     * The main method that creates the tipover objects based on the file given
     * @param args the file name to get the data from
     */
    public static void main( String[] args ) {
        String fileName = args[0];
        try(Scanner in = new Scanner(new File(fileName))) {
            String line = in.nextLine();
            String[] fields = line.split("\\s+");
            int numRows = Integer.valueOf(fields[0]);
            int numCols = Integer.valueOf(fields[1]);
            int[] startPos = new int[2];
            startPos[0] = Integer.valueOf(fields[2]);
            startPos[1] = Integer.valueOf(fields[3]);
            int[] endPos = new int[2];
            endPos[0] = Integer.valueOf(fields[4]);
            endPos[1] = Integer.valueOf(fields[5]);
            char[][] gameBoard = new char[numRows][numCols];
            int row = 0;
            while (row < numRows) {
                line = in.nextLine().trim();
                fields = line.split("\\s+");
                for (int i = 0; i < numCols; i++) {
                    String str = fields[i];
                    char val = str.charAt(0);
                    gameBoard[row][i] = val;
                }
                row++;
            }
            TipOverPTUI ptui = new TipOverPTUI(numRows, numCols, startPos, endPos, startPos, gameBoard);
            ptui.run();
        }catch(FileNotFoundException f){}
    }
}
