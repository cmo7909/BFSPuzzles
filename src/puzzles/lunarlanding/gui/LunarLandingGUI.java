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
    private ArrayList<String> robotFiles = new ArrayList<>();

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
        fileChooser.setInitialDirectory(new File("data/lunarlanding"));
    }

    private void fillRobotFiles(){
        robotFiles.add("resources/robot-blue.png");
        robotFiles.add("resources/robot-green.png");
        robotFiles.add("resources/robot-lightblue.png");
        robotFiles.add("resources/robot-orange.png");
        robotFiles.add("resources/robot-pink.png");
        robotFiles.add("resources/robot-purple.png");
        robotFiles.add("resources/robot-white.png");
        robotFiles.add("resources/robot-yellow.png");
    }

    /**
     * The setup method for the gui and its components
     * @param stage the stage to add the GUI components to
     */
    @Override
    public void start( Stage stage ) {
        fillRobotFiles();
        ArrayList<String> robotFilesCopy = new ArrayList<>();
        for(int i = 0; i < robotFiles.size(); i++){
            robotFilesCopy.add(robotFiles.get(i));
        }
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
                button.setPrefSize(100, 100);
                if(!boardString.equals("0")) {
                    int finalR = r;
                    int finalC = c;
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        /**
                         * Handles what happens after the user clicks on a figure
                         * @param e the event occurring
                         */
                        @Override
                        public void handle(final ActionEvent e) {
                            gameModel.choose(finalR, finalC);
                        }
                    });
                }
                if(mainConfig.getLanderCoords()[0] == r && mainConfig.getLanderCoords()[1] == c){
                    Image lander = new Image(getClass().getResourceAsStream("resources/lander.png"));
                    button.setGraphic(new ImageView(lander));
                }if(boardString.equals("0")){
                    button.setStyle("-fx-background-color: gray;");
                }else if(boardString.equals("E")){
                    Image explorer = new Image(getClass().getResourceAsStream("resources/sus.png"));
                    button.setGraphic(new ImageView(explorer));
                }else if(boardString.equals("B")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(0)))));
                }else if(boardString.equals("G")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(1)))));
                }else if(boardString.equals("L")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(2)))));
                }else if(boardString.equals("O")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(3)))));
                }else if(boardString.equals("P")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(4)))));
                }else if(boardString.equals("W")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(6)))));
                }else if(boardString.equals("Y")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(7)))));
                }else{
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFilesCopy.remove(0)))));
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
                topLabel.setText("Unsolvable Board");
            }
            update(gameModel, null);
        });
        buttonsList.add(hint);

        for(int i=0; i<3; i++){
            for(int j=0; j<1; j++){
                interactionGrid.add(buttonsList.get(i), j, i);
            }
        }
        Button north = new Button("???");
        north.setPrefSize(128, 36);
        north.setOnAction(actionEvent -> {
            topLabel.setText("");
            int[] selected = gameModel.getSelectedCoords();
            boolean canGoNorth = gameModel.getCurrentConfig().getMoveDirections(selected[0], selected[1]).contains("N");
            if(!canGoNorth){
                topLabel.setText("Please Select a Valid Move");
            }
            this.gameModel.go("N");
            update(gameModel, null);
        });

        Button west = new Button("???");
        west.setPrefSize(64, 36);
        west.setOnAction(actionEvent -> {
            topLabel.setText("");
            int[] selected = gameModel.getSelectedCoords();
            boolean canGoWest = gameModel.getCurrentConfig().getMoveDirections(selected[0], selected[1]).contains("W");
            if(!canGoWest){
                topLabel.setText("Please Select a Valid Move");
            }
            this.gameModel.go("W");
            update(gameModel, null);
        });

        Button south = new Button("???");
        south.setPrefSize(128, 36);
        south.setOnAction(actionEvent -> {
            topLabel.setText("");
            int[] selected = gameModel.getSelectedCoords();
            boolean canGoSouth = gameModel.getCurrentConfig().getMoveDirections(selected[0], selected[1]).contains("S");
            if(!canGoSouth){
                topLabel.setText("Please Select a Valid Move");
            }
            this.gameModel.go("S");
            update(gameModel, null);
        });

        Button east = new Button("???");
        east.setPrefSize(64, 36);
        east.setOnAction(actionEvent -> {
            topLabel.setText("");
            int[] selected = gameModel.getSelectedCoords();
            boolean canGoEast = gameModel.getCurrentConfig().getMoveDirections(selected[0], selected[1]).contains("E");
            if(!canGoEast){
                topLabel.setText("Please Select a Valid Move");
            }
            this.gameModel.go("E");
            update(gameModel, null);
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
     * Updates the display after an action has occurred
     * @param lunarLandingModel the LunarLanding model to update
     * @param o always null
     */
    @Override
    public void update( LunarLandingModel lunarLandingModel, Object o ) {
        ArrayList<String> robotFilesCopy = new ArrayList<>();
        for(int i = 0; i < robotFiles.size(); i++){
            robotFilesCopy.add(robotFiles.get(i));
        }
        for (int r = 0; r < gameModel.getCurrentConfig().getNumRows(); r++) {
            for (int c = 0; c < gameModel.getCurrentConfig().getNumCols(); c++) {
                String boardString = String.valueOf(gameModel.getCurrentConfig().getBoard()[r][c]);
                Button button = new Button();
                button.setPrefSize(100, 100);
                if(!boardString.equals("0")) {
                    int finalR = r;
                    int finalC = c;
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        /**
                         * Handles what happens after the user clicks on a figure
                         * @param e the event occurring
                         */
                        @Override
                        public void handle(final ActionEvent e) {
                            gameModel.choose(finalR, finalC);
                        }
                    });
                }
                if(gameModel.getCurrentConfig().getLanderCoords()[0] == r && gameModel.getCurrentConfig().getLanderCoords()[1] == c){
                    Image lander = new Image(getClass().getResourceAsStream("resources/lander.png"));
                    button.setGraphic(new ImageView(lander));
                }if(boardString.equals("0")){
                    button.setStyle("-fx-background-color: gray;");
                }else if(boardString.equals("E")){
                    Image explorer = new Image(getClass().getResourceAsStream("resources/sus.png"));
                    button.setGraphic(new ImageView(explorer));
                }else if(boardString.equals("B")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(0)))));
                }else if(boardString.equals("G")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(1)))));
                }else if(boardString.equals("L")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(2)))));
                }else if(boardString.equals("O")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(3)))));
                }else if(boardString.equals("P")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(4)))));
                }else if(boardString.equals("W")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(6)))));
                }else if(boardString.equals("Y")){
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFiles.get(7)))));
                }else{
                    button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(robotFilesCopy.remove(0)))));
                }
                gameGrid.add(button,c,r);
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
        String fileName = "data/lunarlanding/" + args[0];
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
            Stage newStage = new Stage();
            start(newStage);
        } catch (FileNotFoundException f){}
    }
}
