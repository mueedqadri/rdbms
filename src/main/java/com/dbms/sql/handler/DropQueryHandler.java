package com.dbms.sql.handler;

import com.dbms.Context;
import com.dbms.transaction.TransactionController;
import com.dbms.data.dictionary.Database;
import com.dbms.log.DBLogger;
import com.dbms.log.QueryLogModel;
import com.dbms.log.QueryLogger;
import com.dbms.model.Pair;
import com.dbms.sql.parser.DropQueryParser;

import java.util.List;
import java.util.Locale;

import static com.dbms.constants.GlobalConstants.DATABASE;
import static com.dbms.constants.GlobalConstants.TABLE;

public class DropQueryHandler {

    private final DropQueryParser parser = new DropQueryParser();
    private String sqlQuery;

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public void handle(){
        if(parser.isValid(sqlQuery)){
            switch (parser.getDropOperationType(sqlQuery).toLowerCase(Locale.ROOT)){
                case TABLE:
                    dropTable();
                    break;
                case DATABASE:
                    dropDatabase();
                    break;
                default:
                    //TODO not valid
            }
        }
    }

    private void dropDatabase(){
        if(!TransactionController.isTransaction){
            String database = parser.getEntityName(sqlQuery);
            List<Pair> dbUserMap = TableReader.getUserDbMapping();
            for(Pair item : dbUserMap){
                if (item.key.equals(database)
                        && item.value.equals(Context.currentUser.getUserName())){
                    Database.setName("");
                    if(TableWriter.deleteDatabase(database)){
                        logDrop(DATABASE, database);
                        DBLogger.logInfo("Drop successful ");
                        return;
                    }
                }
            }
            DBLogger.logError("You are not authorized to drop "+database);
        }
    }

    private void logDrop(String subtype, String tableName){
        QueryLogModel model = new QueryLogModel();
        model.operation = "DROP "+subtype;
        model.tableName = tableName;
        model.columnName = "";
        model.prevValue = "";
        model.newValue = "";
        QueryLogger.customLogger(model);
    }

    private void dropTable(){
        if(Database.getTables().size() == 0){
            DBLogger.logError("No database has been selected ");
        }else{
            String tableName = parser.getEntityName(sqlQuery);
            if(Database.getTables().containsKey(tableName)){
                TransactionController controller= new TransactionController();
                controller.dropTable(tableName);
                logDrop(TABLE, tableName);
                DBLogger.logInfo("Drop successful ");
            }else {
                DBLogger.logError("Table does not exist");
            }
        }
    }
}
