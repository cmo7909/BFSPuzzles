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

    @Override
    public void start( Stage stage ) {
        stage.setTitle( "Tip Over" );
        Image spaceship = new Image(
                LunarLandingGUI.class.getResourceAsStream(
                        "resources" + File.separator + "lander.png"
                )
        );
        Button temp = new Button();
        temp.setGraphic( new ImageView( spaceship ) );
        Scene scene = new Scene( temp, 640, 480 );
        stage.setScene( scene );
        stage.show();
    }

    @Override
    public void update( LunarLandingModel lunarLandingModel, Object o ) {
        System.out.println( "My model has changed! (DELETE THIS LINE)");
    }

    public static void main( String[] args ) {
        System.err.println( "REPLACE THIS METHOD!" );
        Application.launch( args );
    }
}
