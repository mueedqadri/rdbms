package com.dbms.sql.handler;

import com.dbms.transaction.Lock;
import com.dbms.transaction.TransactionController;
import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.log.DBLogger;
import com.dbms.log.QueryLogModel;
import com.dbms.log.QueryLogger;
import com.dbms.model.TableMetaDataModel;
import com.dbms.sql.handler.interfaces.IHandler;
import com.dbms.sql.parser.InsertQueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;

public class InsertQueryHandler implements IHandler {

    private final InsertQueryParser parser = new InsertQueryParser();

    @Override
    public void handle(String sqlQuery) {
        if(parser.isValid(sqlQuery)) {
            String tableName = parser.getTableName(sqlQuery);
            final MetaDataTable metaDataForTable = Database.getMetaDataForTable(tableName);
            final Table table = Database.getTable(tableName);
            final Map<Object, List<Object>> rows = table.getRows();
            Map<String, String> columnsWithValues = parser.getColumnsWithValues(sqlQuery, getDataModels(metaDataForTable), rows.size());
            Object key = getKey(columnsWithValues, metaDataForTable);
            checkForPrimaryKey(rows, key);
            checkForForeignKey(columnsWithValues, metaDataForTable);
            List<Object> row = getRow(columnsWithValues, metaDataForTable);
            TransactionController controller = new TransactionController();
            controller.executeAddRow(row, tableName, key);
            logInsert(tableName, row.toString());
        } else {
            DBLogger.logError("Invalid Query.");
        }
    }

    private void logInsert( String tableName, String newValue){
        QueryLogModel model = new QueryLogModel();
        model.operation = "INSERT ";
        model.tableName = tableName;
        model.columnName = "";
        model.prevValue = "";
        model.newValue = newValue;
        QueryLogger.customLogger(model);
    }

    private void checkForForeignKey(Map<String, String> columnsWithValues, MetaDataTable metaDataForTable) {
        metaDataForTable.getTableMetaData().values().forEach(value -> {
            if(!isNull(value.getFkTable()) && !value.getFkTable().trim().isEmpty() &&
                    !value.getFkTable().trim().equalsIgnoreCase("null")) {
                Boolean valid = false;
                final Object fkValue = columnsWithValues.get(value.getColName());
                if(isNull(fkValue)) throw new IllegalArgumentException("FK value can't be null");
                final Table table = Database.getTable(value.getFkTable());
                final MetaDataTable metaDataForFKTable = Database.getMetaDataForTable(value.getFkTable());
                final Integer order = metaDataForFKTable.getTableMetaData().get(value.getFkColumn()).getOrder();
                for (List<Object> row : table.getRows().values()) {
                    if (fkValue.equals(row.get(order))) {
                        valid = true;
                    }
                }
                if(!valid) throw new IllegalArgumentException("Invalid value for FK");
            }
        });
    }

    private void checkForPrimaryKey(Map<Object, List<Object>> rows, Object key) {
        if(rows.containsKey(key)) throw new IllegalArgumentException("Primary key Already exists");
    }

    private List<TableMetaDataModel> getDataModels(MetaDataTable metaDataForTable) {
        return metaDataForTable.getTableMetaData().values().stream()
                .sorted(comparing(TableMetaDataModel::getOrder)).collect(Collectors.toList());
    }

    private Object getKey(Map<String, String> columnsWithValues, MetaDataTable metaDataForTable) {
        final TableMetaDataModel tableMetaDataModel = metaDataForTable.getTableMetaData().values().stream()
                .filter(TableMetaDataModel::getPk).findFirst().orElseThrow(IllegalArgumentException::new);
        return columnsWithValues.get(tableMetaDataModel.getColName());
    }

    private List<Object> getRow(Map<String, String> columnsWithValues, MetaDataTable metaDataForTable) {
        List<Object> row = new ArrayList<>();
        metaDataForTable.getTableMetaData().values().stream().sorted(comparing(TableMetaDataModel::getOrder))
                .forEach(val -> {
                    row.add(columnsWithValues.get(val.getColName()));
                });
        return row;
    }
}
