package com.dbms.sql.handler;

import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.model.TableMetaDataModel;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectQueryHandlerTest {

    SelectQueryHandler selectQueryHandler = new SelectQueryHandler();

    @Test
    void shouldHandle() {
        setUpDB();
        selectQueryHandler.handle("select * from table_name where column2 = value2;");
    }

    @Test
    void shouldHandleWithoutWhereClause() {
        setUpDB();
        selectQueryHandler.handle("select * from table_name;");
    }

    private void setUpDB() {
        Database.setName("university");
        final Table table = new Table();
        table.setTableName("table_name");
        table.addRow("val1", asList("value1", "value2", "value3"));
        Database.addTable(table);
        final MetaDataTable metaData = new MetaDataTable();
        metaData.setName("table_name");
        metaData.setTableMetaData(getTableMetaDataModels());
        Database.addMetaDataTable(metaData);
    }

    private HashMap<String, TableMetaDataModel> getTableMetaDataModels() {
        HashMap<String, TableMetaDataModel> hashMap = new HashMap<>();
        final TableMetaDataModel tableMetaDataModel1 = new TableMetaDataModel();
        tableMetaDataModel1.setColName("column1");
        tableMetaDataModel1.setOrder(1);
        tableMetaDataModel1.setPk(true);
        hashMap.put("column1", tableMetaDataModel1);
        final TableMetaDataModel tableMetaDataModel2 = new TableMetaDataModel();
        tableMetaDataModel2.setColName("column2");
        tableMetaDataModel2.setOrder(2);
        tableMetaDataModel2.setFkTable("fk_table_name");
        tableMetaDataModel2.setFkColumn("fk_table_col");
        hashMap.put("column2", tableMetaDataModel2);
        final TableMetaDataModel tableMetaDataModel3 = new TableMetaDataModel();
        tableMetaDataModel3.setColName("column3");
        tableMetaDataModel3.setOrder(3);
        hashMap.put("column3", tableMetaDataModel3);
        return hashMap;
    }

}