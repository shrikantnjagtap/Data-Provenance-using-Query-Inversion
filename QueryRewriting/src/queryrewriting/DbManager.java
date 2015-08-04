/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package queryrewriting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Shrikant
 * Class DbManager
 * Handles all the MySQL database related activities
 */
public class DbManager {
    
    Statement stmt = null;
    
     public Connection getConnection(){
        String dbURL = "jdbc:mysql://localhost:3306/rp";
        String username ="root";
        String password = "1234";
        Connection dbCon = null;
        try {
            // this will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
          //getting database connection to MySQL server
            dbCon = DriverManager.getConnection(dbURL, username, password);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } 
        return dbCon;
    }
     
    public ResultSet getResultSet(Connection dbCon,String cmdStr){
        
        ResultSet rs = null;
       
            try {
                  //getting PreparedStatment to execute query
                  stmt = dbCon.prepareStatement(cmdStr);

                  //Resultset returned by query
                    rs = stmt.executeQuery(cmdStr);
                    
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                } 
            return rs;
    }
    
    public boolean update(Connection dbCon,String cmdStr){
        try{
        stmt = dbCon.prepareStatement(cmdStr);
        stmt.executeQuery(cmdStr);
        return true;
         } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                } 
        return false;
    }
    
    public boolean close(Connection conn,ResultSet rs){
        try{
            if(rs!=null){
                rs.close();
            }
            if(stmt!=null){
                stmt.close();
            }
            if(conn!=null)
            {
                conn.close();
            }
            return true;
        }catch(Exception ex){
        System.out.println(ex.getMessage());
        }
        return false;
    }
}
