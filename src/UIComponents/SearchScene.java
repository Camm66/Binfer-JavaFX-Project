package UIComponents;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class SearchScene extends Scene{
    private Scene nextScene; 
    
    public SearchScene(Parent root, double width, double height){
        super(root, width, height);
    }

    public Scene getNextScene() {
        return nextScene;
    }

    public void setNextScene(Scene nextScene) {
        this.nextScene = nextScene;
    }
}
