/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APIComponents;

import DataModels.Search;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

/**
 *
 * @author Cam
 */
public class WikipediaService implements APIinterface{
    public WikipediaService(){}
    
    public List<Search> search(String searchText){
        String parsedText = cleanSearchInput(searchText);
        String address = "https://en.wikipedia.org/w/api.php?action=query&"
                + "prop=extracts&list=search&origin=*&srsearch=" + parsedText
                + "&srwhat=text&utf8=&format=json";
            
        ClientResource resource = new ClientResource(address);
        resource.accept(MediaType.APPLICATION_JSON);
        Representation repr = resource.get();
        
        ArrayList<Search> responses = new ArrayList<>();
        String response = "N/A"; 
        String title = "N/A";
        try{
            JsonRepresentation jsonRepresentation = new JsonRepresentation(repr);
            JSONObject jsonObj = jsonRepresentation.getJsonObject();
            JSONObject jsonObj2 = jsonObj.getJSONObject("query");
            JSONArray jsonArr = (JSONArray) jsonObj2.get("search");
            int searchIndex = 0;
            int searchCount = 100;
            while(searchIndex < jsonArr.length() && searchCount > 0){
                JSONObject jsonObj3 = (JSONObject) jsonArr.get(searchIndex);
                title = (String) jsonObj3.get("title");
                response = (String) jsonObj3.get("snippet");
                responses.add(new Search(null, title, cleanSearchOutput(response), "Wikipedia"));
                searchIndex++;
                searchCount--;
            }
        }catch (IOException ex) {
            Logger.getLogger(WikipediaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return responses;
}

    private String cleanSearchInput(String searchText) {
        String parsedText = searchText.replaceAll("\\s+", "%20");
        return parsedText;
    }
    
    private String cleanSearchOutput(String resultText){
        String parsedText = resultText.replaceAll("(<([^>]+)>)", "");
        return parsedText;
    }
}
