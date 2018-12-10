/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBComponents;

import DataModels.Search;
import javafx.collections.ObservableList;

/**
 *
 * @author Cam
 */
public interface DBinterface {
     public void writeToDatabase(Search search);
     public void deleteFromDatabase(Search search);
     public ObservableList<Search> getAllSearches();
}
