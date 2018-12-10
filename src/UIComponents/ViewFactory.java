/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UIComponents;

import APIComponents.APIinterface;
import APIComponents.WikipediaService;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Cam
 */
public class ViewFactory {
    Stage window;
    Scene scene1, scene2;  
    private final APIinterface wikiAPI = new WikipediaService();
    private final DBController dbController = new DBController();

    ViewFactory(Stage window) {
        this.window = window;
    }
     
    public Scene buildScene1() {
    //Scene 1 layout
        BorderPane border = new BorderPane();
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
    //Create GridPane to display results;
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        border.setCenter(grid);
        
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Search> results = (ArrayList<Search>) 
                        wikiAPI.search(searchTerm.getText());
                
                grid.getChildren().clear();
                
                for(int i = 0; i < results.size(); i ++){
                    Search result = results.get(i);
                    
                    Label term = new Label(result.getTerm());
                    GridPane.setConstraints(term, 0, i+ 2);
                    grid.getChildren().add(term);
                    
                    Text response = new Text(result.getResponse());
                    response.setWrappingWidth(300);
                    GridPane.setConstraints(response, 2, i+ 2);
                    grid.getChildren().add(response);
               
                    Button saveBtn = new Button("Save");
                    GridPane.setConstraints(saveBtn, 5, i+2);
                    grid.getChildren().add(saveBtn);
                    saveBtn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e){
                            dbController.writeToDatabase(result);
                            saveBtn.setDisable(true);
                        }
                    });
                }
            }
        });
        searchHistoryBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                buildScene2();
                window.setScene(scene2);
            }
        });
        this.scene1 =  new Scene(border, 700, 450);
        return this.scene1;
    }

    Scene buildScene2() {
        ObservableList<Search> searches = dbController.getAllSearches();
        //Scene 2 layout
        BorderPane border = new BorderPane();
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
        backToSearchBtn.setOnAction(e -> window.setScene(scene1));
        //Create view to display data table;
        TableColumn<Search, String> termCol = new TableColumn("Search Term");
        termCol.setMinWidth(100);
        termCol.setCellValueFactory(new PropertyValueFactory<>("term"));
        termCol.setSortable(false);
        
        TableColumn<Search, String> responseCol = new TableColumn("Response");
        responseCol.setMaxWidth(300);
        responseCol.setCellValueFactory(new PropertyValueFactory<>("response"));
        responseCol.setSortable(false);
        
        TableColumn<Search, String> actionCol = new TableColumn("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<Search, String>("checkbox"));
        actionCol.setSortable(false);
        
        TableView<Search> table = new TableView();
        table.getColumns().addAll(actionCol, termCol, responseCol);
        table.setItems(searches);
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        border.setCenter(vbox);
        
        this.scene2 = new Scene(border, 700, 450);
        return scene2;
    }
}
