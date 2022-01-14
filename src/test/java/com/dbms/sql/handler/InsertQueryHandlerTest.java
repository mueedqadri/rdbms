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

class InsertQueryHandlerTest {

    InsertQueryHandler insertQueryHandler = new InsertQueryHandler();

    @Test
    void shouldHandleWithForeignKey() {
        setUpDB();
        insertQueryHandler.handle("insert into table_name (column1, column2, column3) values (value1, value2, value3);");
        final Table updatedTable = Database.getTable("table_name");
        final Map<Object, List<Object>> rows = updatedTable.getRows();
        final Map<Object, List<Object>> expected = new HashMap<>();
        expected.put("value1", asList("value1", "value2", "value3"));
        assertEquals(expected, rows);
    }

    private void setUpDB() {
        Database.setName("university");
        final Table table = new Table();
        table.setTableName("table_name");
        final Table fkTable = new Table();
        fkTable.setTableName("fk_table_name");
        fkTable.addRow("val1", asList("val1", "value2"));
        Database.addTable(table);
        Database.addTable(fkTable);
        final MetaDataTable metaData = new MetaDataTable();
        metaData.setName("table_name");
        metaData.setTableMetaData(getTableMetaDataModels());
        final MetaDataTable metaDataFK = new MetaDataTable();
        metaDataFK.setName("fk_table_name");
        metaDataFK.setTableMetaData(getTableMetaDataModelsFK());
        Database.addMetaDataTable(metaData);
        Database.addMetaDataTable(metaDataFK);
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

    private HashMap<String, TableMetaDataModel> getTableMetaDataModelsFK() {
        HashMap<String, TableMetaDataModel> hashMap = new HashMap<>();
        final TableMetaDataModel tableMetaDataModel1 = new TableMetaDataModel();
        tableMetaDataModel1.setColName("column1");
        tableMetaDataModel1.setOrder(1);
        tableMetaDataModel1.setPk(true);
        hashMap.put("column1", tableMetaDataModel1);
        final TableMetaDataModel tableMetaDataModel2 = new TableMetaDataModel();
        tableMetaDataModel2.setColName("fk_table_col");
        tableMetaDataModel2.setOrder(2);
        hashMap.put("fk_table_col", tableMetaDataModel2);
        return hashMap;
    }


}