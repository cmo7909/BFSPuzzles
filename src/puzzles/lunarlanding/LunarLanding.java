package puzzles.lunarlanding;

import puzzles.lunarlanding.model.LunarLandingConfig;
import solver.Configuration;
import solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author Andrew Moulton
 * November 2021
 */
public class LunarLanding {

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
     */

    public static void main( String[] args ) throws FileNotFoundException{
        String fileName = args[0];
        try(Scanner in = new Scanner(new File(fileName))){
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
            while(in.hasNextLine()){
                line = in.nextLine().trim();
                if(line.equals("")){
                    break;
                }
                fields = line.split("\\s+");
                gameBoard[Integer.parseInt(fields[1])][Integer.parseInt(fields[2])] = fields[0].charAt(0);
            }
            //find coordinates of explorer
            int[] explorerCoords = new int[2];
            for(int x = 0; x < gameBoard.length; x++){
                for(int y = 0; y < gameBoard[0].length; y++){
                    if(gameBoard[x][y] == 'E'){
                        explorerCoords[0] = x;
                        explorerCoords[1] = y;
                    }
                }
            }

            LunarLandingConfig toSolve = new LunarLandingConfig(numRows, numCols, landerCoords, explorerCoords, gameBoard);

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
