package com.dbms.sql.handler;

import com.dbms.transaction.Lock;
import com.dbms.transaction.TransactionController;
import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.log.*;
import com.dbms.model.TableMetaDataModel;
import com.dbms.sql.handler.interfaces.IHandler;
import com.dbms.sql.parser.UpdateQueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;

public class UpdateQueryHandler implements IHandler {

    private final UpdateQueryParser updateQueryParser = new UpdateQueryParser();

    @Override
    public void handle(String sqlQuery) {
        if (updateQueryParser.isValid(sqlQuery)) {
            final String tableName = updateQueryParser.getTableName(sqlQuery);
            final MetaDataTable metaDataForTable = Database.getMetaDataForTable(tableName);
            final ArrayList<TableMetaDataModel> metaDataModels = new ArrayList<>(getDataModels(metaDataForTable));
            final List<String> whereClause = updateQueryParser.getWhereClause(sqlQuery, metaDataForTable.getTableMetaData());
            final Map<Integer, String> columnsWithValues = updateQueryParser.getColumnsWithValues(sqlQuery, metaDataForTable.getTableMetaData());

            final Table table = Database.getTable(tableName);
            updateRows(table, whereClause, columnsWithValues, metaDataModels);
            TransactionController controller = new TransactionController();
            controller.executeUpdate(tableName, getTableContent(table));
        } else {
            DBLogger.logError("Invalid Query.");
        }
    }


    private void logUpdate( String tableName, String newValue, String prevValue, String columnName){
        QueryLogModel model = new QueryLogModel();
        model.operation = "UPDATE ";
        model.tableName = tableName;
        model.columnName = columnName;
        model.prevValue = prevValue;
        model.newValue = newValue;
        QueryLogger.customLogger(model);
    }



    private String getTableContent(Table table) {
        return table.getRows().values().stream()
                .map(row -> row.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("|"))
                ).collect(Collectors.joining("\n"));
    }

    private void updateRows(Table table, List<String> whereClause, Map<Integer, String> columnsWithValues,
                            ArrayList<TableMetaDataModel> metaDataModels) {
        Integer indexOfColumn = !whereClause.isEmpty() ?
                getIndices(metaDataModels, singletonList(whereClause.get(0))).get(0) : null;
        table.getRows().values().stream()
                .filter(row -> whereClause.isEmpty() || row.get(indexOfColumn).equals(whereClause.get(1)))
                .forEach(row -> updateRow(row, columnsWithValues, metaDataModels, table.getTableName()));
    }

    private void updateRow(List<Object> row, Map<Integer, String> columnsWithValues,
                           ArrayList<TableMetaDataModel> metaDataModels, String tableName) {
        columnsWithValues.forEach((key, value) -> {
            checkForPrimaryKey(row, key, metaDataModels);
            checkForForeignKey(columnsWithValues, metaDataModels);
            final List<Object> prev = row;
            row.set(key, value);
            logUpdate(tableName, row.toString(), prev.toString(), "");
        });
    }

    private void checkForPrimaryKey(List<Object> row, Integer index, ArrayList<TableMetaDataModel> metaDataModels) {
        final TableMetaDataModel pkColumn = metaDataModels.stream().filter(metaDataModel -> metaDataModel.getPk()).findFirst().get();
        if (index.equals(pkColumn.getOrder())) throw new IllegalArgumentException();

    }

    private void checkForForeignKey(Map<Integer, String> columnsWithValues, ArrayList<TableMetaDataModel> metaDataForTable) {
        metaDataForTable.forEach(value -> {
            if (!isNull(value.getFkTable()) && !value.getFkTable().trim().isEmpty() &&
            !value.getFkTable().trim().equalsIgnoreCase("null")) {
                Boolean valid = false;
                final Object fkValue = columnsWithValues.get(value.getOrder());
                if (isNull(fkValue)) throw new IllegalArgumentException();
                final Table table = Database.getTable(value.getFkTable());
                final MetaDataTable metaDataForFKTable = Database.getMetaDataForTable(value.getFkTable());
                final Integer order = metaDataForFKTable.getTableMetaData().get(value.getFkColumn()).getOrder();
                for (List<Object> row : table.getRows().values()) {
                    if (fkValue.equals(row.get(order))) {
                        valid = true;
                    }
                }
                if (!valid) throw new IllegalArgumentException();
            }
        });
    }


    private List<TableMetaDataModel> getDataModels(MetaDataTable metaDataForTable) {
        return metaDataForTable.getTableMetaData().values().stream()
                .sorted(comparing(TableMetaDataModel::getOrder)).collect(Collectors.toList());
    }

    private List<Integer> getIndices(ArrayList<TableMetaDataModel> metaDataModels, List<String> columns) {
        return metaDataModels.stream()
                .filter(tableMetaDataModel -> columns.contains(tableMetaDataModel.getColName()))
                .map(TableMetaDataModel::getOrder)
                .collect(Collectors.toList());
    }
}
