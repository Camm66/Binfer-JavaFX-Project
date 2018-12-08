/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxexample;

import java.io.IOException;
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
public class WikipediaService {
    public WikipediaService(){}
    
    public String search(String searchText){
        String parsedText = cleanSearchInput(searchText);
            
        String address = "https://en.wikipedia.org/w/api.php?action=query&"
                + "prop=extracts&list=search&origin=*&srsearch=" + parsedText
                + "&srwhat=text&utf8=&format=json";
            
        ClientResource resource = new ClientResource(address);
        resource.accept(MediaType.APPLICATION_JSON);
        Representation repr = resource.get();
        
        String response = "No Response"; 
        try{
            JsonRepresentation jsonRepresentation = new JsonRepresentation(repr);
            JSONObject jsonObj = jsonRepresentation.getJsonObject();
            JSONObject jsonObj2 = jsonObj.getJSONObject("query");
            JSONArray jsonArr = (JSONArray) jsonObj2.get("search");
            JSONObject jsonObj3 = (JSONObject) jsonArr.get(0);
            response = (String) jsonObj3.get("snippet");
        }catch (IOException ex) {
            Logger.getLogger(WikipediaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String parsedResponse = cleanSearchOutput(response);
        return parsedResponse;
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
