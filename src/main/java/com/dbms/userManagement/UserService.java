package com.dbms.userManagement;

import com.dbms.userInterface.UserView;

public class UserService extends UserQueryService implements  IUser{
    public UserView view= new UserView();

    public UserService() {
        super.setAdmin(false);
    }

    public void showHomePage(){
        while (true){
            view.showUserHomePage();
            int input = view.getIntegerInput();
            switch (input){
                case 1:
                    executeQuery();
                    break;
                case 2:
                    dump();
                    break;
                case 3:
                    generateErd();
                    break;
                case 4:
                    return;
                default:view.showInvalidInputNumber();
                break;
            }
        }
    }


}
