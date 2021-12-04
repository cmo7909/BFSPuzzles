package puzzles.tipover.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import puzzles.tipover.model.TipOverConfig;
import puzzles.tipover.model.TipOverModel;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author Craig O'Connor
 * November 2021
 */
public class TipOverGUI extends Application
        implements Observer< TipOverModel, Object > {

    private TipOverModel gameModel;
    private Label topLable;
    private TipOverConfig gameBoard;
    private TipOverConfig copyBoard;

    public TipOverGUI(int rows, int cols, int[] start, int[] end, int[] current, char[][] matrix){
        this.gameBoard = new TipOverConfig(rows, cols, start, end, current, matrix);
        this.copyBoard = new TipOverConfig(rows, cols, start, end, current, matrix);
        this.gameModel = new TipOverModel(gameBoard, copyBoard);
    }

    @Override
    public void start(Stage stage) {
        this.topLable = new Label("New File Loaded.");
        stage.setTitle("Tip Over");
        //Image tipper = new Image(TipOverGUI.class.getResourceAsStream("resources" + File.separator + "tipper.png"));
        GridPane gameGrid = new GridPane();
        for(int r=0; r<gameBoard.getNumRows(); r++){
            for(int c=0; c<gameBoard.getNumCols(); c++){
                char addToGrid = gameBoard.getBoard()[r][c];
               gameGrid.add(addToGrid,c,r);
            }
        }



        stage.show();
    }

    @Override
    public void update(TipOverModel tipOverModel, Object o) {
        System.out.println("My model has changed! (DELETE THIS LINE)");
    }

    public static void main(String[] args) throws FileNotFoundException {
        String fileName = args[0];
        try (Scanner in = new Scanner(new File(fileName))) {
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
            TipOverGUI game = new TipOverGUI(numRows, numCols, startPos, endPos, startPos, gameBoard);
            Application.launch(args);
        } catch (FileNotFoundException f) {
            System.out.println("Please enter a valid file");
        }
    }
}
