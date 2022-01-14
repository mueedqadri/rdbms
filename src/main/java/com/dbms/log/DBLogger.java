package com.dbms.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.util.Objects.isNull;

public class DBLogger {
    private static Logger logger;

    private static Logger getLogger() {
        if(isNull(logger)) {
            logger = Logger.getLogger("DBMS-GROUP-21");
            try {
                final FileHandler handler = new FileHandler("src/main/resources/log/db.log", true);
                handler.setFormatter(new SimpleFormatter());
                logger.addHandler(handler);
            } catch (IOException e) {
                System.out.println("Error in logging");
            }
        }
        return logger;
    }

    public static void logInfo(String message) {
        getLogger().info(message);
    }

    public static void logError(String message) {
        getLogger().log(Level.SEVERE, message);
    }

    private DBLogger() {
    }
}
