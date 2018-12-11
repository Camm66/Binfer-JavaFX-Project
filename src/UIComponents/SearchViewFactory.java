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
    ListView<Search> listView;

    SearchViewFactory(Stage window, APIinterface apiService, DBController dbController) {
        this.window = window;
        this.apiService = apiService;
        this.dbController = dbController;
        this.viewFactory = new SavedDataViewFactory(window, dbController);
    }
     
    public SearchScene buildScene() {
        BorderPane border = new BorderPane();
        SearchScene scene1 =  new SearchScene(border, 700, 450);
        
        HBox hbox = buildMenu(scene1);
        border.setTop(hbox);
    
        this.listView = new ListView<>();
        border.setCenter(listView);
        
        HBox anchor = buildAnchor();
        border.setBottom(anchor);
        
        return scene1;
    }

    private HBox buildMenu(SearchScene scene1) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #e95b4f;");
        
        //Create Search button
        Button searchBtn = new Button("Search");
        TextField searchTerm = new TextField();
        hbox.getChildren().addAll(searchTerm, searchBtn);
        
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Search> results = (ArrayList<Search>) apiService.search(searchTerm.getText());
                listView.getItems().clear();
                for(int i = 0; i < results.size(); i ++){
                    Search result = results.get(i);
                    listView.getItems().add(result);
                }
                
                listView.setCellFactory(CheckBoxListCell.forListView(new Callback<Search, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(Search item) {
                        BooleanProperty observable = new SimpleBooleanProperty();
                        observable.addListener(e -> 
                        item.getCheckbox().setSelected(!(item.getCheckbox().isSelected())));
                        return observable;
                    }
                }));

                listView.setOnMouseClicked(e -> buildListPopUp());     
            }
        });
        

        Button searchHistoryBtn = new Button("Search History");
        searchHistoryBtn.setOnAction(e -> {
            SearchScene scene2 = this.viewFactory.buildScene();
            scene2.setNextScene(scene1);
            window.setScene(scene2);
        });
        
        StackPane stack = new StackPane();
        stack.getChildren().add(searchHistoryBtn);
        stack.setAlignment(Pos.BASELINE_RIGHT);
        StackPane.setMargin(searchHistoryBtn, new Insets(0, 10, 0, 0));
        
        hbox.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
        
        return hbox;
    }
    
    private void buildListPopUp(){
        Search search = listView.getSelectionModel().getSelectedItem();
        
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);
        
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(10, 10, 10, 10));
        
        Text title = new Text(search.getTerm());
        dialogVbox.getChildren().add(title);
        
        Text response = new Text(search.getResponse());
        response.setWrappingWidth(200);
        dialogVbox.getChildren().add(response);
        
        Text source = new Text("Source: " + search.getSource());
        dialogVbox.getChildren().add(source);
        
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private HBox buildAnchor() {
        HBox anchor = new HBox();
        anchor.setPadding(new Insets(10, 7, 10, 7));
        anchor.setStyle("-fx-background-color: #0F0603");
   
        Button saveBtn = new Button("Save Results");
        saveBtn.setOnAction(e -> {
            saveItems(listView.getItems());
        });
        
        anchor.getChildren().add(saveBtn);
        return anchor;
    }
    
    private void saveItems(ObservableList<Search> items){
        ArrayList<Search> savedSearches = new ArrayList<>();
        for(Search search: items){
            if(search.getCheckbox().isSelected()){
                dbController.writeToDatabase(search);
                savedSearches.add(search);
            }
        }
        for(Search search: savedSearches){
            items.remove(search);
        }
    }

    public void setScene2(Scene scene2) {
        this.scene2 = scene2;
    }
}
