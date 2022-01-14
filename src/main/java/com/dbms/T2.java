package com.dbms;

import com.dbms.sql.handler.QueryHandler;
import com.dbms.userManagement.UserAuthModel;

public class T2 implements Runnable{

    @Override
    public void run() {
        String use = "use dataDb;";
        String start = "start transaction;";
        String alter = "alter table people rename name newname ;";
        String commit = "commit transaction";
        QueryHandler handler = new QueryHandler();
        Context.currentUser = new UserAuthModel();
        Context.currentUser.setUserName("mueed@dal.ca");
        handler.handleQuery(use);
        handler.handleQuery(start);
        handler.handleQuery(alter);
        handler.handleQuery(commit);
    }
}
