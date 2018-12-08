/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxexample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;

/**
 *
 * @author Cam
 */
public final class DBController {
    private final String userName = "cammorales93";
    private String password = "Crushed66cc?";
    private String dbName = "//localhost:1527/SearchResults";
    private String dbms = "derby";
    
    public DBController(){
        try {
            getConnectionToDatabase();
        } catch (SQLException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Connection getConnectionToDatabase() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        // Using a driver manager:

      if (this.dbms.equals("derby")) {
        conn = DriverManager.getConnection("jdbc:" + dbms + ":" 
                    + dbName, connectionProps);
      }
      System.out.println("Connected to database");
      return conn;
    }

    public static void viewTable(Connection con, String dbName) {
        Statement stmt = null;
        String query = "select * " +
                   "from " + dbName + ".searches";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("term");
                String search = rs.getString("response");
                String date = rs.getString("timestamp");
                System.out.println(id + "\t" + title +
                               "\t" + search + "\t" + date);
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } finally {
            if (stmt != null) { 
                try { 
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
