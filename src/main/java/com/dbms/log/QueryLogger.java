package com.dbms.log;

import com.dbms.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class QueryLogger {

    private static void write(final String s){
        try {
            Files.writeString(
                    Path.of( "src/main/resources/log/databaseLog.log"),
                    s + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        }
        catch (IOException e){
            System.out.println( e.getMessage());
        }

    }
    public static void customLogger( QueryLogModel log)  {
        try {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
            write(String.format("%-20s %-20s %-20s %-30s %-20s %-30s %-20s",
                     log.operation, log.tableName ,log.prevValue,log.newValue,log.columnName, timeStamp,
                    Context.currentUser.getUserName()));
        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }

    public static void logTransactionState(String transactionState){
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        write(String.format(" %-51s %-40s %-20s %-30s  %-18s", "", "***** "+transactionState.toUpperCase(Locale.ROOT)+" Transaction  *****", "", timeStamp,
                Context.currentUser.getUserName()));
    }
}
