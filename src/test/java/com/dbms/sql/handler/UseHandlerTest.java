package com.dbms.sql.handler;

import com.dbms.Context;
import com.dbms.userManagement.UserAuthModel;
import org.junit.jupiter.api.Test;

public class UseHandlerTest {
    UseQueryHandler handler = new UseQueryHandler();

    @Test
    public void testCreateDataBase() {
        String query = "USE testing3;";
        Context.currentUser = new UserAuthModel();
        Context.currentUser.setUserName("mueed@gmail.com");
        handler.setSqlQuery(query);
        handler.handle();
    }
}
