/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UIComponents;

import DBComponents.DBController;
import DataModels.Search;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Cam
 */
public class SavedDataViewFactory {
    Stage window;
    DBController dbController;
    
    public SavedDataViewFactory(Stage window, DBController dbController) {
        this.window = window;
        this.dbController = dbController;
    }
    public SearchScene buildScene2() {
        BorderPane border = new BorderPane();
        SearchScene scene2 = new SearchScene(border, 700, 450);
        ObservableList<Search> searches = dbController.getAllSearches();
        //Scene 2 layout
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #273c75;");
        border.setTop(hbox);
        //Create Delete button
        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Search> deletedSearches = new ArrayList<>();
                for(Search search : searches){
                    if(search.getCheckbox().isSelected()){
                        dbController.deleteFromDatabase(search);
                        deletedSearches.add(search);
                    }
                }
                for(Search search: deletedSearches){
                    searches.remove(search);
                }
            }
        });
        hbox.getChildren().add(deleteBtn);
        //Add StackPane to position the Search Again Button
        Button backToSearchBtn = new Button("Search Again");
        StackPane stack = new StackPane();
        stack.getChildren().add(backToSearchBtn);
        stack.setAlignment(Pos.BASELINE_RIGHT);
        StackPane.setMargin(backToSearchBtn, new Insets(0, 10, 0, 0));
        hbox.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
        backToSearchBtn.setOnAction(e -> window.setScene(scene2.getNextScene()));
        //Create view to display data table;
        TableColumn<Search, String> termCol = new TableColumn("Search Term");
        termCol.setMinWidth(100);
        termCol.setCellValueFactory(new PropertyValueFactory<>("term"));
        termCol.setSortable(false);
        
        TableColumn<Search, String> responseCol = new TableColumn("Response");
        responseCol.setMinWidth(400);
        responseCol.setCellValueFactory(new PropertyValueFactory<>("response"));
        responseCol.setSortable(false);
        
        TableColumn<Search, String> sourceCol = new TableColumn("Source");
        sourceCol.setMinWidth(100);
        sourceCol.setCellValueFactory(new PropertyValueFactory<>("source"));
        sourceCol.setSortable(false);
        
        TableColumn<Search, String> actionCol = new TableColumn("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
        actionCol.setSortable(false);
        
        TableView<Search> table = new TableView();
        table.getColumns().addAll(actionCol, termCol, responseCol, sourceCol);
        table.setItems(searches);
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        border.setCenter(vbox);
       
        return scene2;
    }
}
