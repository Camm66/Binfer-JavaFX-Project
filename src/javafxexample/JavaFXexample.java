/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxexample;

import java.sql.Timestamp;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Cam
 */
public class JavaFXexample extends Application {
    private final WikipediaService wikiAPI = new WikipediaService();
    private GridPane grid;
    private DBController dbController;
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Search");
        TextField searchTerm = new TextField ();
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String result = wikiAPI.search(searchTerm.getText());
                logData(searchTerm.getText(), result);
                grid.add(new Label(searchTerm.getText()), 0, 2);
                grid.add(new Label(result), 0, 3);
            }
        });
        
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        GridPane.setConstraints(btn, 1, 0);
        grid.getChildren().add(btn);
        GridPane.setConstraints(searchTerm, 0, 0);
        grid.getChildren().add(searchTerm);
        
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Search UI");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        this.dbController = new DBController();
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void logData(String searchText, String result) {
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        System.out.println(searchText);
        System.out.println(result);
        System.out.println(timeStamp);
    }
    
}
