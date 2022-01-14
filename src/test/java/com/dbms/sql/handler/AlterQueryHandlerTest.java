package com.dbms.sql.handler;

import com.dbms.transaction.TransactionController;
import com.dbms.data.dictionary.Database;
import org.junit.jupiter.api.Test;

public class AlterQueryHandlerTest {
    @Test
    public void addTest() {
        TransactionController controller = new TransactionController();
        String query = "alter TABLE WithAllOptionalToken  \n" +
            "ADD cus_age varchar(40) NN;";
        DatabaseLoader databaseLoader = new DatabaseLoader();
        AlterQueryHandler handler = new AlterQueryHandler();
        TransactionController.isTransaction = true;
        Database.setName("university");
        databaseLoader.load();
        handler.handle(query);
    }

    @Test
    public void modifyTest() {
        TransactionController controller = new TransactionController();
        String query = "alter TABLE new  \n" +
                "MODIFY pay int ;";
        DatabaseLoader databaseLoader = new DatabaseLoader();
        AlterQueryHandler handler = new AlterQueryHandler();
        TransactionController.isTransaction = true;
        Database.setName("university");
        databaseLoader.load();
        handler.handle(query);
        controller.commit();
    }
}