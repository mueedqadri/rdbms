package com.dbms.sql.handler;

import com.dbms.model.Pair;
import com.dbms.model.TableMetaDataModel;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.dbms.constants.DatabaseConstants.*;
import static com.dbms.constants.RegexConstants.PIPE;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class TableReader {

    public static Map<Object, List<Object>> readTable(File file) throws FileNotFoundException {
        Map<Object, List<Object>> keyRowMap = new LinkedHashMap<>();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.lines().forEach(line -> {
                final List<Object> row = Arrays.stream(line.split(PIPE)).collect(Collectors.toList());
                keyRowMap.put(row.get(0), row);
            });
            bufferedReader.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return keyRowMap;
    }

    public static Map<String, TableMetaDataModel> readMetaData(File file) {
        AtomicLong count = new AtomicLong();
        Map<String, TableMetaDataModel> keyRowMap = new LinkedHashMap<>();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.lines().forEach(line -> {
                if (count.incrementAndGet() > 1) {
                    final List<Object> row = Arrays.stream(line.split(PIPE)).collect(Collectors.toList());
                    TableMetaDataModel metaDataModel = new TableMetaDataModel();
                    metaDataModel.setColName((String) row.get(0));
                    metaDataModel.setColType((String) row.get(1));
                    metaDataModel.setPk(Boolean.parseBoolean((String) row.get(2)));
                    metaDataModel.setNotNull(Boolean.parseBoolean((String) row.get(3)));
                    metaDataModel.setAi(Boolean.parseBoolean((String) row.get(4)));
                    metaDataModel.setFkTable((String) row.get(5));
                    metaDataModel.setFkColumn((String) row.get(6));
                    metaDataModel.setOrder(Integer.parseInt((String) row.get(7)));
                    keyRowMap.put((String) row.get(0), metaDataModel);
                }
            });
            bufferedReader.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return keyRowMap;
    }

    public static Map<Object, List<Object>> readUserCred() {
        File mainFolder = new File(DBMS+"/"+USER_DETAILS);
        AtomicReference<Map<Object, List<Object>>> keyRowMap = new AtomicReference<>();
        stream(requireNonNull(mainFolder.listFiles()))
                .forEach(file -> {
                    try {
                        if(file.getName().equals(USER_CREDENTIAL)){
                            keyRowMap.set(TableReader.readTable(file));
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                });
        return keyRowMap.get();
    }

    public static List<Pair> getUserDbMapping() {
        File mainFolder = new File(DBMS+"/"+USER_DETAILS);
        List<Pair> keyRowMap = new ArrayList<>() ;
        stream(requireNonNull(mainFolder.listFiles()))
                .forEach(file -> {
                    try {
                        if(file.getName().equals(USER_TO_DB))
                        {
                            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                            bufferedReader.lines().forEach(line -> {
                                final List<Object> row = Arrays.stream(line.split(PIPE)).collect(Collectors.toList());
                                keyRowMap.add(new Pair(row.get(0), row.get(1)));
                            });
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                });
        return keyRowMap;
    }

    public static boolean isExistsDatabase(String database){
        String databasePath =   DBMS +"/"+ database;
        File theDir = new File(databasePath);
        return theDir.exists();
    }
}
