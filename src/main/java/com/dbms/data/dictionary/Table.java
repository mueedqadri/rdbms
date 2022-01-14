package com.dbms.data.dictionary;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table {

    private String tableName;
    private Map<Object, List<Object>> rows = new LinkedHashMap<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<Object, List<Object>> getRows() {
        return rows;
    }

    public void setRows(Map<Object, List<Object>> rows) {
        this.rows = rows;
    }

    public void addRow(Object key, List<Object> row) {
        this.rows.put(key, row);
    }

    public void deleteRow(Object key) {
        this.rows.remove(key);
    }

    public void updateRow(Object key, List<Object> row) {
        this.rows.replace(key, row);
    }
}
