package puzzles.tipover;

import puzzles.tipover.model.TipOverConfig;
import solver.Configuration;
import solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author Craig O'Connor
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
            while(row < numRows){
                line = in.nextLine().trim();
                fields = line.split("\\s+");
                for(int i=0; i<numCols; i++){
                    String str = fields[i];
                    char val = str.charAt(0);
                    gameBoard[row][i] = val;
                }
                row++;
            }

            TipOverConfig toSolve = new TipOverConfig(numRows, numCols, startPos, endPos, startPos, gameBoard);
            //TipOverConfig toSolveTwo = new TipOverConfig(numRows, numCols, startPos, endPos, startPos, gameBoard);

            //System.out.println(toSolve.equals(toSolveTwo));

            ArrayList<Configuration> solution = toSolve.getSolutionSteps();
            System.out.println("Total configs: " + Solver.getNumTotalConfigs());
            System.out.println("Unique configs: " + Solver.getNumUniqueConfigs());
            if(solution != null){
                for(int i = 0; i < solution.size(); i++){
                    System.out.println("Step " + i + ": \n" + solution.get(i).toString());
                }
            }
            else{
                System.out.println("No solution");
            }
        }catch(FileNotFoundException f){
            throw new FileNotFoundException();
        }

    }
}
