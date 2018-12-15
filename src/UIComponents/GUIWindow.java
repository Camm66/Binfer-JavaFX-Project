package UIComponents;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIWindow {
    private static Stage window;  
    private static SearchViewBuilder searchViewBuilder;
    private static SavedDataViewBuilder savedViewBuilder;
    
    public GUIWindow(Stage window, SearchViewBuilder searchViewBuilder, SavedDataViewBuilder savedDataViewBuilder){
        this.window = window;
        this.searchViewBuilder = searchViewBuilder;
        this.savedViewBuilder = savedDataViewBuilder;
    }
    
    public static void setScene(String sceneType) {
        Scene newScene = null;
        
        switch(sceneType){
            case "Search":
                newScene =  searchViewBuilder.buildScene();
                window.setTitle("Search");
            case "View Saved":
                newScene = savedViewBuilder.buildScene();
                window.setTitle("SavedView");
        }
        window.setScene(newScene);
    }
    
    public Stage getWindow(){
        return this.window;
    }
}
