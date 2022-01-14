package com.dbms.sql.handler;

import com.dbms.log.QueryLogModel;
import com.dbms.log.QueryLogger;
import com.dbms.transaction.Lock;
import com.dbms.transaction.TransactionController;
import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.log.DBLogger;
import com.dbms.model.TableMetaDataModel;
import com.dbms.sql.handler.interfaces.IHandler;
import com.dbms.sql.parser.DeleteQueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;

public class DeleteQueryHandler implements IHandler {

    private final DeleteQueryParser deleteQueryParser = new DeleteQueryParser();
    private Lock lock= new Lock();

    @Override
    public void handle(String sqlQuery) {
        if (deleteQueryParser.isValid(sqlQuery)) {
            final String tableName = deleteQueryParser.getTableName(sqlQuery);
            final MetaDataTable metaDataForTable = Database.getMetaDataForTable(tableName);
            final ArrayList<TableMetaDataModel> metaDataModels = new ArrayList<>(getDataModels(metaDataForTable));
            final List<String> whereClause = deleteQueryParser.getWhereClause(sqlQuery, metaDataForTable.getTableMetaData());

            final Table table = Database.getTable(tableName);
            deleteRows(table, whereClause, metaDataModels);
            TransactionController controller = new TransactionController();
            controller.executeUpdate(tableName, getTableContent(table));
        } else {
            DBLogger.logError("Invalid Query.");
        }
    }

    private void logDelete( String tableName, String previousValue){
        QueryLogModel model = new QueryLogModel();
        model.operation = "DELETE ";
        model.tableName = tableName;
        model.columnName = "";
        model.prevValue = previousValue;
        model.newValue = "";
        QueryLogger.customLogger(model);
    }


    private String getTableContent(Table table) {
        return table.getRows().values().stream()
                .map(row -> row.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("|"))
                ).collect(Collectors.joining("\n"));
    }

    private void deleteRows(Table table, List<String> whereClause,
                            ArrayList<TableMetaDataModel> metaDataModels) {
        Integer indexOfColumn = !whereClause.isEmpty() ?
                getIndices(metaDataModels, singletonList(whereClause.get(0))).get(0) : null;
        List<Object> keysToDelete = new ArrayList<>();
        table.getRows().forEach((key, value) -> {
            if(!whereClause.isEmpty() && value.get(indexOfColumn).equals(whereClause.get(1))) {
                keysToDelete.add(key);
            }
        });
        keysToDelete.forEach(key -> {
            final List<Object> objects = table.getRows().get(key);
            logDelete(table.getTableName(), objects.toString());
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
