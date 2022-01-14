package com.dbms.sql.handler;

import com.dbms.transaction.TransactionController;
import com.dbms.data.dictionary.Database;
import com.dbms.log.DBLogger;
import com.dbms.log.QueryLogger;
import com.dbms.userInterface.UserView;

import java.util.Locale;

public class QueryHandler {
    UserView view = new UserView();
    TransactionController controller = new TransactionController();

    public void handleQuery(String query) {
        final String[] split = query.split("\\s");
        String keyword = split[0];
        handleKeyWord(query, keyword);
    }

    private void handleKeyWord(String query, String keyword) {
        if(Database.name == null || Database.name.isEmpty()){
            switch (keyword.toLowerCase(Locale.ROOT)){
                case "use":
                    UseQueryHandler useQueryHandler = new UseQueryHandler();
                    useQueryHandler.setSqlQuery(query);
                    useQueryHandler.handle();
                    break;
                case "create":
                    CreateQueryHandler handler = new CreateQueryHandler();
                    handler.setSqlQuery(query);
                    handler.handle();
                    break;
                case "drop":
                    DropQueryHandler dropQueryHandler = new DropQueryHandler();
                    dropQueryHandler.setSqlQuery(query);
                    dropQueryHandler.handle();
                    break;
                default:
                    view.selectDatabase();
                    break;
            }
        }else {
            switch (keyword.toLowerCase()) {
                case "start":
                    TransactionController.isTransaction = true;
                    DBLogger.logInfo("Transaction Started");
                    QueryLogger.logTransactionState(keyword.toLowerCase(Locale.ROOT));
                    break;
                case "commit":
                    controller.commit();
                    QueryLogger.logTransactionState(keyword.toLowerCase(Locale.ROOT));
                    break;
                case "rollback":
                    controller.rollBack();
                    QueryLogger.logTransactionState(keyword.toLowerCase(Locale.ROOT));
                    break;
                case "create":
                    CreateQueryHandler handler = new CreateQueryHandler();
                    handler.setSqlQuery(query);
                    handler.handle();
                    break;
                case "alter":
                    AlterQueryHandler alterQueryHandler = new AlterQueryHandler();
                    alterQueryHandler.handle(query);
                    break;
                case "delete":
                    DeleteQueryHandler deleteQueryHandler = new DeleteQueryHandler();
                    deleteQueryHandler.handle(query);
                    break;
                case "insert":
                    InsertQueryHandler insertQueryHandler = new InsertQueryHandler();
                    insertQueryHandler.handle(query);
                    break;
                case "update":
                    UpdateQueryHandler updateQueryHandler = new UpdateQueryHandler();
                    updateQueryHandler.handle(query);
                    break;
                case "select":
                    SelectQueryHandler selectQueryHandler = new SelectQueryHandler();
                    selectQueryHandler.handle(query);
                    break;
                case "drop":
                    DropQueryHandler dropQueryHandler = new DropQueryHandler();
                    dropQueryHandler.setSqlQuery(query);
                    dropQueryHandler.handle();
                    break;
                case "use":
                    UseQueryHandler useQueryHandler = new UseQueryHandler();
                    useQueryHandler.setSqlQuery(query);
                    useQueryHandler.handle();
                    break;
                default:
                    DBLogger.logError("Invalid Query.");
                    break;
            }
        }
    }
}
