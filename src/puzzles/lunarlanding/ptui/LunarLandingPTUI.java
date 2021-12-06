package puzzles.lunarlanding.ptui;

import puzzles.lunarlanding.LunarLanding;
import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Observer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Plain Text User Interface for playing the Lunar Landing Game
 * @author Andrew Moulton
 * November 2021
 */
public class LunarLandingPTUI implements Observer<LunarLandingModel, Object>{

    private LunarLandingModel model;
    private LunarLandingConfig copy;

    public LunarLandingPTUI(int rows, int cols, int[] lander, int[] explorer, char[][] board){
        LunarLandingConfig modelConfig = new LunarLandingConfig(rows, cols, lander, explorer, board);
        LunarLandingConfig copyModel = new LunarLandingConfig(rows, cols, lander, explorer, board);
        this.copy = copyModel;
        this.model = new LunarLandingModel(modelConfig, copyModel);
        initializeView();
    }

    /**
     * Determines what to do based on the command entered by the user
     */
    private void run(){
        Scanner in = new Scanner(System.in);
        for( ; ; ){
            System.out.print("game command: ");
            String line = in.nextLine();
            line.toLowerCase(Locale.ROOT);
            String[] words = line.split("\\s+");
            if(words.length == 1) {
                if (words[0].startsWith("q")) { //quit command
                    System.exit(1);
                } else if (words[0].startsWith("r")) { //reload command
                    this.model.reload(copy);
                    update(this.model, null);
                } else if (words[0].startsWith("l")) { //load command
                    this.model.load();
                    update(this.model, null);
                } else if (words[0].startsWith("h")) { //hint command
                    this.model.hint();
                    update(this.model, null);
                } else if (words[0].startsWith("s")) { //show command
                    System.out.println(this.model.toString());
                } else { //help command
                    displayHelp();
                }
            }else if(words.length > 0 && words[0].startsWith("c")){
                int row = Integer.parseInt(words[1]);
                int col = Integer.parseInt(words[2]);
                this.model.choose(row, col);
            }else if(words.length > 0 && words[0].startsWith("g")){
                String direction = words[1];
                this.model.go(direction.substring(0,1).toUpperCase());
                update(this.model, null);
            }else{
                displayHelp();
            }
        }
    }

    /**
     * Checks if the current position is the solution and prints the board
     * @param o the model to update
     * @param arg always null
     */
    public void update(LunarLandingModel o, Object arg){
        System.out.println(o);
        if(o.configIsSolution()){
            System.out.println("You Win!");
            System.exit(1);
        }
    }

    /**
     * Adds an observer to the model and calls update with the model
     */
    public void initializeView(){
        this.model.addObserver(this);
        update(this.model, null);
    }

    /**
     * Method for displaying help
     */
    public void displayHelp(){
        System.out.println("E -- current position of explorer");
        System.out.println("! -- current position of lander");
        System.out.println("_ -- empty position");
        System.out.println("any letter other than E -- position of a figure");
        System.out.println("l(oad) -- abandon game, and load a new game");
        System.out.println("r(eload) -- reload with the same file");
        System.out.println("c(hoose) row column -- choose which figure moves next (2 arguments)");
        System.out.println("g(o) {n(orth)|s(outh)|e(ast)|w(est)} -- tell chosen figure where to go (1 argument)");
        System.out.println("h(int) -- moves the tipper a step towards the goal");
        System.out.println("s(how) -- displays the board");
        System.out.println("q(uit) -- terminates the program");
    }

    /**
     * The main method that creates the lunarLanding objects based on the file given
     * @param args the file name to get the data from
     */
    public static void main( String[] args ) {
        String fileName = args[0];
        try(Scanner in = new Scanner(new File(fileName))) {
            String line = in.nextLine();
            String[] fields = line.split("\\s+");
            int numRows = Integer.valueOf(fields[0]);
            int numCols = Integer.valueOf(fields[1]);
            int[] landerCoords = new int[]{Integer.valueOf(fields[2]), Integer.valueOf(fields[3])};
            char [][] gameBoard = new char[numRows][numCols];
            for(int i = 0; i < gameBoard.length; i++){
                for(int j = 0; j < gameBoard[0].length; j++){
                    gameBoard[i][j] = '0';
                }
            }
            String expLine = in.nextLine().trim();
            String[] expFields = expLine.split("\\s+");
            int[] explorerCoords = new int[]{Integer.parseInt(expFields[1]), Integer.parseInt(expFields[2])};
            gameBoard[Integer.parseInt(expFields[1])][Integer.parseInt(expFields[2])] = expFields[0].charAt(0);
            while(in.hasNextLine()){
                line = in.nextLine().trim();
                if(line.equals("")){
                    break;
                }
                fields = line.split("\\s+");
                gameBoard[Integer.parseInt(fields[1])][Integer.parseInt(fields[2])] = fields[0].charAt(0);
            }
            LunarLandingPTUI ptui = new LunarLandingPTUI(numRows, numCols, landerCoords, explorerCoords, gameBoard);
            ptui.run();
        }catch(FileNotFoundException f){}
    }
}
