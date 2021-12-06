package puzzles.lunarlanding.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Observer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * The graphical user interface to display a graphical version of the game
 * @author Andrew Moulton
 * November 2021
 */
public class LunarLandingGUI extends Application
        implements Observer< LunarLandingModel, Object > {

    private LunarLandingModel gameModel;
    private Label topLabel;
    private LunarLandingConfig mainConfig;
    private LunarLandingConfig copyConfig;
    private GridPane gameGrid;
    private Desktop desktop = Desktop.getDesktop();

    /**
     * Gets the LunarLanding object's data in order to create the objects necessary for the GUI
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
            this.mainConfig = new LunarLandingConfig(numRows, numCols, landerCoords, explorerCoords, gameBoard);
            this.copyConfig = new LunarLandingConfig(numRows, numCols, landerCoords, explorerCoords, gameBoard);
            this.gameModel = new LunarLandingModel(this.mainConfig, this.copyConfig);
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
    public void start( Stage stage ) {
        final FileChooser fileChooser = new FileChooser();
        stage.setTitle( "Lunar Landing" );
        this.gameModel.addObserver(this);
        this.topLabel = new Label("New File Loaded.");
        BorderPane layout = new BorderPane();
        BorderPane arrows = new BorderPane();
        BorderPane functions = new BorderPane();
        layout.setTop(topLabel);
        this.gameGrid = new GridPane();
        GridPane interactionGrid = new GridPane();
        layout.setRight(functions);
        functions.setTop(arrows);
        functions.setBottom(interactionGrid);

        for(int r=0; r<mainConfig.getNumRows(); r++){
            for(int c=0; c<mainConfig.getNumCols(); c++){
                String boardString = String.valueOf(mainConfig.getBoard()[r][c]);
                Button button = new Button();
                if(boardString.equals("0")){
                    button.setStyle("-fx-background-color: gray;");
                    button.setPrefSize(100, 100);
                }else if(boardString.equals("E")){
//                    Image explorer = new Image(getClass().getResourceAsStream("resources/explorer.png"));
                    Image explorer = new Image(getClass().getResourceAsStream("resources/sus.png"));
                    button.setGraphic(new ImageView(explorer));
                    button.setPrefSize(100, 100);
                }else{
                    //Set graphic to the image that corresponds to the boardString
//                    button.setGraphic();
                    button.setPrefSize(100, 100);
                }
                gameGrid.add(button,c,r);
            }
        }
        layout.setLeft(gameGrid);

        ArrayList<Button> buttonsList = new ArrayList<>();

        Button load = new Button("Load");
        load.setPrefSize(128, 36);
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
                    GUIReload(args);
                }

            }
        });

        buttonsList.add(load);

        Button reload = new Button("Reload");
        reload.setPrefSize(128, 36);
        reload.setOnAction(actionEvent -> {
            topLabel.setText(" ");
            gameModel.reload(copyConfig);
            update(gameModel, null);
        });
        buttonsList.add(reload);

        Button hint = new Button("Hint");
        hint.setPrefSize(128, 36);
        hint.setOnAction(actionEvent -> {
            topLabel.setText(" ");
            if(!gameModel.hint()){
                topLabel.setText("Hint Cannot find a solution\nplease press reload");
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
        north.setPrefSize(128, 36);
        north.setOnAction(actionEvent -> {
//            topLabel.setText("");
//            int row = gameModel.getCurrentConfig().getCurrentPos()[0];
//            int col = gameModel.getCurrentConfig().getCurrentPos()[1];
//            int[] pos = {row - 1, col};
//            if(!gameModel.validMove(pos)){
//                topLabel.setText("Please Select a Valid Move");
//            }
//            this.gameModel.move("n");
//            if(gameModel.getCurrentConfig().getBoard()[row][col] == '0'){
//                topLabel.setText("A Tower Has Been Tipped");
//            }
//            update(gameModel, null);
        });

        Button west = new Button("←");
        west.setPrefSize(64, 36);
        west.setOnAction(actionEvent -> {
//            topLabel.setText("");
//            int row = gameModel.getCurrentConfig().getCurrentPos()[0];
//            int col = gameModel.getCurrentConfig().getCurrentPos()[1];
//            int[] pos = {row, col - 1};
//            if(!(gameModel.validMove(pos))){
//                topLabel.setText("Please Select a Valid Move");
//            }
//            this.gameModel.move("w");
//            if(gameModel.getCurrentConfig().getBoard()[row][col] == '0'){
//                topLabel.setText("A Tower Has Been Tipped");
//            }
//            update(gameModel, null);
        });

        Button south = new Button("↓");
        south.setPrefSize(128, 36);
        south.setOnAction(actionEvent -> {
//            topLabel.setText("");
//            int row = gameModel.getCurrentConfig().getCurrentPos()[0];
//            int col = gameModel.getCurrentConfig().getCurrentPos()[1];
//            int[] pos = {row + 1, col};
//            if(!(gameModel.validMove(pos))){
//                topLabel.setText("Please Select a Valid Move");
//            }
//            this.gameModel.move("s");
//            if(gameModel.getCurrentConfig().getBoard()[row][col] == '0'){
//                topLabel.setText("A Tower Has Been Tipped");
//            }
//            update(gameModel, null);
        });

        Button east = new Button("→");
        east.setPrefSize(64, 36);
        east.setOnAction(actionEvent -> {
//            topLabel.setText("");
//            int row = gameModel.getCurrentConfig().getCurrentPos()[0];
//            int col = gameModel.getCurrentConfig().getCurrentPos()[1];
//            int[] pos = {row, col + 1};
//            if(!gameModel.validMove(pos)){
//                topLabel.setText("Please Select a Valid Move");
//            }
//            this.gameModel.move("e");
//            if(gameModel.getCurrentConfig().getBoard()[row][col] == '0'){
//                topLabel.setText("A Tower Has Been Tipped");
//            }
//            update(gameModel,null);
        });

        arrows.setTop(north);
        arrows.setLeft(west);
        arrows.setBottom(south);
        arrows.setRight(east);

        Scene mainScene = new Scene(layout);
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.show();
//        stage.show();
//        Image spaceship = new Image(LunarLandingGUI.class.getResourceAsStream("resources/lander.png"));
//        Button temp = new Button();
//        temp.setGraphic( new ImageView( spaceship ) );
//        Scene scene = new Scene( temp, 640, 480 );
//        stage.setScene( scene );
//        stage.show();
    }

    /**
     * Updates the display after an action has occurred
     * @param lunarLandingModel the LunarLanding model to update
     * @param o always null
     */
    @Override
    public void update( LunarLandingModel lunarLandingModel, Object o ) {
        for (int r = 0; r < gameModel.getCurrentConfig().getNumRows(); r++) {
            for (int c = 0; c < gameModel.getCurrentConfig().getNumCols(); c++) {
                Button button = new Button();
                if (gameModel.getCurrentConfig().getBoard()[r][c] == '0') {
                    button.setStyle("-fx-background-color: gray;" + "-fx-text-fill: white");
                }// else if (r == gameModel.getCurrentConfig().getGoalCords()[0] && c == gameModel.getCurrentConfig().getGoalCords()[1]) {
//                    button.setStyle("-fx-background-color: blue;" + "-fx-text-fill: white");
//                }
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
    public static void main( String[] args ) throws FileNotFoundException{
        Application.launch( args );
    }

    /**
     * Once the reload button is pressed init cannot be called again so to work around this
     * the method is used to create the objects again then just call start at the end.
     * @param args the filename to use to get data from
     */
    public void GUIReload(String[] args){
        //TODO write method
    }
}
