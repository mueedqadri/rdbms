package com.dbms.userManagement;

public class UserAuthModel {
    private int id;
    private String userName;
    private String password;
    private String secQuestion1;
    private String secAnswer1;
    private String secQuestion2;
    private String secAnswer2;
    private String secQuestion3;
    private String secAnswer3;
    private boolean isAdmin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSecQuestion1(String secQuestion1) {
        this.secQuestion1 = secQuestion1;
    }

    public void setSecAnswer1(String secAnswer1) {
        this.secAnswer1 = secAnswer1;
    }

    public void setSecQuestion2(String secQuestion2) {
        this.secQuestion2 = secQuestion2;
    }

    public void setSecAnswer2(String secAnswer2) {
        this.secAnswer2 = secAnswer2;
    }

    public void setSecQuestion3(String secQuestion3) {
        this.secQuestion3 = secQuestion3;
    }

    public void setSecAnswer3(String secAnswer3) {
        this.secAnswer3 = secAnswer3;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getSecQuestion1() {
        return secQuestion1;
    }

    public String getSecAnswer1() {
        return secAnswer1;
    }

    public String getSecQuestion2() {
        return secQuestion2;
    }

    public String getSecAnswer2() {
        return secAnswer2;
    }

    public String getSecQuestion3() {
        return secQuestion3;
    }

    public String getSecAnswer3() {
        return secAnswer3;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
