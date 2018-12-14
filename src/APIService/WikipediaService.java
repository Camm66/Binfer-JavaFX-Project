package APIService;

import Model.Search;
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

public class WikipediaService implements APIinterface{
    private static String source = "Wikipedia";
    
    @Override
    public List<Search> search(String searchText){
        String cleanedInput = cleanSearchInput(searchText);
        String address = "https://en.wikipedia.org/w/api.php?action=query&"
                + "prop=extracts&list=search&origin=*&srsearch=" + cleanedInput
                + "&srwhat=text&utf8=&format=json";
            
        ClientResource resource = new ClientResource(address);
        resource.accept(MediaType.APPLICATION_JSON);
        Representation jsonResponse = resource.get();
        
        ArrayList<Search> parsedResponse = parseJSON(jsonResponse);
        return parsedResponse;
}

    private String cleanSearchInput(String searchText) {
        String parsedText = searchText.replaceAll("\\s+", "%20");
        return parsedText;
    }

    private ArrayList<Search> parseJSON(Representation jsonResponse) {
        ArrayList<Search> responses = new ArrayList<>();
        
        try{
            JsonRepresentation jsonRepresentation = new JsonRepresentation(jsonResponse);
            JSONObject jsonObj = jsonRepresentation.getJsonObject();
            JSONObject jsonObj2 = jsonObj.getJSONObject("query");
            JSONArray jsonArr = (JSONArray) jsonObj2.get("search");
            
            int searchIndex = 0;
            int searchCount = 100;
            while(searchIndex < jsonArr.length() && searchCount > 0){
                JSONObject jsonObj3 = (JSONObject) jsonArr.get(searchIndex);
                String title = (String) jsonObj3.get("title");
                String response = (String) jsonObj3.get("snippet");
                responses.add(new Search(null, title, cleanSearchOutput(response), this.source));
                searchIndex++;
                searchCount--;
            }
        }catch (IOException ex) {
            Logger.getLogger(WikipediaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responses;
    }
   
    private String cleanSearchOutput(String resultText){
        String parsedText = resultText.replaceAll("(<([^>]+)>)", "");
        return parsedText;
    }
}
