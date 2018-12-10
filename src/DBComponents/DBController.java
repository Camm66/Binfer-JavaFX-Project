/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import DataModels.Search;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Cam
 */
public final class DBController implements DBinterface{
    private final String userName = "cammorales93";
    private String password = "Crushed66cc?";
    private String dbUrl = "jdbc:derby://localhost:1527/SearchResults";
    
    private static String INSERT_SQL = "INSERT INTO "
             + "cammorales93.searches(id, term, response) values(?, ?, ?) ";
     
    private static final String DELETE_SQL = "DELETE FROM cammorales93.searches"
                                              + " WHERE id = ?";
    
    private static final String FIND_ALL_SQL = "SELECT * FROM cammorales93.searches";
     
    private static final String GET_ID_SQL = "SELECT * FROM cammorales93.searches "
                                             + " ORDER BY id DESC";
    
    public DBController(){}
    
    public void writeToDatabase(Search search){
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(this.INSERT_SQL,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, getLastID(conn));
            ps.setString(2, search.getTerm());
            ps.setString(3, search.getResponse());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteFromDatabase(Search search) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_SQL,
                    Statement.RETURN_GENERATED_KEYS);
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
                String term = rs.getString("term");
                String response = rs.getString("response");
                result.add(new Search(id, term, response));
            }    
        } catch (SQLException e ) {
            System.out.println(e);
        }
        return result;
    }

    private long getLastID(Connection conn) {
        Long id = null;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(GET_ID_SQL);   
            rs.next();
            id = rs.getLong(1);
            System.out.println(id + 1);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return id + 1;
    }
    
    private Connection getConnection() {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        try {
            conn = DriverManager.getConnection(this.dbUrl, connectionProps);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return conn;
    }
}
