/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModels;

import javafx.scene.control.CheckBox;

/**
 *
 * @author Cam
 */
public class Search {
    private Long id;
    private String term;
    private String response;
    private CheckBox checkbox;
    
    public Search(Long id, String term, String response){
        this.id = id;
        this.term = term;
        this.response = response;
        this.checkbox = new CheckBox();
    }
    
    public Long getID(){
        return this.id;
    }
    
    public String getTerm(){
        return this.term;
    }
    
    public String getResponse(){
        return this.response;
    }
    
    public CheckBox getCheckbox(){
        return this.checkbox;
    }
    
    public void setCheckbox(CheckBox checkbox){
        this.checkbox = checkbox;
    }
}
