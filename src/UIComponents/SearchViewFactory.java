/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UIComponents;

import APIComponents.APIinterface;
import DBComponents.DBController;
import DataModels.Search;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Cam
 */
public class SearchViewFactory {
    Stage window;
    Scene scene2;  
    private APIinterface apiService;
    private DBController dbController;

    SearchViewFactory(Stage window, APIinterface apiService, DBController dbController) {
        this.window = window;
        this.apiService = apiService;
        this.dbController = dbController;
    }
     
    public SearchScene buildScene1() {
    //Scene 1 layout
        BorderPane border = new BorderPane();
        SearchScene scene1 =  new SearchScene(border, 700, 450);
        
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #e84118;");
    //Create scene1 buttons
        Button searchBtn = new Button("Search");
        TextField searchTerm = new TextField ();
    //Add to the top of the border pane
        hbox.getChildren().addAll(searchTerm, searchBtn);
        border.setTop(hbox);
    //Add StackPane for the Search History Button
        Button searchHistoryBtn = new Button("Search History");
        StackPane stack = new StackPane();
        stack.getChildren().add(searchHistoryBtn);
        stack.setAlignment(Pos.BASELINE_RIGHT);
        StackPane.setMargin(searchHistoryBtn, new Insets(0, 10, 0, 0));
        hbox.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
    //Create ListView to display results;
        ListView<Search> listView = new ListView<>();
        border.setCenter(listView);
        
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Search> results = (ArrayList<Search>) apiService.search(searchTerm.getText());
                
                listView.getItems().clear();
               
                Button saveBtn = new Button("Save Results");
                hbox.getChildren().add(saveBtn);
                
                saveBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e){
                        dbController.writeToDatabase(null);
                        saveBtn.setDisable(true);
                    }
                });
                
                for(int i = 0; i < results.size(); i ++){
                    Search result = results.get(i);
                    listView.getItems().add(result);
                }
            }
        });
        searchHistoryBtn.setOnAction(e -> window.setScene(scene1.getNextScene()));
        return scene1;
    }

    public void setScene2(Scene scene2) {
        this.scene2 = scene2;
    }
}
