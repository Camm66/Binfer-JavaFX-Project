package Main;

import APIService.APIinterface;
import APIService.WikipediaService;
import DBComponents.DBController;
import UIComponents.SearchViewFactory;
import UIComponents.SearchScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage window;
    private SearchScene scene1;   
    private SearchViewFactory viewFactory;
    private APIinterface wikiAPI;
    private DBController dbController;

    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;
        this.wikiAPI = new WikipediaService();
        this.dbController = new DBController();
        this.viewFactory = new SearchViewFactory(window, wikiAPI, dbController);
        scene1 = viewFactory.buildScene();
        window.setTitle("Search");
        window.setScene(scene1);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
