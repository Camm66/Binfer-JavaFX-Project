package APIService;

import Model.Search;
import java.util.List;

public interface APIinterface {
    List<Search> search(String searchText);
}
