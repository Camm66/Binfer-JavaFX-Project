package DBComponents;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import Model.Search;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class DBController {
    private final String userName = "xxxxxx";
    private final String password = "xxxxxx";
    private final String dbUrl = "jdbc:derby://localhost:1527/SavedSearches";
    
    private static final String INSERT_SQL = 
            "INSERT INTO xxxxxx.searches(id, title, summary, source) "
          + "values(?, ?, ?, ?) ";
     
    private static final String DELETE_SQL = 
            "DELETE FROM xxxxxx.searches"
         + " WHERE id = ?";
    
    private static final String FIND_ALL_SQL = 
            "SELECT * "
          + "FROM xxxxxx.searches";
     
    private static final String GET_ID_SQL = 
            "SELECT * "
          + "FROM xxxxxx.searches "
          + " ORDER BY id DESC";
    
    private Connection getConnection() {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(this.dbUrl, connectionProps);
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
     
    public void writeToDatabase(Search search){
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
            ps.setLong(1, getNextID(conn));
            ps.setString(2, search.getTitle());
            ps.setString(3, search.getSummary());
            ps.setString(4, search.getSource());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteFromDatabase(Search search) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_SQL);
            ps.setLong(1, search.getID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ObservableList<Search> getAllSearches() {
        ObservableList<Search> result = FXCollections.observableArrayList();
        Statement stmt = null;
        try {
            Connection conn = getConnection();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            rs.next();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String summary = rs.getString("summary");
                String source = rs.getString("source");
                result.add(new Search(id, title, summary, source));
            }    
        } catch (SQLException e ) {
            System.out.println(e);
        }
        return result;
    }

    private long getNextID(Connection conn) {
        Long id = null;
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(GET_ID_SQL);   
            rs.next();
            id = rs.getLong(1);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return id + 1;
    }
}
