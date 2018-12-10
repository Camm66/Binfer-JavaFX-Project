/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UIComponents;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author Cam
 */
public class Main extends Application {
    Stage window;
    Scene scene1, scene2;   
    ViewFactory viewFactory;
 
    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;
        this.viewFactory = new ViewFactory(window);
        
        scene1 = viewFactory.buildScene1();
        scene2 = viewFactory.buildScene2();
        
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
