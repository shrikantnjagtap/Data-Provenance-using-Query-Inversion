/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewriting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author KIRAN
 */
public class QINVERSION {
Connection connection;
Statement stmt,stat;
public QINVERSION()
{
    /*try {
        	  Class.forName("com.mysql.jdbc.Driver");
              // Setup the connection with the DB
              connection = DriverManager
                  .getConnection("jdbc:mysql://localhost/RP?"
                      + "user=sqluser&password=sqluserpw"); 
              stmt = connection.createStatement();
           
           // stmt = connection.createStatement();
       
            }catch(Exception e){
            e.printStackTrace();
          
        }*/
}
public void updateQT(String Qstr)
{
   
   java.sql.Timestamp TS=null;
   String sql="",d1="";
   try{
        TS = new java.sql.Timestamp(new java.util.Date().getTime());
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         d1=format.format(TS);
        sql = "INSERT INTO  RP.QTABLE (QSTR,TS) VALUES ('" + Qstr.substring(0,(Qstr.length())-1) + "','"+ d1 +"');";
        System.out.println(sql);
   }catch(Exception ex){
       ex.printStackTrace();
   }

   try {
       try {
        	  Class.forName("com.mysql.jdbc.Driver");
              // Setup the connection with the DB
              connection = DriverManager
                  .getConnection("jdbc:mysql://localhost/rp?"
                      + "user=root&password=1234"); 
              stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY,ResultSet.CLOSE_CURSORS_AT_COMMIT);
              stat=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY,ResultSet.CLOSE_CURSORS_AT_COMMIT);
              System.out.println("jjjlll");
           // stmt = connection.createStatement();
       
            }catch(Exception e){
            e.printStackTrace();
          
        }
       
        stmt.executeUpdate(sql);
      
            
        

    } catch (SQLException ex) {
        Logger.getLogger(QINVERSION.class.getName()).log(Level.SEVERE, null, ex);
    }
   String sql1 = "SELECT QID FROM QTABLE WHERE TS = '"+ d1 +"';";
   System.out.println("asdfasdfasdf");
    ResultSet rs,rs1;
    try {
        rs1= stmt.executeQuery(Qstr);
        rs = stat.executeQuery(sql1);
        rs.next();
        System.out.println("QID = "+Integer.parseInt(rs.getObject(1).toString()));
        createOT(Integer.parseInt(rs.getObject(1).toString()),rs1);
    } catch (SQLException ex) {
        Logger.getLogger(QINVERSION.class.getName()).log(Level.SEVERE, null, ex);
    }
}
public void createOT(int num,ResultSet rs2)
{
 String columnlist ="";   
 String sql = "CREATE TABLE OUTPUT"+num+"(";
    try {
        rs2.next();
        for(int i=1;i<=rs2.getMetaData().getColumnCount();i++)
        {
            sql=sql+rs2.getMetaData().getColumnName(i)+ " ";
            columnlist+=rs2.getMetaData().getColumnName(i);
            if(i!=rs2.getMetaData().getColumnCount()){
                columnlist+=",";
            }
           if(rs2.getMetaData().getColumnTypeName(i).equals("VARCHAR")==true){
               sql=sql+rs2.getMetaData().getColumnTypeName(i)+"(50),";
           }else{
               sql=sql+rs2.getMetaData().getColumnTypeName(i)+",";
           }
            
        }
        sql=sql+"QID INTEGER DEFAULT "+num+", FOREIGN KEY(QID) REFERENCES qtable(qid));";
        stat.executeUpdate(sql);
        String sql3="";
        stat = connection.createStatement();
        do
        {
        sql3 = "INSERT INTO OUTPUT"+num+"("+columnlist+") VALUES (";
                for(int j=1;j<=rs2.getMetaData().getColumnCount();j++)
                { 
                    if(rs2.getMetaData().getColumnTypeName(j).equals("INT"))
                      sql3=sql3+rs2.getInt(j);  
                    else
                      sql3=sql3+"'"+rs2.getString(j)+"'";
                    if(j!=rs2.getMetaData().getColumnCount())
                      sql3=sql3+","; 
                }
           sql3=sql3+");";
           stat.addBatch(sql3);
           
        }while(rs2.next());
        
        stat.executeBatch();
        if(connection != null)
                connection.close();
            connection = null;
                  
    } catch (SQLException ex) {
        Logger.getLogger(QINVERSION.class.getName()).log(Level.SEVERE, null, ex);
    }
}
   
}
