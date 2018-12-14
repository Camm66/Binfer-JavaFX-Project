package Model;

import javafx.scene.control.CheckBox;

public class Search {
    private Long id;
    private String title;
    private String summary;
    private String source;
    private CheckBox checkbox;
    
    public Search(Long id, String title, String summary, String source){
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.source = source;
        this.checkbox = new CheckBox();
    }
    
    public Long getID(){
        return this.id;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public String getSummary(){
        return this.summary;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public CheckBox getCheckbox(){
        return this.checkbox;
    }
    
    public void setCheckbox(CheckBox checkbox){
        this.checkbox = checkbox;
    }
    
    @Override
    public String toString(){
        return this.getTitle();
    }
}
