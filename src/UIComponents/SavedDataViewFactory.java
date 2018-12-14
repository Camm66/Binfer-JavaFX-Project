package UIComponents;

import DBComponents.DBController;
import Model.Search;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SavedDataViewFactory {
    private Stage window;
    private DBController dbController;
    private ObservableList<Search> searches;
    private TableView<Search> searchTable;
    
    public SavedDataViewFactory(Stage window, DBController dbController) {
        this.window = window;
        this.dbController = dbController;
    }
    
    public SearchScene buildScene() {
        BorderPane border = new BorderPane();
        SearchScene scene2 = new SearchScene(border, 700, 450);
        
        HBox hbox = buildMenu(scene2);
        border.setTop(hbox);
       
        this.searchTable = buildTable();
        border.setCenter(searchTable);
       
        return scene2;
    }
    
    private HBox buildMenu(SearchScene scene2) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #780000;");
        
        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setOnAction(e -> deleteSelected());
        
        hbox.getChildren().add(deleteBtn);

        Button backToSearchBtn = new Button("Search Again");
        backToSearchBtn.setOnAction(e -> {
            window.setScene(scene2.getNextScene());
            window.setTitle("Search");
        });
        
        StackPane stack = new StackPane();
        stack.setAlignment(Pos.BASELINE_RIGHT);
        stack.getChildren().add(backToSearchBtn);
        StackPane.setMargin(backToSearchBtn, new Insets(0, 10, 0, 0));
        
        hbox.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
        
        return hbox;
    }
    
    private void deleteSelected() {
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
   
    private TableView<Search> buildTable(){
        this.searches = dbController.getAllSearches();
        TableColumn<Search, String> termCol = new TableColumn("Title");
        termCol.setMinWidth(130);
        termCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        termCol.setSortable(true);

        TableColumn<Search, String> responseCol = new TableColumn("Summary");
        responseCol.setMinWidth(420);
        responseCol.setCellValueFactory(new PropertyValueFactory<>("summary"));
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
        table.setOnMouseClicked(e -> { 
            if (e.getClickCount() == 2) {
                buildListPopUp();
            }
        });
        return table;
    }
    
        private void buildListPopUp(){
        Search search = searchTable.getSelectionModel().getSelectedItem();
        
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);
        
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(10, 10, 10, 10));
        
        Text title = new Text(search.getTitle());
        dialogVbox.getChildren().add(title);
        
        Text response = new Text(search.getSummary());
        response.setWrappingWidth(300);
        dialogVbox.getChildren().add(response);
        
        Text source = new Text("Source: " + search.getSource());
        dialogVbox.getChildren().add(source);
        
        Scene dialogScene = new Scene(dialogVbox, 350, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
