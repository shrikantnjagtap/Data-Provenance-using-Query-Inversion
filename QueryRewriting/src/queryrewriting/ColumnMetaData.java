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
public class ColumnMetaData {
    
    private String columnName;
    private String columnAliasName;
    private String tableName;
    private String columnNewName;
    private String aggregateFunction;

    public ColumnMetaData(){

    }
    public ColumnMetaData(String columnName,String columnAliasName,String tableName,String columnNewName,String aggregateFunction){
        this.columnName=columnName;
        this.columnAliasName=columnAliasName;
        this.tableName=tableName;
        this.columnNewName=columnNewName;
        this.aggregateFunction=aggregateFunction;
    }
    
    public String toString()
    {
        if(this.tableName.isEmpty() || this.tableName==null){
            return this.columnName;
        }
        else{
            return this.tableName+"."+this.columnName;
        }  
    }
    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * @return the columnAliasName
     */
    public String getColumnAliasName() {
        return columnAliasName;
    }

    /**
     * @param columnAliasName the columnAliasName to set
     */
    public void setColumnAliasName(String columnAliasName) {
        this.columnAliasName = columnAliasName;
    }

    /**
     * @return the columnNewName
     */
    public String getColumnNewName() {
        return columnNewName;
    }

    /**
     * @param columnNewName the columnNewName to set
     */
    public void setColumnNewName(String columnNewName) {
        this.columnNewName = columnNewName;
    }

    /**
     * @return the aggregateFunction
     */
    public String getAggregateFunction() {
        return aggregateFunction;
    }

    /**
     * @param aggregateFunction the aggregateFunction to set
     */
    public void setAggregateFunction(String aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
