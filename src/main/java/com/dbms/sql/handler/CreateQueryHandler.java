package com.dbms.sql.handler;

import com.dbms.transaction.TransactionController;
import com.dbms.constants.GlobalConstants;
import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.log.DBLogger;
import com.dbms.log.QueryLogModel;
import com.dbms.log.QueryLogger;
import com.dbms.model.TableMetaDataModel;
import com.dbms.sql.parser.CreateQueryParser;

import java.util.*;

public class CreateQueryHandler {

    private final CreateQueryParser parser = new CreateQueryParser();
    public String sqlQuery;
    private List<String> allColumnNames;

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery.replaceAll("\n", "");
    }

    public void handle() {
        if(parser.isValid(sqlQuery)){
            if(parser.operationType.equalsIgnoreCase(GlobalConstants.TABLE)){
                createTable();
            } else if(parser.operationType.equalsIgnoreCase(GlobalConstants.DATABASE)){
                if(!createDatabase()){
                    DBLogger.logError("Database already exists");
                }
            }
        } else  {
            DBLogger.logError("Invalid Query.");
        }
    }

    private void createTable(){
        String tableName = parser.getTableGroup(sqlQuery);
        if(!Database.getTables().containsKey(tableName)){
            MetaDataTable metaData = getMetaDataTable(tableName);
            Table table = getEmptyTable(tableName);
            TransactionController controller = new TransactionController();
            controller.executeTableWrite(table);
            controller.executeMetaTableWrite(metaData);
            logCreate("TABLE", tableName);
        }
        else {
            DBLogger.logError("Table already exists");
        }
    }

    private void logCreate(String subtype, String tableName){
        QueryLogModel model = new QueryLogModel();
        model.operation = "CREATE "+subtype;
        model.tableName = tableName;
        model.columnName = "";
        model.prevValue = "";
        model.newValue = "";
        QueryLogger.customLogger(model);
    }

    private boolean createDatabase(){
        String databaseName = parser.getDatabaseGroup(sqlQuery);
        logCreate("DATABASE", databaseName);
        return TableWriter.createDatabase(databaseName) && TableWriter.addUserDbMapping(databaseName);
    }

    private MetaDataTable getMetaDataTable(String tableName) {
        MetaDataTable metaData = new MetaDataTable();
        metaData.setName(tableName);
        Map<String, TableMetaDataModel > metaDataTable = new LinkedHashMap<>();
        allColumnNames = parser.getAllColumnName(this.sqlQuery);
        List<String> allDataTypes = parser.getAllDatatype(this.sqlQuery);
        List<Boolean> allPrimaryKey = parser.getAllPrimary(this.sqlQuery);
        List<Boolean> allNotNull = parser.getAllNotNULL(this.sqlQuery);
        List<Boolean> allAutoIncrement = parser.getAllAutoIncrement(this.sqlQuery);
        Map<String,String> FK = parser.getAllForeignKey(this.sqlQuery);

        for (int i = 0; i < allColumnNames.size(); i++) {
            String columnName = allColumnNames.get(i);
            String fk = FK.get(columnName);
            TableMetaDataModel tempColumn = new TableMetaDataModel();
            tempColumn.setColName(columnName);
            tempColumn.setColType(allDataTypes.get(i));
            tempColumn.setPk(allPrimaryKey.get(i));
            tempColumn.setNotNull(allNotNull.get(i));
            tempColumn.setAi(allAutoIncrement.get(i));
            if (fk != null) {
                tempColumn.setFkTable(fk.split("\\|")[0]);
                tempColumn.setFkColumn(fk.split("\\|")[1]);
            }
            tempColumn.setOrder(i);
            metaDataTable.put(tempColumn.getColName(), tempColumn);
        }
        metaData.setTableMetaData(metaDataTable);
        return metaData;
    }

    private Table getEmptyTable(String tableName){
        Table table=new Table();
        table.setRows(new LinkedHashMap<>());
        table.setTableName(tableName);
        return table;
    }
}