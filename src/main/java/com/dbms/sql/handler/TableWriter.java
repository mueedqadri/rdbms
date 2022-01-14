package com.dbms.sql.handler;

import com.dbms.Context;
import com.dbms.constants.DatabaseConstants;
import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.model.TableMetaDataModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dbms.constants.DatabaseConstants.*;
import static com.dbms.constants.GlobalConstants.PIPE;

public class TableWriter {

    public static boolean writeMetaData(MetaDataTable metaDataTable )  {
        String filename = DBMS + "/" + Database.name + "/" + META + "/" + metaDataTable.getName();
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.append(DatabaseConstants.META_HEADER);
            fileWriter.flush();
            for (Map.Entry<String, TableMetaDataModel> row :metaDataTable.getTableMetaData().entrySet()) {
                String line;
                TableMetaDataModel data = row.getValue();
                line = data.getColName() + PIPE + data.getColType() + PIPE + data.getPk()+ PIPE + data.isNotNull()
                        + PIPE + data.isAi() + PIPE + data.getFkTable() + PIPE + data.getFkColumn() +PIPE + data.getOrder() + "\n";
                fileWriter.append(line);
                fileWriter.flush();
            }
            return true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean writeTable(Table table){
        String filename = DBMS + "/" + Database.name+ "/" + TABLES + "/" + table.getTableName();
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.flush();
            for ( Map.Entry<Object, List<Object>> row:table.getRows().entrySet()) {
                StringBuilder line = new StringBuilder();
                for (Object col: row.getValue()){
                    line.append(col);
                    line.append(PIPE);
                }
                line.append(System.lineSeparator());
                fileWriter.append(line);
                fileWriter.flush();
            }
            return true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void writeRow(Table table, List<Object> row) {
        String filename = DatabaseConstants.DBMS + "/" + Database.name + "/"
                + DatabaseConstants.TABLES + "/" + table.getTableName();
        final String rowString = row.stream().map(Object::toString).collect(Collectors.joining(PIPE));
        try (FileWriter fileWriter = new FileWriter(filename, true)) {
            fileWriter.append(rowString);
            fileWriter.append(System.lineSeparator());
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    public static boolean appendUserTable( List<Object> details) {
        String filename = DBMS+"/"+USER_DETAILS+"/"+USER_CREDENTIAL;
        Map<Object, List<Object>> users = TableReader.readUserCred();
        try (FileWriter fileWriter = new FileWriter(filename, true)) {
            fileWriter.flush();
            StringBuilder line = new StringBuilder();
            if(users.isEmpty()){
                line.append(USER_CRED_HEADER);
            }
            line.append(PIPE);
            for (Object col: details){
                line.append(col);
                line.append(PIPE);
            }
            fileWriter.append(String.valueOf(line))
                    .append(System.lineSeparator());
            fileWriter.flush();
            return true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean addUserDbMapping(String database){
        String filename = DBMS+"/"+USER_DETAILS+"/"+USER_TO_DB;
        try (FileWriter fileWriter = new FileWriter(filename, true)) {
            fileWriter.flush();
            StringBuilder line = new StringBuilder();
            line.append(database);
            line.append(PIPE);
            line.append(Context.currentUser.getUserName());
            fileWriter.append(String.valueOf(line))
                    .append(System.lineSeparator());
            fileWriter.flush();
            return true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean createDatabase(String database){
        String databasePath =   DBMS +"/"+ database;
        File theDir = new File(databasePath);
        if (!theDir.exists()){
            theDir.mkdirs();
            File tables = new File(databasePath+"/"+ META);
            File meta = new File(databasePath+"/"+ TABLES);
            tables.mkdirs();
            meta.mkdirs();
            return true;
        }
        return false;
    }

    public static boolean createDump(String dump){
        String dumpPath =   DUMP +"/"+ "Dump-"+LocalDate.now();
        File dumpFolder = new File(DUMP);
        File dumpFile = new File(dumpPath);
        try {
            if(!dumpFolder.exists()
                    && !dumpFolder.mkdirs()
                    && !dumpFile.createNewFile()){
                return false;
            }
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter fileWriter = new FileWriter(dumpFile, true)) {
            fileWriter.flush();
            fileWriter.append(dump);
            fileWriter.flush();
            return true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean deleteDatabase(String database){
        String databasePath =   DBMS +"/"+ database;
        File databaseFolder = new File(databasePath);
        if (databaseFolder.exists()){
            return deleteDirectory(databaseFolder);
        }
        return false;
    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static boolean deleteTable(String tableName){
        String metaPath =   DBMS +"/"+ Database.name+"/"+META+"/"+tableName;
        String tablePath =   DBMS +"/"+ Database.name+"/"+TABLES+"/"+tableName;
        File table = new File(tablePath);
        File meta = new File(metaPath);
        if(table.exists() && table.isFile() && meta.exists() && meta.isFile()){
            return table.delete() && meta.delete();
        }
        return false;
    }

    public static void writeTableData(String tableName, String content) {
        String filename = DatabaseConstants.DBMS + "/" + Database.name + "/"
                + DatabaseConstants.TABLES + "/" + tableName;
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(content);
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
