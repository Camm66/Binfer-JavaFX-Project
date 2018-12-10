/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UIComponents;

import APIComponents.APIinterface;
import APIComponents.WikipediaService;
import DBComponents.DBController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author Cam
 */
public class Main extends Application {
    Stage window;
    SearchScene scene1;
    SearchScene scene2;   
    SearchViewFactory viewFactory;
    SavedDataViewFactory viewFactory2;
    final APIinterface wikiAPI = new WikipediaService();
    final DBController dbController = new DBController();

    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;
        this.viewFactory = new SearchViewFactory(window, wikiAPI, dbController);
        this.viewFactory2 = new SavedDataViewFactory(window, dbController);
        
        scene1 = viewFactory.buildScene1();
        scene2 = viewFactory2.buildScene2();
        
        scene1.setNextScene(scene2);
        scene2.setNextScene(scene1);
        
        primaryStage.setTitle("");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
