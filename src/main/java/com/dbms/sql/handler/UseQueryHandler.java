package com.dbms.sql.handler;

import com.dbms.Context;
import com.dbms.data.dictionary.Database;
import com.dbms.log.DBLogger;
import com.dbms.model.Pair;
import com.dbms.sql.parser.UseParser;

import java.util.List;

public class UseQueryHandler {

    private final UseParser parser = new UseParser();
    private String sqlQuery;

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public boolean handle(){
        if(parser.isValid(sqlQuery)){
            String database = parser.getDatabase(sqlQuery);
            List<Pair> dbUserMap = TableReader.getUserDbMapping();
            for(Pair item : dbUserMap){
                if (item.key.equals(database)
                        && item.value.equals(Context.currentUser.getUserName())){
                    Database.setName(database);
                    DatabaseLoader databaseLoader = new DatabaseLoader();
                    databaseLoader.load();
                    DBLogger.logInfo(database + " has been selected");
                    return true;
                }
            }
            DBLogger.logError("You are no authorized access the database.");
        }else {
            DBLogger.logError("Invalid syntax");
        }

        return false;
    }

}
