package com.dbms;

import com.dbms.log.DBLogger;
import com.dbms.userInterface.HomePage;
import com.dbms.userManagement.AdminService;
import com.dbms.userManagement.IUser;
import com.dbms.userManagement.UserService;

public class Application {
    private static HomePage view =new HomePage();

    public static void main(String[] args) {
        IUser user;
        try {
            handle();
        } catch (Exception exception) {
            DBLogger.logError(exception.getMessage());
        }
    }

    private static void handle() {
        IUser user;
        while (true){
            view.showHomePage();
            switch (view.getIntegerInput()){
                case 1:
                    user = new AdminService();
                    show(user);
                    break;
                case 2:
                    user = new UserService();
                    show(user);
                    break;
                case 3:
                    return;
                default:
                    view.showInvalidInputNumber();
                    break;
            }
        }
    }

    private static void show(IUser user) {
        if(user != null && user.login()){
            user.showHomePage();
        }
    }
}
