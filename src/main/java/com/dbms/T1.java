package com.dbms;

import com.dbms.Context;
import com.dbms.sql.handler.QueryHandler;
import com.dbms.userManagement.UserAuthModel;

public class T1 implements Runnable{

    @Override
    public void run() {
        String use = "use dataDb;";
        String start = "start transaction;";
        String alter = "alter table people rename dob age ;";
        String commit = "commit transaction";
        QueryHandler handler= new QueryHandler();
        Context.currentUser = new UserAuthModel();
        Context.currentUser.setUserName("mueed@gmail.com");
        handler.handleQuery(use);
        handler.handleQuery(start);
        handler.handleQuery(alter);
        handler.handleQuery(commit);
    }
}
