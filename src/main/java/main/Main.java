package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main entry class which starts the application.
 */
public class Main extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage){
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource("/itcua.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.setTitle("ITCua - Главная");
        primaryStage.getIcons().add(new Image("itc_icon.png"));
        primaryStage.setScene(new Scene(root,1280,700));
        primaryStage.setMinWidth(600);
        //primaryStage.setFullScreen(true);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }

    /**
     * The main method launches the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
