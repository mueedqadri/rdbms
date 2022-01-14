package com.dbms.transaction;

import java.util.HashMap;
import java.util.Map;

public class Lock {
    public static Map<String, Boolean> tablesLock=  new HashMap<>();
    private Thread currentWriteLockOwner;

    public synchronized void acquireExclusiveLock(String tableName)  {
        try {
            while(tablesLock.get(tableName)) {
                wait();
            }
            currentWriteLockOwner = Thread.currentThread();
            tablesLock.put(tableName, !tablesLock.get(tableName) );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void releaseExclusiveLock(){
        for (var table: Lock.tablesLock.entrySet()){
            table.setValue(false);
        }
        currentWriteLockOwner = null;
        notifyAll();
    }

}
