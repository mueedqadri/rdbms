package com.dbms.data.dictionary;

import com.dbms.log.DBLogger;
import com.dbms.sql.handler.TableReader;

import java.util.LinkedHashMap;
import java.util.Map;

public class Database {

    public static String name;
    private static Map<String, Table> tables = new LinkedHashMap<>();
    private static Map<String, MetaDataTable> metaDataAllTables = new LinkedHashMap<>();

    private Database(){}
    
    public static void setName(String name) {
        if(TableReader.isExistsDatabase(name)){
            Database.name = name;
        } else{
            DBLogger.logError("database does not exist");
        }
    }

    public static void reset(){
        tables =  new LinkedHashMap<>();
        metaDataAllTables = new LinkedHashMap<>();
    }

    public static Map<String, MetaDataTable> getMetaDataAllTables() {
        return metaDataAllTables;
    }

    public static Map<String, Table> getTables() {
        return tables;
    }

    public static Integer addTable(Table table) {
        tables.put(table.getTableName(), table);
        return tables.size();
    }

    public static Integer addMetaDataTable(MetaDataTable metaData) {
        metaDataAllTables.put(metaData.getName(), metaData);
        return tables.size();
    }

    public static MetaDataTable getMetaDataForTable(String table) {
        if(!metaDataAllTables.containsKey(table)) throw new RuntimeException("Table does not exist.");
        return metaDataAllTables.get(table);
    }

    public static Table getTable(String table) {
        if(!tables.containsKey(table)) throw new RuntimeException("Table does not exist.");
        return tables.get(table);
    }
}
