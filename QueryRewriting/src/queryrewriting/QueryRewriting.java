/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewriting;

import java.sql.*;

/**
 *
 * @author Shrikant
 */
public class QueryRewriting {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DbManager dbmanager=new DbManager();
        String query ="select count(*) from customer";
       
        try {
            Connection conn=dbmanager.getConnection();
            ResultSet result=dbmanager.getResultSet(conn, query);
            while(result.next()){
                int count = result.getInt(1);
                System.out.println("count of customers : " + count);
            }
            dbmanager.close(conn,result);
           
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally{
           //close connection ,stmt and resultset here
        }
    }
 
}


