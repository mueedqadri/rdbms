package com.dbms.sql;

import com.dbms.Context;
import com.dbms.userManagement.UserAuthModel;
import com.dbms.userManagement.UserQueryService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class QueryHandlerTest {
    @Test
    public void testQueryHandler(){

        String sb = "use dataDb;\nstart transaction;\nselect * from people;\nselect * from people;\n0";
//        String sb = "use dataDb;\nalter table persons add address text ;\n0";
//        String sb = "use dataDb;\nalter table persons drop address ;\n0";
//        String sb = "use dataDb;\nalter table persons modify dob text pk;\n0";
//        String sb = "use dataDb;\nalter table people rename age dob ;\n0";
        InputStream in = new ByteArrayInputStream(sb.getBytes());
        System.setIn(in);
        Context.currentUser = new UserAuthModel();
        Context.currentUser.setUserName("mueed@gmail.com");
        UserQueryService service= new UserQueryService();
        service.executeQuery();
    }
}
