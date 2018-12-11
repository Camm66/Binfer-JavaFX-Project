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
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Cam
 */
public class SearchViewFactory {
    Stage window;
    Scene scene2;  
    private APIinterface apiService;
    private DBController dbController;
    private final SavedDataViewFactory viewFactory;

    SearchViewFactory(Stage window, APIinterface apiService, DBController dbController) {
        this.window = window;
        this.apiService = apiService;
        this.dbController = dbController;
        this.viewFactory = new SavedDataViewFactory(window, dbController);
    }
     
    public SearchScene buildScene() {
        BorderPane border = new BorderPane();
        SearchScene scene1 =  new SearchScene(border, 700, 450);
        
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #e84118;");
    //Create scene1 buttons
        Button searchBtn = new Button("Search");
        TextField searchTerm = new TextField();
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
                HBox anchor = new HBox();
                anchor.setPadding(new Insets(10, 7, 10, 7));
                anchor.setStyle("-fx-background-color: #0F0603");
                anchor.getChildren().add(saveBtn);
                border.setBottom(anchor);
                
                saveBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e){
                        saveItems(listView.getItems());
                        saveBtn.setDisable(true);
                    }
                });
                
                for(int i = 0; i < results.size(); i ++){
                    Search result = results.get(i);
                    listView.getItems().add(result);
                }
                
                listView.setCellFactory(CheckBoxListCell.forListView(new Callback<Search, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(Search item) {
                        BooleanProperty observable = new SimpleBooleanProperty();
                        observable.addListener((obs, wasSelected, isNowSelected) -> 
                        item.getCheckbox().setSelected(!(item.getCheckbox().isSelected())));
                        return observable;
                    }
                }));
                
                listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Search search = listView.getSelectionModel().getSelectedItem();
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(window);
                        VBox dialogVbox = new VBox(20);
                        dialogVbox.setPadding(new Insets(10, 10, 10, 10));
                        dialogVbox.getChildren().add(new Text("Title: " + search.getTerm()));
                        dialogVbox.getChildren().add(new Text("Response: " + search.getResponse()));
                        dialogVbox.getChildren().add(new Text("Source: " + search.getSource()));
                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                });      
            }
        });
      
        searchHistoryBtn.setOnAction(e -> {
            SearchScene scene2 = this.viewFactory.buildScene();
            scene2.setNextScene(scene1);
            window.setScene(scene2);
        });
        return scene1;
    }
    
    private void saveItems(ObservableList<Search> items){
        for(Search search: items){
            if(search.getCheckbox().isSelected()){
                dbController.writeToDatabase(search);
            }
        }
    }

    public void setScene2(Scene scene2) {
        this.scene2 = scene2;
    }
}
