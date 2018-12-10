/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UIComponents;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author Cam
 */
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
