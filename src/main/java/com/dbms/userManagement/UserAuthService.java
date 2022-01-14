package com.dbms.userManagement;

import com.dbms.Context;
import com.dbms.Utils;
import com.dbms.sql.handler.TableReader;
import com.dbms.userInterface.AuthenticationView;

import java.util.List;
import java.util.Random;

public class UserAuthService {
    private AuthenticationView view = new AuthenticationView();
    private boolean isAdmin;
    private String userName;
    private UserAuthModel user;
    private List<Object> rawUser;

    private UserAuthModel transformData(List<Object> usersRaw){
        UserAuthModel user = new UserAuthModel();
        user.setUserName((String) usersRaw.get(0));
        user.setPassword((String) usersRaw.get(1));
        user.setSecQuestion1((String) usersRaw.get(2));
        user.setSecAnswer1((String) usersRaw.get(3));
        user.setSecQuestion2((String) usersRaw.get(4));
        user.setSecAnswer2((String) usersRaw.get(5));
        user.setSecQuestion3((String) usersRaw.get(6));
        user.setSecAnswer3((String) usersRaw.get(7));
        user.setAdmin(Boolean.parseBoolean((String) usersRaw.get(8)));
        return user;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean login() {
        do{
            view.showEnterUserName();
            userName = view.getValidEmailInput();
            view.showEnterPassword();
            String enteredPassword = view.getStringInputNoValidation();
            rawUser = TableReader.readUserCred().get(userName);
            if(rawUser!=null && !rawUser.isEmpty()){
                user = transformData(rawUser);
                String password = getHashPassword();
                if(isAdmin == user.isAdmin()){
                    if (password.equals(Utils.encryptToSha256(enteredPassword))) {
                        int random = getRandomQuestion();
                        view.answerSecurityQuestion((String) rawUser.get(random));
                        if(view.getStringInputNoValidation().equals(rawUser.get(random+1))){
                            Context.currentUser =user;
                            return true;
                        }
                    }
                }
            }
            view.checkCredentials();
        }while ( view.getIntegerInput() != 1);
        return false;
    }

    private String getHashPassword(){
        return user.getPassword();
    }

    private int getRandomQuestion(){
        int[] arr = new int[] { 2, 4, 6};
        int idx = new Random().nextInt(arr.length);
        return arr[idx];
    }
}
