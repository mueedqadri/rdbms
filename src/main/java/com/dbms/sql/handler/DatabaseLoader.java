package com.dbms.sql.handler;

import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.data.dictionary.Table;
import com.dbms.model.TableMetaDataModel;
import com.dbms.transaction.Lock;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import static com.dbms.constants.DatabaseConstants.*;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class DatabaseLoader {

    public void load() {
        try {
            Database.reset();
            File folder = new File(DBMS);
            final File databaseFolder = getFolder(Database.name, folder);
            final File tableFolder = getFolder(TABLES, databaseFolder);
            final File metadataFolder = getFolder(META, databaseFolder);
            stream(requireNonNull(tableFolder.listFiles()))
                    .forEach(table -> {
                        try {
                            Lock.tablesLock.put(table.getName(), false);
                            Database.addTable(getTable(table));
                        } catch (FileNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    });
            stream(requireNonNull(metadataFolder.listFiles()))
                    .forEach(metaDataTable -> {
                        try {
                            Database.addMetaDataTable(getMetaDataTable(metaDataTable));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    protected Table getTable(File tableFile) throws FileNotFoundException {
        Table table = new Table();
        table.setTableName(tableFile.getName());
        table.setRows(TableReader.readTable(tableFile));
        return table;
    }

    protected File getFolder(String folderName, File folder) throws FileNotFoundException {
        return stream(requireNonNull(folder.listFiles()))
                .filter(file -> file.isDirectory() && file.getName().matches(folderName))
                .findFirst()
                .orElseThrow(FileNotFoundException::new);
    }

    protected MetaDataTable getMetaDataTable(File metaDataTable) throws FileNotFoundException {
        MetaDataTable metaData = new MetaDataTable();
        metaData.setName(metaDataTable.getName());
        Map<String, TableMetaDataModel> table = TableReader.readMetaData(metaDataTable);
        metaData.setTableMetaData(table);
        return metaData;
    }

}
