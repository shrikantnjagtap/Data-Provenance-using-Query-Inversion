/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewriting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Shrikant
 */
public class QueryMetaData {

        // Select
        // From table list
        // where
        // group by
        // Join - 1.table list 2.On condition
        // Inner query tablemetadata
        private String queryType;
        private ArrayList<ColumnMetaData> selectPart;
        private ArrayList<TableMetaData> fromPart;
        private ArrayList<ColumnMetaData> wherePart;
        private ArrayList<ColumnMetaData> groupByPart;
        private ArrayList<TableMetaData> joinPart;
        private ArrayList<ColumnMetaData> onConditionPart;
    
    public QueryMetaData()
    {
        this.selectPart = new ArrayList<ColumnMetaData>();
        this.fromPart = new ArrayList<TableMetaData>();
        this.wherePart = new ArrayList<ColumnMetaData>();
        this.groupByPart = new ArrayList<ColumnMetaData>();
        this.joinPart = new ArrayList<TableMetaData>();
        this.onConditionPart = new ArrayList<ColumnMetaData>();
    }

    /**
     * @return the queryType
     */
    public String getQueryType() {
        return queryType;
    }

    /**
     * @param queryType the queryType to set
     */
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    /**
     * @return the selectPart
     */
    public ArrayList<ColumnMetaData> getSelectPart() {
        return selectPart;
    }

    /**
     * @param selectPart the selectPart to set
     */
    public void setSelectPart(ArrayList<ColumnMetaData> selectPart) {
        this.selectPart = selectPart;
    }

    /**
     * @return the fromPart
     */
    public ArrayList<TableMetaData> getFromPart() {
        return fromPart;
    }

    /**
     * @param fromPart the fromPart to set
     */
    public void setFromPart(ArrayList<TableMetaData> fromPart) {
        this.fromPart = fromPart;
    }

    /**
     * @return the wherePart
     */
    public ArrayList<ColumnMetaData> getWherePart() {
        return wherePart;
    }

    /**
     * @param wherePart the wherePart to set
     */
    public void setWherePart(ArrayList<ColumnMetaData> wherePart) {
        this.wherePart = wherePart;
    }

    /**
     * @return the groupByPart
     */
    public ArrayList<ColumnMetaData> getGroupByPart() {
        return groupByPart;
    }

    /**
     * @param groupByPart the groupByPart to set
     */
    public void setGroupByPart(ArrayList<ColumnMetaData> groupByPart) {
        this.groupByPart = groupByPart;
    }

    /**
     * @return the joinPart
     */
    public ArrayList<TableMetaData> getJoinPart() {
        return joinPart;
    }

    /**
     * @param joinPart the joinPart to set
     */
    public void setJoinPart(ArrayList<TableMetaData> joinPart) {
        this.joinPart = joinPart;
    }

    /**
     * @return the onConditionPart
     */
    public ArrayList<ColumnMetaData> getOnConditionPart() {
        return onConditionPart;
    }

    /**
     * @param onConditionPart the onConditionPart to set
     */
    public void setOnConditionPart(ArrayList<ColumnMetaData> onConditionPart) {
        this.onConditionPart = onConditionPart;
    }
}
