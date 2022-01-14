package com.dbms.data.dictionary;

import com.dbms.model.TableMetaDataModel;

import java.util.Map;

public class MetaDataTable {

    private String name;
    private Map<String, TableMetaDataModel> tableMetaData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, TableMetaDataModel> getTableMetaData() {
        return tableMetaData;
    }

    public void setTableMetaData(Map<String, TableMetaDataModel> tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    public void addRow( TableMetaDataModel metaDataModel){
        tableMetaData.put(metaDataModel.getColName(), metaDataModel);
    }
}
