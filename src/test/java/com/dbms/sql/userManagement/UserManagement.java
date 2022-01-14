package com.dbms.sql.userManagement;

import com.dbms.userManagement.AdminService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class UserManagement {

    @Test
    public void createNewUser() {
        StringBuilder sb = new StringBuilder();
        sb.append("mueed@gmail.com ");
        sb.append("password ");
        sb.append("Where do you live?\n");
        sb.append("Canada\n");
        sb.append("When is your Birthday?\n");
        sb.append("July\n");
        sb.append("How tall are you?\n");
        sb.append("6\n");
        sb.append("true\n");
        InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        AdminService adminService = new AdminService();
        adminService.newUser();
    }
}
