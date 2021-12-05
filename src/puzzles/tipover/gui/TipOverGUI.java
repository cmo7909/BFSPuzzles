package puzzles.tipover.gui;

import java.awt.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.tipover.model.TipOverConfig;
import puzzles.tipover.model.TipOverModel;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The graphical user interface to display a graphical version of the game
 * @author Craig O'Connor
 * November 2021
 */
public class TipOverGUI extends Application
        implements Observer< TipOverModel, Object > {

    private TipOverModel gameModel;
    private Label topLabel;
    private TipOverConfig mainConfig;
    private TipOverConfig copyConfig;
    private GridPane gameGrid;
    private Desktop desktop = Desktop.getDesktop();

    /**
     * Gets the tipover objects data in order to create the objects necessary for the GUI
     * @throws Exception
     */
    @Override
    public void init() throws Exception{
        List<String> args = getParameters().getRaw();
        String fileName = args.get(0);
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
            this.mainConfig = new TipOverConfig(numRows, numCols, startPos, endPos, startPos, gameBoard);
            this.copyConfig = new TipOverConfig(numRows, numCols, startPos, endPos, startPos, gameBoard);
            this.gameModel = new TipOverModel(this.mainConfig, this.copyConfig);
        } catch (FileNotFoundException f){
            System.out.println("Please enter a valid file");
        }
    }

    /**
     * Configures the file chooser with the desired beginning directory
     * @param fileChooser the file chooser object
     */
     private static void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Choose a text file to load");
        fileChooser.setInitialDirectory(new File("data/tipover"));
     }

    /**
     * The setup method for the gui and its components
     * @param stage the stage to add the GUI components to
     */
    @Override
    public void start(Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        Label bottomLable = new Label("Red = Starting Position, Blue = Goal");
        this.gameModel.addObserver(this);
        this.topLabel = new Label("New File Loaded.");
        stage.setTitle("Tip Over");
        BorderPane layout = new BorderPane();
        BorderPane arrows = new BorderPane();
        BorderPane functions = new BorderPane();
        layout.setTop(topLabel);
        this.gameGrid = new GridPane();
        GridPane interactionGrid = new GridPane();
        layout.setRight(functions);
        functions.setTop(arrows);
        functions.setBottom(interactionGrid);
        layout.setBottom(bottomLable);

        for(int r=0; r<mainConfig.getNumRows(); r++){
            for(int c=0; c<mainConfig.getNumCols(); c++){
                Button button = new Button(String.valueOf(mainConfig.getBoard()[r][c]));
                if(r == mainConfig.getCurrentPos()[0] && c == mainConfig.getCurrentPos()[1]){
                    button.setStyle("-fx-background-color: red;" + "-fx-text-fill: white");
                }else if(r == mainConfig.getGoalCords()[0] && c == mainConfig.getGoalCords()[1]){
                    button.setStyle("-fx-background-color: blue;" + "-fx-text-fill: white");
                }
                gameGrid.add(button,c,r);
            }
        }
        layout.setLeft(gameGrid);

        ArrayList<Button> buttonsList = new ArrayList<>();

        Button load = new Button("Load");
        load.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Handles what happens after the user selects a file
             * @param e the event occurring
             */
            @Override
            public void handle(final ActionEvent e) {
                configureFileChooser(fileChooser);
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    String fileName = file.getName();
                    String[] args = {fileName};
                    stage.close();
                    forReload(args);
                }

            }
        });

        
        buttonsList.add(load);

        Button reload = new Button("Reload");
        reload.setOnAction(actionEvent -> {
            topLabel.setText(" ");
            gameModel.reload(copyConfig);
            update(gameModel, null);
        });
        buttonsList.add(reload);

        Button hint = new Button("Hint");
        hint.setOnAction(actionEvent -> {
            topLabel.setText(" ");
            if(!gameModel.hint()){
                topLabel.setText("Hint Cannot find a solution,\nplease press reload");
            }
            update(gameModel, null);
        });
        buttonsList.add(hint);

        for(int i=0; i<3; i++){
            for(int j=0; j<1; j++){
                interactionGrid.add(buttonsList.get(i), j, i);
            }
        }
        Button north = new Button("↑");
        north.setOnAction(actionEvent -> {
            topLabel.setText("");
            int row = gameModel.getCurrentConfig().getCurrentPos()[0];
            int col = gameModel.getCurrentConfig().getCurrentPos()[1];
            int[] pos = {row - 1, col};
            if(!gameModel.validMove(pos)){
                topLabel.setText("Please Select a Valid Move");
            }
           this.gameModel.move("n");
           if(gameModel.getCurrentConfig().getBoard()[row][col] == '0'){
               topLabel.setText("A Tower Has Been Tipped");
           }
           update(gameModel, null);
        });

        Button west = new Button("←");
        west.setOnAction(actionEvent -> {
            topLabel.setText("");
            int row = gameModel.getCurrentConfig().getCurrentPos()[0];
            int col = gameModel.getCurrentConfig().getCurrentPos()[1];
            int[] pos = {row, col - 1};
            if(!(gameModel.validMove(pos))){
                topLabel.setText("Please Select a Valid Move");
            }
            this.gameModel.move("w");
            if(gameModel.getCurrentConfig().getBoard()[row][col] == '0'){
                topLabel.setText("A Tower Has Been Tipped");
            }
            update(gameModel, null);
        });

        Button south = new Button("↓");
        south.setOnAction(actionEvent -> {
            topLabel.setText("");
            int row = gameModel.getCurrentConfig().getCurrentPos()[0];
            int col = gameModel.getCurrentConfig().getCurrentPos()[1];
            int[] pos = {row + 1, col};
            if(!(gameModel.validMove(pos))){
                topLabel.setText("Please Select a Valid Move");
            }
            this.gameModel.move("s");
            if(gameModel.getCurrentConfig().getBoard()[row][col] == '0'){
                topLabel.setText("A Tower Has Been Tipped");
            }
            update(gameModel, null);
        });

        Button east = new Button("→");
        east.setOnAction(actionEvent -> {
            topLabel.setText("");
            int row = gameModel.getCurrentConfig().getCurrentPos()[0];
            int col = gameModel.getCurrentConfig().getCurrentPos()[1];
            int[] pos = {row, col + 1};
            if(!gameModel.validMove(pos)){
                topLabel.setText("Please Select a Valid Move");
            }
            this.gameModel.move("e");
            if(gameModel.getCurrentConfig().getBoard()[row][col] == '0'){
                topLabel.setText("A Tower Has Been Tipped");
            }
            update(gameModel,null);
        });

        arrows.setTop(north);
        arrows.setLeft(west);
        arrows.setBottom(south);
        arrows.setRight(east);

        Scene mainScene = new Scene(layout);
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Method that updates the display after an action has occurred
     * @param tipOverModel the tipover model to update
     * @param o always null
     */
    @Override
    public void update(TipOverModel tipOverModel, Object o) {

            for (int r = 0; r < gameModel.getCurrentConfig().getNumRows(); r++) {
                for (int c = 0; c < gameModel.getCurrentConfig().getNumCols(); c++) {
                    Button button = new Button(String.valueOf(gameModel.getCurrentConfig().getBoard()[r][c]));
                    if (r == gameModel.getCurrentConfig().getCurrentPos()[0] && c == gameModel.getCurrentConfig().getCurrentPos()[1]) {
                        button.setStyle("-fx-background-color: red;" + "-fx-text-fill: white");
                    } else if (r == gameModel.getCurrentConfig().getGoalCords()[0] && c == gameModel.getCurrentConfig().getGoalCords()[1]) {
                        button.setStyle("-fx-background-color: blue;" + "-fx-text-fill: white");
                    }
                    gameGrid.add(button, c, r);
                }
            }
        if(gameModel.getCurrentConfig().isSolution()) {
            topLabel.setText("You Win!");
        }
    }

    /**
     * Main method to start the process
     * @param args the file to get the data from in init()
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
            Application.launch(args);
    }

    /**
     * Once the reload button is pressed init cannot be called again so to work around this
     * the method is used to create the objects again then just call start at the end.
     * @param args the filename to use to get data from
     */
    public void forReload(String[] args){
        String fileName = "data/tipover/" + args[0];
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
            this.mainConfig = new TipOverConfig(numRows, numCols, startPos, endPos, startPos, gameBoard);
            this.copyConfig = new TipOverConfig(numRows, numCols, startPos, endPos, startPos, gameBoard);
            this.gameModel = new TipOverModel(this.mainConfig, this.copyConfig);
            Stage newStage = new Stage();
            start(newStage);
        } catch (FileNotFoundException f){}
    }
}
