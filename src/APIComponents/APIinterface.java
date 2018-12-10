/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APIComponents;

import DataModels.Search;
import java.util.List;

/**
 *
 * @author Cam
 */
public interface APIinterface {
    List<Search> search(String searchText);
}
