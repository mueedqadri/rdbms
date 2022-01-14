package com.dbms.transaction;

import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.log.DBLogger;
import com.dbms.log.QueryLogger;
import com.dbms.sql.handler.DatabaseLoader;
import com.dbms.sql.handler.TableWriter;

import java.util.List;
import java.util.Map;

public class TransactionController {

    public static boolean isTransaction = false;
    private Lock lock = new Lock();

    public void commit(){
        Map<String, Table> tables = Database.getTables();
        Map<String, MetaDataTable> metaDataTables = Database.getMetaDataAllTables();
        for(var table : tables.entrySet()){
            TableWriter.writeTable(table.getValue());
        }
        for (var metaTable : metaDataTables.entrySet()){
            TableWriter.writeMetaData(metaTable.getValue());
        }
        DBLogger.logInfo("Transaction committed successfully ");
        isTransaction = false;
    }

    public void executeTableWrite(Table table){
        Database.addTable(table);
        if(!isTransaction){
            TableWriter.writeTable(table);
        }
    }

    public void executeUpdate(String tableName, String content){
        if(!isTransaction){
            TableWriter.writeTableData(tableName, content);
        }
    }

    public void executeMetaTableWrite(MetaDataTable metaDataTable){
        Database.addMetaDataTable(metaDataTable);
        if(!isTransaction){
            TableWriter.writeMetaData(metaDataTable);
        }
    }

    public void dropTable(String tableName){
        Database.getTables().remove(tableName);
        Database.getMetaDataAllTables().remove(tableName);
        if(!isTransaction){
            TableWriter.deleteTable(tableName);
        }
    }

    public void rollBack(){
        DatabaseLoader loader = new DatabaseLoader();
        loader.load();
        DBLogger.logInfo("Rolled back ");
        isTransaction = false;
    }

    public void executeAddRow(List<Object> row, String tableName, Object key) {
        final Table table = Database.getTable(tableName);
        table.addRow(key, row);
        if(!isTransaction) {
            TableWriter.writeRow(table, row);
        }
    }
}
