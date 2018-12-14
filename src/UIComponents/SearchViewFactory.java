package UIComponents;

import APIService.APIinterface;
import DBComponents.DBController;
import Model.Search;
import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SearchViewFactory {
    private Stage window;  
    private APIinterface apiService;
    private final DBController dbController;
    private final SavedDataViewFactory viewFactory;
    private ListView<Search> listView;

    public SearchViewFactory(Stage window, APIinterface apiService, DBController dbController) {
        this.window = window;
        this.apiService = apiService;
        this.dbController = dbController;
        this.viewFactory = new SavedDataViewFactory(window, dbController);
    }
     
    public SearchScene buildScene() {
        BorderPane rootPane = new BorderPane();
        SearchScene scene1 =  new SearchScene(rootPane, 700, 450);
        
        HBox hbox = buildMenu(scene1);
        rootPane.setTop(hbox);
    
        this.listView = new ListView<>();
        rootPane.setCenter(listView);
        
        HBox anchor = buildAnchor();
        rootPane.setBottom(anchor);
        return scene1;
    }

    private HBox buildMenu(SearchScene scene1) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #780000;");
        
        Button searchBtn = new Button("Search");
        TextField searchTerm = new TextField();
        searchBtn.setOnAction(e -> buildListView(searchTerm));
        hbox.getChildren().addAll(searchTerm, searchBtn);

        Button viewSavedBtn = new Button("View Saved");
        viewSavedBtn.setOnAction(e -> {
            SearchScene scene2 = this.viewFactory.buildScene();
            scene2.setNextScene(scene1);
            window.setTitle("View Saved");
            window.setScene(scene2);
        });
        StackPane stack = new StackPane();
        stack.getChildren().add(viewSavedBtn);
        stack.setAlignment(Pos.BASELINE_RIGHT);
        StackPane.setMargin(viewSavedBtn, new Insets(0, 10, 0, 0));
        hbox.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
        
        return hbox;
    }
    
    private void buildListView(TextField searchTerm){
        listView.getItems().clear();
        
        ArrayList<Search> results = 
                (ArrayList<Search>) apiService.search(searchTerm.getText());
        
        for(int i = 0; i < results.size(); i ++){
            Search result = results.get(i);
            listView.getItems().add(result);
        }
            
        listView.setCellFactory(CheckBoxListCell.forListView(
                new Callback<Search, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Search item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener(e -> 
                item.getCheckbox().setSelected(!(item.getCheckbox().isSelected())));
                return observable;
                }
            }));
                
        listView.setOnMouseClicked(e -> { 
            if (e.getClickCount() == 2) {
                buildListPopUp();
            }
        });  
    }
    
    private void buildListPopUp(){
        Search search = listView.getSelectionModel().getSelectedItem();
        
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

    private HBox buildAnchor() {
        HBox anchor = new HBox();
        anchor.setPadding(new Insets(10, 7, 10, 7));
        anchor.setStyle("-fx-background-color: #303030");
   
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
}
