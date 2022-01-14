package com.dbms.sql.handler;

import com.dbms.constants.DataTypes.DataType;
import com.dbms.constants.GlobalConstants;
import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.log.DBLogger;
import com.dbms.log.QueryLogModel;
import com.dbms.log.QueryLogger;
import com.dbms.model.TableMetaDataModel;
import com.dbms.sql.handler.interfaces.IHandler;
import com.dbms.sql.parser.AlterQueryParser;
import com.dbms.sql.parser.OperationParser;
import com.dbms.transaction.TransactionController;

import java.util.Locale;
import java.util.NoSuchElementException;

public class AlterQueryHandler extends OperationParser implements IHandler {

    private final TransactionController controller = new TransactionController();
    private MetaDataTable currentTableMeta;
    private Table currentTable;
    private String tableName;
    private String operation;

    @Override
    public void handle(String string) {
        AlterQueryParser parser = new AlterQueryParser();
        if (parser.isValid(string)) {
            tableName = parser.getTableGroup(string);
            currentTableMeta = Database.getMetaDataForTable(tableName);
            currentTable = Database.getTable(tableName);
            if(Database.getTables().containsKey(tableName)){
                operation = parser.getAlterOperation(string);
                String operationType = parser.getAlterOperationType(string);
                executeAlter(operationType);
            } else {
                DBLogger.logError("Table not found.");
            }
        }else {
            DBLogger.logError("Invalid Query");
        }
    }

    public void executeAlter(String operationType){
        switch (operationType.toLowerCase(Locale.ROOT)){
            case GlobalConstants.ADD:
                add();
                break;
            case GlobalConstants.RENAME:
                rename();
                break;
            case GlobalConstants.MODIFY:
                modify();
                break;
            case GlobalConstants.DROP:
                drop();
                break;
        }
    }

    private void logAlterTable(String subtype, String columnName, String prevValue, String newValue){
        QueryLogModel model = new QueryLogModel();
        model.operation = "ALTER "+subtype;
        model.tableName = tableName;
        model.columnName = columnName;
        model.prevValue = prevValue;
        model.newValue = newValue;
        QueryLogger.customLogger(model);
    }

    public void rename(){
        String columnName = getColumnName(operation);
        if(currentTableMeta.getTableMetaData().containsKey(columnName)){
            TableMetaDataModel row = currentTableMeta.getTableMetaData().get(columnName);
            row.setColName(getColumnNewName(operation));
            controller.executeMetaTableWrite(currentTableMeta);
            logAlterTable("RENAME", columnName, getColumnName(operation), getColumnNewName(operation));
        }else {
            DBLogger.logError("Column does not exists.");
        }
    }

    public int getMaxOrder(){
        return currentTableMeta.getTableMetaData()
                .values()
                .stream()
                .mapToInt(TableMetaDataModel::getOrder)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    public void add( ){
        TableMetaDataModel metaDataModel = new TableMetaDataModel();
        String columnName = getColumnName(operation);
        if(!currentTableMeta.getTableMetaData().containsKey(columnName)){
            metaDataModel.setColName(columnName);
            metaDataModel.setColType(getDataType(operation));
            metaDataModel.setPk(getPrimaryKey(operation));
            metaDataModel.setAi(getAutoIncrement(operation));
            metaDataModel.setNotNull(getNotNull(operation));
            metaDataModel.setFkColumn(" ");
            metaDataModel.setFkTable(" ");
            metaDataModel.setOrder(getMaxOrder()+1);
            for(var row : currentTable.getRows().values()){
                    row.add(" ");
            }
            currentTableMeta.addRow(metaDataModel);
            controller.executeMetaTableWrite(currentTableMeta);
            controller.executeTableWrite(currentTable);
            logAlterTable("ADD", columnName, "", operation);
        } else{
            DBLogger.logError("Column already exists.");
        }
    }

    public void drop(){
        String columnName = getColumnName(operation);
        if(currentTableMeta.getTableMetaData().containsKey(columnName)){
            int order = currentTableMeta.getTableMetaData().get(columnName).getOrder();
            for(var row : currentTable.getRows().values()){
                row.remove(order);
            }
            for(var row : currentTableMeta.getTableMetaData().values()){
                int currRowOrder = row.getOrder();
                if(currRowOrder > order){
                    row.setOrder(currRowOrder);
                }
            }
            currentTableMeta.getTableMetaData().remove(columnName);
            controller.executeMetaTableWrite(currentTableMeta);
            controller.executeTableWrite(currentTable);
        }else {
            DBLogger.logError("Column does not exists.");
        }
    }

    private boolean checkLegalCasting(String prevType){
        String newType = getDataType(operation);
        boolean isStringToText = prevType.equalsIgnoreCase(DataType.VARCHAR.name())
                && newType.equalsIgnoreCase(DataType.TEXT.name());
        boolean isPrevTypeString = prevType.equalsIgnoreCase(DataType.TEXT.name())
                || prevType.equalsIgnoreCase(DataType.VARCHAR.name());
        return prevType.equalsIgnoreCase(newType) || isStringToText || !isPrevTypeString;
    }

    public void modify( ){
        String columnName = getColumnName(operation);
        Table table = Database.getTables().get(tableName);
        TableMetaDataModel prevColumn = currentTableMeta.getTableMetaData().get(columnName);
        String prevColType = prevColumn.getColType().toUpperCase(Locale.ROOT);
        boolean noForeignKey = prevColumn.getFkColumn().trim().isEmpty() && prevColumn.getFkTable().trim().isEmpty();
        if( table.getRows().isEmpty() ||( noForeignKey && !getAutoIncrement(operation)
                && !getNotNull(operation)
                && checkLegalCasting(prevColType))){
            prevColumn.setNotNull(getNotNull(operation));
            prevColumn.setAi(getNotNull(operation));
            prevColumn.setColType(getDataType(operation));
            controller.executeMetaTableWrite(currentTableMeta);
            logAlterTable("MODIFY", columnName, "", operation);
        }else {
            DBLogger.logError("Alter operation not supported.");
        }
    }
}
