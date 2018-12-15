package Main;

import APIService.APIinterface;
import APIService.WikipediaService;
import DBComponents.DBController;
import UIComponents.GUIWindow;
import UIComponents.SavedDataViewBuilder;
import UIComponents.SearchViewBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage window;
    private APIinterface wikiAPI;
    private DBController dbController;
    private SearchViewBuilder viewFactory;
    private SavedDataViewBuilder viewFactory2;
    private GUIWindow gui;

    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;
        this.wikiAPI = new WikipediaService();
        this.dbController = new DBController();
        this.viewFactory = new SearchViewBuilder(wikiAPI, dbController);
        this.viewFactory2 = new SavedDataViewBuilder(dbController);
        this.gui = new GUIWindow(window, viewFactory, viewFactory2);
        this.viewFactory.setWindow(gui);
        this.viewFactory2.setWindow(gui);
        gui.setScene("Search");
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
