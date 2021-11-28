package puzzles.tipover;

import puzzles.tipover.model.TipOverConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author YOUR NAME HERE
 * November 2021
 */
public class TipOver {

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
     */

    public static void main( String[] args ) throws FileNotFoundException {
        String fileName = args[0];
        try(Scanner in = new Scanner(new File(fileName))){
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
            char [][] gameBoard = new char[numRows][numCols];
            int row = 0;
            while(in.hasNextLine()){
                line = in.nextLine();
                fields = line.split("\\s+");
                for(int i=0; i<numCols; i++){
                    String str = fields[i];
                    char val = str.charAt(0);
                    gameBoard[row][i] = val;
                }
                row++;
            }

            TipOverConfig toSolve = new TipOverConfig(numRows, numCols, startPos, endPos, startPos, gameBoard);

        }catch(FileNotFoundException f){
            throw new FileNotFoundException();
        }

    }
}
