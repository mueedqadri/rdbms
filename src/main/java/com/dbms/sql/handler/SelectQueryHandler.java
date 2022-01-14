package com.dbms.sql.handler;

import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.log.DBLogger;
import com.dbms.log.QueryLogModel;
import com.dbms.log.QueryLogger;
import com.dbms.model.TableMetaDataModel;
import com.dbms.sql.handler.interfaces.IHandler;
import com.dbms.sql.parser.SelectQueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;

public class SelectQueryHandler implements IHandler {
    private final SelectQueryParser parser = new SelectQueryParser();

    @Override
    public void handle(String sqlQuery) {
        if (parser.isValid(sqlQuery)) {
            final String tableName = parser.getTableName(sqlQuery);
            final MetaDataTable metaDataForTable = Database.getMetaDataForTable(tableName);
            final ArrayList<TableMetaDataModel> metaDataModels = new ArrayList<>(getDataModels(metaDataForTable));
            final List<String> whereClause = parser.getWhereClause(sqlQuery, metaDataForTable.getTableMetaData());
            final List<String> columns = parser.getColumns(sqlQuery, metaDataModels);

            List<Integer> indexOfSelectColumns = getIndices(metaDataModels, columns);
            Integer indexOfColumn = !whereClause.isEmpty() ?
                    getIndices(metaDataModels, singletonList(whereClause.get(0))).get(0) : null;
            final List<List<Object>> view = getRows(Database.getTable(tableName).getRows(),
                    whereClause, indexOfSelectColumns, indexOfColumn, !whereClause.isEmpty());

            formattedPrint(view, columns);
            logSelect("", tableName, "", view.toString());
        } else {
            DBLogger.logError("Invalid Query.");
        }

    }

    private List<TableMetaDataModel> getDataModels(MetaDataTable metaDataForTable) {
        return metaDataForTable.getTableMetaData().values().stream()
                .sorted(comparing(TableMetaDataModel::getOrder)).collect(Collectors.toList());
    }

    private void logSelect(String subtype, String tableName, String prevValue, String newValue){
        QueryLogModel model = new QueryLogModel();
        model.operation = "SELECT "+ subtype;
        model.tableName = tableName;
        model.columnName = "";
        model.prevValue = prevValue;
        model.newValue = newValue;
        QueryLogger.customLogger(model);
    }

    private void formattedPrint(List<List<Object>> view, List<String> columns){
        StringBuilder sb = new StringBuilder();
        for(String col:columns){
            sb.append(String.format("|  %-20s", col));
        }
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        List<List<String>> stringView =view.stream()
                .map(v -> v.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList())
                ).collect(Collectors.toList());;
        for(List<String> row : stringView){
            for(String col: row){
                sb.append(String.format("|  %-20s", col));
            }
            sb.append(System.lineSeparator());
        }
        System.out.println(sb);
    }

    private List<Integer> getIndices(ArrayList<TableMetaDataModel> metaDataModels, List<String> columns) {
        return metaDataModels.stream()
                .filter(tableMetaDataModel -> columns.contains(tableMetaDataModel.getColName()))
                .map(TableMetaDataModel::getOrder)
                .collect(Collectors.toList());
    }

    private List<List<Object>> getRows(Map<Object, List<Object>> rows, List<String> whereClauseValue, List<Integer> columns,
                                       Integer indexOfColumn, Boolean isWherePresent) {
        return rows.values().stream()
                .filter(row -> !isWherePresent || row.get(indexOfColumn).equals(whereClauseValue.get(1)))
                .map(row -> getViewRow(columns, row))
                .collect(Collectors.toList());
    }

    private List<Object> getViewRow(List<Integer> columns, List<Object> row) {
        return columns.stream().map(col -> row.get(col)).collect(Collectors.toList());
    }
}
