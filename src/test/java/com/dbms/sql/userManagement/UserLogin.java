
package com.dbms.sql.userManagement;

import com.dbms.userManagement.AdminService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class UserLogin {

    @Test
    public void login() {
        StringBuilder sb = new StringBuilder();
        sb.append("mueed@gmail.com");
        sb.append(" ");
        sb.append("password");
        sb.append(" ");
        sb.append(" ");
        InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        AdminService adminService = new AdminService();
        adminService.login();
    }
}
