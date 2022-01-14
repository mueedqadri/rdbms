package com.dbms.sql;

import com.dbms.Context;
import com.dbms.userManagement.UserAuthModel;
import org.junit.jupiter.api.Test;

public class SqlDumpTest {
    @Test
    public void testCreateDataBase() {
        ExportDump dump = new ExportDump("university");
        Context.currentUser = new UserAuthModel();
        Context.currentUser.setUserName("mueed@gmail.com");
        dump.dump();
    }
}
