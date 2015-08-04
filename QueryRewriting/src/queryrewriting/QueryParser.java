package queryrewriting;

import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import java.util.Map;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TBaseType;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TGroupBy;
import gudusoft.gsqlparser.nodes.TGroupByItemList;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TJoinItem;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TTable;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

public class QueryParser {

    private String query;

    private String reslutQuery;
    QueryMetaData queryStmt;

    private HashMap<String, String> aliasMap;

    public QueryParser(String query) {

        this.query = query;

        aliasMap = new HashMap<>();

        

    }
    
    public  QueryMetaData  getQueryMetaData()
    {
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmysql);
        
        sqlparser.sqltext = getQuery();
        int ret = sqlparser.parse();

        if (ret == 0) {
            for (int i = 0; i < sqlparser.sqlstatements.size(); i++) {
               queryStmt =new QueryMetaData();
                //SQL statement is present- analyze the query
                analyzeStmt(sqlparser.sqlstatements.get(i));
                System.out.println("");
                return queryStmt;
                
            }
        } else {
            System.out.println(sqlparser.getErrormessage());
        }
       return null; 
    }
    

    protected void analyzeStmt(TCustomSqlStatement stmt) {
        switch (stmt.sqlstatementtype) {
            case sstselect:
                
                analyzeSelectStmt((TSelectSqlStatement) stmt);
                break;
            case sstupdate:
                break;
            case sstcreatetable:
                break;
            case sstaltertable:
                break;
            case sstcreateview:
                break;
            default:
                System.out.println(stmt.sqlstatementtype.toString());
        }

        /*   important	*/
        for (int i = 0; i < stmt.getStatements().size(); i++) {
            analyzeStmt(stmt.getStatements().get(i));
        }

    }

    private void analyzeSelectStmt(TSelectSqlStatement pStmt) {

        TTable t;
        //create object of querymetadata to store query details
        
        
        for (int i = 0; i < pStmt.tables.size(); i++) {
            t = pStmt.tables.getTable(i);
        	//System.out.println(t.toString()+" "+t.getAliasClause());
            
            //	storing the alias onto a map
            if (t.getAliasClause() == null) {
                aliasMap.put(t.toString(), t.toString());
                //queryStmt.getFromPart().put(t.toString(), t.toString());
                TableMetaData tempTable = new TableMetaData(t.toString(),t.toString());
                queryStmt.getFromPart().add(tempTable);
            } else {
                aliasMap.put(t.getAliasClause().toString(), t.toString());
                //queryStmt.getFromPart().put(t.getAliasClause().toString(), t.toString());
                TableMetaData tempTable = new TableMetaData(t.toString(),t.getAliasClause().toString());
                queryStmt.getFromPart().add(tempTable);
            }
        }

        // group by
        if (pStmt.getGroupByClause() != null) {
			//System.out.printf("\ngroup by: \n\t%s\n", pStmt.getGroupByClause().toString());
            //WhereCondition w=new WhereCondition(pStmt.getGroupByClause().getGROUP());
            //System.out.println("----------------"+pStmt.getGroupByClause().getItems());
            TGroupBy tg = pStmt.getGroupByClause();
            TGroupByItemList l = tg.getItems();

            // from here on we can query the metadata about the existence of aggregate table
            for (int i = 0; i < l.size(); i++) {
				//if no alias 
                //System.out.println(l.elementAt(i).getStartToken());
                //System.out.println(aliasMap.get(l.elementAt(i).getStartToken().toString()));
                //System.out.println(l.elementAt(i).getEndToken());
                if (l.elementAt(i).getEndToken().toString().equals(l.elementAt(i).getStartToken().toString())) {
                    System.out.println(l.elementAt(i).getStartToken());
                    ColumnMetaData tempCol =new ColumnMetaData(l.elementAt(i).getEndToken().toString(),"",l.elementAt(i).getStartToken().toString(),"","");
                    queryStmt.getGroupByPart().add(tempCol);
                } else {
                    System.out.println(l.elementAt(i).getStartToken());
                    System.out.println(l.elementAt(i).getEndToken());
                    ColumnMetaData tempCol =new ColumnMetaData(l.elementAt(i).getEndToken().toString(),"",l.elementAt(i).getStartToken().toString(),"","");
                    queryStmt.getGroupByPart().add(tempCol);
                }
            }
        }

        //System.out.println("\nSelect statement:");
        System.out.println("\nSELECT");
        queryStmt.setQueryType("SELECT");

		// select list
        for (int i = 0; i < pStmt.getResultColumnList().size(); i++) {
            TResultColumn resultColumn = pStmt.getResultColumnList().getResultColumn(i);
			// System.out.printf("\tColumn: %s, Alias: %s\n",
            // resultColumn.getExpr().toString(),
            // (resultColumn.getAliasClause() == null) ? "" :
            // resultColumn.getAliasClause().toString());
            //System.out.printf("\t%s, Alias: %s", resultColumn.getExpr(), (resultColumn.getAliasClause() == null) ? "" : resultColumn.getAliasClause().toString());
            //getAliasMap().put(resultColumn.getExpr().toString(), )
            System.out.println(resultColumn.getExpr() + " " + resultColumn.getAliasClause());
            if(resultColumn.getAliasClause()!=null){
                ColumnMetaData tempCol =new ColumnMetaData(resultColumn.getExpr().toString(),resultColumn.getAliasClause().toString(),"","","");
                queryStmt.getSelectPart().add(tempCol);
            }
            else
            {
                 ColumnMetaData tempCol =new ColumnMetaData(resultColumn.getExpr().toString(),"","","","");
                queryStmt.getSelectPart().add(tempCol);
            }
            
        }

		// from clause, check this document for detailed information
        // http://www.sqlparser.com/sql-parser-query-join-table.php
        for (int i = 0; i < pStmt.joins.size(); i++) {
            TJoin join = pStmt.joins.getJoin(i);
            switch (join.getKind()) {
                case TBaseType.join_source_fake:
                    System.out.printf("\ntable: \n\t%s, alias: %s\n", join.getTable().toString(), (join.getTable().getAliasClause() != null) ? join.getTable().getAliasClause().toString() : "");
                    break;

                case TBaseType.join_source_table:
                    System.out.printf("\ntable: \n\t%s, alias: %s\n", join.getTable().toString(), (join.getTable().getAliasClause() != null) ? join.getTable().getAliasClause().toString() : "");

                    for (int j = 0; j < join.getJoinItems().size(); j++) {
                        TJoinItem joinItem = join.getJoinItems().getJoinItem(j);
                        System.out.printf("Join type: %s\n", joinItem.getJoinType().toString());
                        System.out.printf("table: %s, alias: %s\n", joinItem.getTable().toString(), (joinItem.getTable().getAliasClause() != null) ? joinItem.getTable().getAliasClause().toString() : "");
                        //queryStmt.getJoinPart().put((joinItem.getTable().getAliasClause() != null) ? joinItem.getTable().getAliasClause().toString() : "", joinItem.getTable().toString());
                        if(joinItem.getTable().getAliasClause() != null){
                            TableMetaData tempTable = new TableMetaData(joinItem.getTable().toString(),joinItem.getTable().getAliasClause().toString());
                        queryStmt.getJoinPart().add(tempTable);
                        }else{
                            TableMetaData tempTable = new TableMetaData(joinItem.getTable().toString(),joinItem.getTable().toString());
                        queryStmt.getJoinPart().add(tempTable);
                        }
                        
                        
                        if (joinItem.getOnCondition() != null) {
                            System.out.printf("On: %s\n", joinItem.getOnCondition().toString());
                             if(joinItem.getOnCondition().getStartToken().toString().equals("("))
                             {
                                ColumnMetaData tempCol = new ColumnMetaData(joinItem.getOnCondition().getLeftOperand().getLeftOperand().getEndToken().toString(),"",joinItem.getOnCondition().getLeftOperand().getLeftOperand().getStartToken().toString(),"","");
                                queryStmt.getOnConditionPart().add(tempCol);
                                tempCol = new ColumnMetaData(joinItem.getOnCondition().getLeftOperand().getRightOperand().getEndToken().toString(),"",joinItem.getOnCondition().getLeftOperand().getRightOperand().getStartToken().toString(),"","");
                                queryStmt.getOnConditionPart().add(tempCol);
                             }  
                             
                             else
                             {
                                ColumnMetaData tempCol = new ColumnMetaData(joinItem.getOnCondition().getLeftOperand().getEndToken().toString(),"",joinItem.getOnCondition().getLeftOperand().getStartToken().toString(),"","");
                                queryStmt.getOnConditionPart().add(tempCol);
                                tempCol = new ColumnMetaData(joinItem.getOnCondition().getRightOperand().getEndToken().toString(),"",joinItem.getOnCondition().getRightOperand().getStartToken().toString(),"","");
                                queryStmt.getOnConditionPart().add(tempCol);
                             }
                             
                            //ColumnMetaData tempCol = new ColumnMetaData(joinItem.getOnCondition().toString(),"","","","");
                            //queryStmt.getOnConditionPart().add(tempCol);
                        } else if (joinItem.getUsingColumns() != null) {
                            System.out.printf("using: %s\n", joinItem.getUsingColumns().toString());
                        }
                    }
                    break;

                case TBaseType.join_source_join:
                    TJoin source_join = join.getJoin();
                    System.out.printf("\ntable: \n\t%s, alias: %s\n", source_join.getTable().toString(), (source_join.getTable().getAliasClause() != null) ? source_join.getTable().getAliasClause().toString() : "");
                    for (int j = 0; j < source_join.getJoinItems().size(); j++) {
                        TJoinItem joinItem = source_join.getJoinItems().getJoinItem(j);
                        System.out.printf("source_join type: %s\n", joinItem.getJoinType().toString());
                        System.out.printf("table: %s, alias: %s\n", joinItem.getTable().toString(), (joinItem.getTable()
                                .getAliasClause() != null) ? joinItem.getTable()
                                .getAliasClause().toString() : "");
                        if (joinItem.getOnCondition() != null) {
                            System.out.printf("On: %s\n", joinItem.getOnCondition().toString());
                        } else if (joinItem.getUsingColumns() != null) {
                            System.out.printf("using: %s\n", joinItem.getUsingColumns().toString());
                        }
                    }
                    for (int j = 0; j < join.getJoinItems().size(); j++) {
                        TJoinItem joinItem = join.getJoinItems().getJoinItem(j);
                        System.out.printf("Join type: %s\n", joinItem.getJoinType().toString());
                        System.out.printf("table: %s, alias: %s\n", joinItem.getTable().toString(), (joinItem.getTable().getAliasClause() != null) ? joinItem.getTable().getAliasClause().toString() : "");
                        if (joinItem.getOnCondition() != null) {
                            System.out.printf("On: %s\n", joinItem.getOnCondition().toString());
                        } else if (joinItem.getUsingColumns() != null) {
                            System.out.printf("using: %s\n", joinItem.getUsingColumns().toString());
                        }
                    }
                    break;
                default:
                    System.out.println("unknown type in join!");
                    break;
            }
        }

        // where clause
        if (pStmt.getWhereClause() != null) {
            //System.out.printf("\nwhere clause: \n\t%s\n", pStmt.getWhereClause().getCondition().toString());
            WhereCondition w = new WhereCondition(pStmt.getWhereClause().getCondition());
            ColumnMetaData tempCol = new ColumnMetaData(pStmt.getWhereClause().getCondition().toString(),"","","","");
            queryStmt.getWherePart().add(tempCol);
            w.printColumn();
        }
        // group by
        if (pStmt.getGroupByClause() != null) {
            System.out.printf("\ngroup by: \n\t%s\n", pStmt.getGroupByClause()
                    .toString());
			//WhereCondition w=new WhereCondition(pStmt.getGroupByClause().getGROUP());
            //System.out.println("----------------"+pStmt.getGroupByClause().getItems());
            TGroupBy tg = pStmt.getGroupByClause();
            TGroupByItemList l = tg.getItems();

            for (int i = 0; i < l.size(); i++) {
                System.out.println(l.elementAt(i).getStartToken());
                System.out.println(l.elementAt(i).getEndToken());
                //ColumnMetaData tempCol = new ColumnMetaData(l.elementAt(i).getEndToken().toString(),"",l.elementAt(i).getStartToken().toString(),"","");
                //queryStmt.getGroupByPart().add(tempCol);
            }
        }
        // order by
        if (pStmt.getOrderbyClause() != null) {
            System.out.printf("\norder by:");
            for (int i = 0; i < pStmt.getOrderbyClause().getItems().size(); i++) {
                System.out.printf("\n\t%s", pStmt.getOrderbyClause().getItems()
                        .getOrderByItem(i).toString());
            }
        }
        // for update
        if (pStmt.getForUpdateClause() != null) {
            System.out.printf("for update: \n%s\n", pStmt.getForUpdateClause()
                    .toString());
        }
        // top clause
        if (pStmt.getTopClause() != null) {
            System.out.printf("top clause: \n%s\n", pStmt.getTopClause()
                    .toString());
        }
        // limit clause
        if (pStmt.getLimitClause() != null) {
            System.out.printf("top clause: \n%s\n", pStmt.getLimitClause()
                    .toString());
        }

        System.out.println("----------------+++");
        for (Map.Entry<String, String> entry : aliasMap.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getReslutQuery() {
        return reslutQuery;
    }

    public void setReslutQuery(String reslutQuery) {
        this.reslutQuery = reslutQuery;
    }

    public HashMap<String, String> getAliasMap() {
        return aliasMap;
    }

    public void setAliasMap(HashMap<String, String> aliasMap) {
        this.aliasMap = aliasMap;
    }
}
