package com.dbms.sql.handler;

import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.model.TableMetaDataModel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.dbms.constants.DatabaseConstants.DBMS;

public class ERDiagramHandler {

  final String ARROW = " ---------------> ";
  final String ONETOMANY = "-1-----------N-";
  final String ONETOONE = "-1-----------1-";
//  final String MANYTOMANY = "-N-----------N-";
  final String MANYTOONE = "-N-----------1-";

  public void generate() {

    Map<String, MetaDataTable> allTableMetaData =
        Database.getMetaDataAllTables();
    Map<String, String> relationMap = this.getAllRelation();

    String result = "";
    //table: table data
    for (Map.Entry<String, MetaDataTable> tableMeta : allTableMetaData.entrySet()) {
      String table = "";
      String relation = "\nRelations : \n";
      String columns = "\nColumns : \n";
      String foeignKey = "\nForeign Keys : \n";

      String tableName = tableMeta.getKey();
      MetaDataTable tableMetaData = tableMeta.getValue();

      ArrayList<String> checkRepete = new ArrayList<>();

      table += "* " + tableName + "\n";
      Map<String, TableMetaDataModel> tableMetaDataModel = tableMetaData.getTableMetaData();
      for(Map.Entry<String,String> reverse : relationMap.entrySet())
      {
        String[] temp = reverse.getKey().split("->");
        String connector = reverse.getValue();

        if(tableName.equals(temp[1]))
        {
          if (connector.equals(MANYTOONE))
            connector = ONETOMANY;

          relation += temp[1] + connector + temp[0] + "\n";

        }

      }

      for (Map.Entry<String, TableMetaDataModel> columnMeta : tableMetaDataModel.entrySet()) {
        String columnName = columnMeta.getKey();
        TableMetaDataModel columnData = columnMeta.getValue();

        String FkTable = columnData.getFkTable();
        String FkColumn = columnData.getFkColumn();

        columns += columnName + " (" + columnData.getColType() + ")";
        if (columnData.getPk())
          columns += " (Primary Key)\n";
        else
          columns += "\n";

        if (!FkTable.equals(" ") && !FkTable.equals("") && !FkTable.toLowerCase(Locale.ROOT).equals("null")) {
          foeignKey += columnName + ARROW + FkTable + " (" + FkColumn + ")\n";
          if (relationMap.containsKey(tableName + "->" + FkTable) && !checkRepete.contains(FkTable)) {
            String connector = relationMap.get(tableName + "->" + FkTable);
            relation += tableName + connector + FkTable + "\n";
            checkRepete.add(FkTable);
          }

        }

      }
      result += table + relation + foeignKey + columns +
          "_____________________________________________________________________\n";

    }
    System.out.println(result);
    try {
      String fileName = DBMS + "/" + Database.name + "/" + Database.name +
          "_" + "ERDiagram.txt";
      FileWriter myWriter = new FileWriter(fileName);
      myWriter.write(result);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

  }

  public Map<String, String> getAllRelation() {

    Map<String, String> relationMap = new HashMap<>();
    Map<String, MetaDataTable> allTableMetaData =
        Database.getMetaDataAllTables();
    for (Map.Entry<String, MetaDataTable> tableMeta : allTableMetaData.entrySet()) {

      String tableName = tableMeta.getKey();
      MetaDataTable tableMetaData = tableMeta.getValue();

      Map<String, TableMetaDataModel> tableMetaDataModel = tableMetaData.getTableMetaData();

      for (Map.Entry<String, TableMetaDataModel> columnMeta : tableMetaDataModel.entrySet()) {
        String columnName = columnMeta.getKey();
        TableMetaDataModel columnData = columnMeta.getValue();

        String FkTable = columnData.getFkTable();
        String FkColumn = columnData.getFkColumn();

        if (!FkTable.equals(" ") && !FkTable.equals("") && !FkTable.toLowerCase(Locale.ROOT).equals("null")) {
          TableMetaDataModel FkColumnData =
              allTableMetaData.get(FkTable).getTableMetaData().get(FkColumn);

          String connector = ARROW;
          if (columnData.getPk() && FkColumnData.getPk()) {
            connector = ONETOONE;
          } else if (!columnData.getPk() && FkColumnData.getPk()) {
            connector = MANYTOONE;
          }
          relationMap.put(tableName + "->" + FkTable, connector);
        }
      }
    }
    System.out.println(relationMap);
    return relationMap;
  }

  public void takeInputAndLoadDatabase() {

    Scanner sc = new Scanner(System.in);
    String DatabaseName = sc.nextLine();
    DatabaseLoader loader = new DatabaseLoader();
    Database.setName(DatabaseName);
    loader.load();
  }

}
