package com.dbms.sql;

import com.dbms.Context;
import com.dbms.model.Pair;
import com.dbms.sql.handler.DatabaseLoader;
import com.dbms.sql.handler.TableReader;
import com.dbms.sql.handler.TableWriter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.dbms.constants.DatabaseConstants.*;
import static com.dbms.constants.GlobalConstants.*;
import static com.dbms.constants.RegexConstants.PIPE;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class ExportDump extends DatabaseLoader {
    private static final StringBuilder sb = new StringBuilder();
    private String databaseName;

    public ExportDump(String databaseName){
        this.databaseName = databaseName;
        sb.append(CREATE)
                .append(SINGLE_SPACE)
                .append(DATABASE.toUpperCase(Locale.ROOT))
                .append(SINGLE_SPACE)
                .append(databaseName)
                .append(TERMINATE)
                .append(System.lineSeparator());
    }

    public boolean dump() {
        try {
            List<Pair> dbUserMap = TableReader.getUserDbMapping();
            for(Pair item : dbUserMap){
                if (item.key.equals(databaseName)
                        && item.value.equals(Context.currentUser.getUserName())){
                    File folder = new File(DBMS);
                    final File databaseFolder = getFolder(databaseName, folder);
                    final File tableFolder = getFolder(TABLES, databaseFolder);
                    final File metadataFolder = getFolder(META, databaseFolder);
                    stream(requireNonNull(metadataFolder.listFiles()))
                            .forEach(ExportDump::readMetaData);
                    stream(requireNonNull(tableFolder.listFiles()))
                            .forEach(ExportDump::readTable);
                    return TableWriter.createDump(sb.toString());
                }else{
                    //TODO database access not granted
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static void readTable(File file)  {
        int count = 0;
        String columnNames ="";
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            List<String> bfr = bufferedReader.lines().toList();
            for(String s: bfr){
                final List<Object> row = Arrays.stream(s.split(PIPE)).collect(Collectors.toList());
                if(count == 0){
                    columnNames= row.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(","));
                }else {
                    String values = row.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(","));
                    sb.append(INSERT)
                            .append(SINGLE_SPACE)
                            .append(INTO)
                            .append(SINGLE_SPACE)
                            .append(file.getName())
                            .append(SINGLE_SPACE)
                            .append(OPEN_BRACE)
                            .append(columnNames)
                            .append(CLOSE_BRACE)
                            .append(SINGLE_SPACE)
                            .append(VALUES)
                            .append(OPEN_BRACE)
                            .append(values)
                            .append(SINGLE_SPACE)
                            .append(CLOSE_BRACE)
                            .append(TERMINATE)
                            .append(System.lineSeparator());

                }
                count++;
            }
            bufferedReader.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static void readMetaData(File file) {
        int count = 0;
        sb.append(CREATE)
                .append(SINGLE_SPACE)
                .append(TABLE.toUpperCase(Locale.ROOT))
                .append(SINGLE_SPACE)
                .append(file.getName())
                .append(SINGLE_SPACE)
                .append(OPEN_BRACE);
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            List<String> bfr = bufferedReader.lines().toList();
            for(String s: bfr){
                if (count >= 1) {
                    final List<Object> row = Arrays.stream(s.split(PIPE)).collect(Collectors.toList());
                    sb.append(row.get(0)).append(SINGLE_SPACE);
                    sb.append(row.get(1)).append(SINGLE_SPACE);
                    if (Boolean.parseBoolean((String) row.get(2))) {
                        sb.append(PRIMARY_KEY).append(SINGLE_SPACE);
                    }
                    if (Boolean.parseBoolean((String) row.get(3))) {
                        sb.append(NOT_NULL).append(SINGLE_SPACE);
                    }
                    if (Boolean.parseBoolean((String) row.get(4))) {
                        sb.append(AUTO_INCREMENT).append(SINGLE_SPACE);
                    }
                    if(count+1 < bfr.size()){
                        sb.append(COMMA).append(SINGLE_SPACE);
                    }
                }
                count ++;
            }
            sb.append(CLOSE_BRACE).append(TERMINATE).append(System.lineSeparator());
            bufferedReader.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
