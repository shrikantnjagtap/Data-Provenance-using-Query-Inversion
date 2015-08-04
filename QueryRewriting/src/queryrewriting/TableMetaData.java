/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package queryrewriting;

/**
 *
 * @author Shrikant
 */

   
public class TableMetaData {
    private String tableName;
    private String tableAliasName;

    public TableMetaData(){
        this.tableName="";
        this.tableAliasName="";
    }
    public TableMetaData(String tableName,String tableAliasName){
        this.tableName=tableName;
        this.tableAliasName=tableAliasName;
    }
    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @return the tableAliasName
     */
    public String getTableAliasName() {
        return tableAliasName;
    }

    /**
     * @param tableAliasName the tableAliasName to set
     */
    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
