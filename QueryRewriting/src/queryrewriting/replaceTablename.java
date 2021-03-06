package queryrewriting;

import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TTable;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

/**
 * This demo shows how to replace a specified table name with new one.
 * <p>Steps to modify a table name:
 * <p>1. find the table name that need to be replaced.
 * <p>2. use setString method to set a new string representation of that table.
 *
 * <p>In this demo, input sql is:
 *
 * <p>select table1.col1, table2.col2
 * <p>from table1, table2
 * <p>where table1.foo > table2.foo
 *
 * <p>we want to replace table2 with "(tableX join tableY using (id)) as table3"
 * <p>and change table2.col2 to table3.col2, table2.foo to table3.foo accordingly.
 *  
 */
public class replaceTablename{
    public static void main(String args[])
     {

        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmysql);

         sqlparser.sqltext = "select table1.col1, g.col2, SUM(g.col3), g.time\n" +
                 "from table1, table2 g\n" +
                 "where table1.foo > g.foo  group by g.time";

        System.out.println("input sql:");
        System.out.println(sqlparser.sqltext);

        int ret = sqlparser.parse();
        if (ret == 0){

            TSelectSqlStatement select = (TSelectSqlStatement)sqlparser.sqlstatements.get(0);

           TTable t ;
           for(int i=0;i<select.tables.size();i++){
             t = select.tables.getTable(i);
             System.out.println(t.toString());
             if (t.toString().compareToIgnoreCase("table2") == 0){
                 for(int j=0;j<t.getObjectNameReferences().size();j++){
                    if(t.getObjectNameReferences().getObjectName(j).getObjectToken().toString().equalsIgnoreCase("table2")){
                        t.getObjectNameReferences().getObjectName(j).getObjectToken().astext = "table3";
                    }
                 }
               t.setString(" table3");
             }
           }

            System.out.println("\noutput sql:");
            System.out.println(select.toString());

        }else{
            System.out.println(sqlparser.getErrormessage());
        }
     }

}
