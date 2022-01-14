package com.dbms.sql.handler;

import com.dbms.Context;
import com.dbms.data.dictionary.Database;
import com.dbms.userManagement.UserAuthModel;
import org.junit.jupiter.api.Test;

public class DropQueryHandlerTest {
    private DropQueryHandler drop = new DropQueryHandler();

    @Test
    public void testDropDataBase() {
        String query = "DROP DATABASE testing3;";
        Context.currentUser = new UserAuthModel();
        Context.currentUser.setUserName("mueed@gmail.com");
        drop.setSqlQuery(query);
        drop.handle();
    }


    @Test
    public void testDropTable() {
        String query = "DROP table new;";
        Database.setName("testing3");
        Context.currentUser = new UserAuthModel();
        Context.currentUser.setUserName("mueed@gmail.com");
        drop.setSqlQuery(query);
        drop.handle();
    }
}
