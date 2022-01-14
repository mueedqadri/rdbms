package com.dbms.sql.handler;

import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.model.TableMetaDataModel;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static java.util.Arrays.asList;

class DeleteQueryHandlerTest {

    DeleteQueryHandler deleteQueryHandler = new DeleteQueryHandler();

    @Test
    void shouldHandleWithForeignKey() {
        setUpDB();
        deleteQueryHandler.handle("delete from table_name where column3=value3;");
        final Table updatedTable = Database.getTable("table_name");
    }

    private void setUpDB() {
        Database.setName("university");
        final Table table = new Table();
        table.setTableName("table_name");
        table.addRow("value1", asList("value1", "value2", "value3"));
        table.addRow("value11", asList("value11", "value2", "value4"));
        table.addRow("value111", asList("value111", "value2", "value3"));
        final Table fkTable = new Table();
        fkTable.setTableName("fk_table_name");
        fkTable.addRow("val1", asList("val1", "value2"));
        fkTable.addRow("val2", asList("val2", "value10"));
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