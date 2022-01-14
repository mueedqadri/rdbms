package com.dbms.userManagement;

import com.dbms.data.dictionary.Database;
import com.dbms.sql.ExportDump;
import com.dbms.sql.handler.DatabaseLoader;
import com.dbms.sql.handler.ERDiagramHandler;
import com.dbms.sql.handler.QueryHandler;
import com.dbms.userInterface.UserView;

import java.util.Scanner;

public class UserQueryService extends UserAuthService{
    protected UserView view= new UserView();

    protected void dump(){
        Scanner sc = new Scanner(System.in);
        view.selectDbToDump();
        String dataBase = sc.next();
        ExportDump exportDump = new ExportDump(dataBase);
        if(exportDump.dump()){
            view.dumpCreated();
        } else {
            view.dumpFailed();
        }
    }

    public void generateErd(){
        Scanner sc = new Scanner(System.in);
        view.selectDbToErd();
        String dataBase = sc.next();
        Database.setName(dataBase);
        DatabaseLoader loader = new DatabaseLoader();
        loader.load();
        ERDiagramHandler erd =new ERDiagramHandler();
        erd.generate();
        view.erdGenerated();
        Database.setName("");
        Database.reset();
    }

    public void executeQuery() {
        Scanner sc = new Scanner(System.in);
        String input;
        do {
            view.enterQuery();
            input = sc.nextLine();
            if(!input.trim().equals("0")){
                QueryHandler queryHandler = new QueryHandler();
                queryHandler.handleQuery(input);
            } else {
                return;
            }
        }while (true);
    }
}
