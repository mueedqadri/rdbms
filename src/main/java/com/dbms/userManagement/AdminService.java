package com.dbms.userManagement;

import com.dbms.constants.GlobalConstants;
import com.dbms.sql.handler.TableWriter;

import java.util.LinkedHashMap;
import java.util.Map;

public class AdminService extends UserQueryService implements  IUser{

    public AdminService() {
        super.setAdmin(true);
    }

    public void showHomePage(){
        while (true){
            view.showAdminHomePage();
            int input = view.getIntegerInput();
            switch (input){
                case 1:
                    newUser();
                    break;
                case 2:
                    executeQuery();
                    break;
                case 3:
                    dump();
                    break;
                case 4:
                    generateErd();
                    break;
                case 5:
                    return;
                default:
                    view.showInvalidInputNumber();
            }
        }
    }

    public boolean newUser(){
        Map<String, Object> userDetails = prepareDetails();
        userDetails = view.addNewCustomer(userDetails);
        TableWriter.appendUserTable( userDetails.values().stream().toList());
        return true;
    }


    private Map<String, Object> prepareDetails(){
        return new LinkedHashMap<String, Object>() {{
            put(GlobalConstants.USERNAME, null);
            put(GlobalConstants.PASSWORD, null);
            put(GlobalConstants.SEC_QUESTION_1, null);
            put(GlobalConstants.SEC_ANSWER_1, null);
            put(GlobalConstants.SEC_QUESTION_2, null);
            put(GlobalConstants.SEC_ANSWER_2, null);
            put(GlobalConstants.SEC_QUESTION_3, null);
            put(GlobalConstants.SEC_ANSWER_3, null);
            put(GlobalConstants.IS_ADMIN, null);
        }};
    }
}
