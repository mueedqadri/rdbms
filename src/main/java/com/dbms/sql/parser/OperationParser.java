package com.dbms.sql.parser;

import com.dbms.constants.GlobalConstants;
import com.dbms.constants.RegexConstants;
import com.dbms.constants.DataTypes;
import com.dbms.data.dictionary.Database;
import com.dbms.data.dictionary.MetaDataTable;
import com.dbms.model.TableMetaDataModel;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OperationParser {

  private final Pattern createForeignKeyPattern = Pattern.compile(RegexConstants.FK_MATCHING);

  public Boolean isValid(String columnData) {

    String[] fields = columnData.trim().split("(\s|\n)+");
    if (fields[0].toLowerCase(Locale.ROOT).equals("foreign")) {
      return fields.length == 5 && (isForeignKeyValid(columnData));
    } else {
      return fields.length >= 2 && (isDataTypeValid(fields[1])) && fields.length <= 5 && isOptionalTokenValid(columnData);

    }
  }

  public Boolean isDataTypeValid(String field) {

    if ((field.toUpperCase()).matches(RegexConstants.VARCHAR))
      return true;
    for (DataTypes.DataType s : DataTypes.DataType.values()) {
      if (s.name().equals(field.toUpperCase(Locale.ROOT))) {
        return true;
      }
    }
    return false;
  }

  public String[] getAllTokens(String columnData) {

    return columnData.toLowerCase(Locale.ROOT).trim().split("(\s|\n)+");
  }

  public boolean isOptionalTokenValid(String columnData) {

    String[] fields = getAllTokens(columnData);
    int operationCount = 0;
    for (int i = 2; i < fields.length; i++) {
      if (fields[i].matches(RegexConstants.OPTIONAL_TOKEN)) {
        operationCount = operationCount + 1;
      }
    }
    return (fields.length - 2) == operationCount;
  }

  public Boolean isForeignKeyValid(String columnData) {

    Matcher matcher = createForeignKeyPattern.matcher(columnData);
    //matching pattern with regex.
    if (matcher.find())// if pattern matches
    {
      String FKDestinationColumn =
          matcher.group(GlobalConstants.FK_DESTINATION_COLUMN);//Get
      // current table column (DestinationColumn)
      String FKTableName =
          matcher.group(GlobalConstants.FK_TABLE_NAME);// get source
      // table name
      String FKSourceColumn =
          matcher.group(GlobalConstants.FK_SOURCE_COLUMN);// get source
      // coloumn name

      Map<String, MetaDataTable> allTableMetaData =
          Database.getMetaDataAllTables();//get all table from databse
      // for checking tables.
      System.out.println(CreateQueryParser.validForeignKey);

      /*
       *checks if destination columns is valid
       */
      if (CreateQueryParser.validColumns.contains(FKDestinationColumn) && !CreateQueryParser.validForeignKey.contains(FKDestinationColumn)) {
        if (allTableMetaData.containsKey(FKTableName)) {
          Map<String, TableMetaDataModel> tableMetaData =
              allTableMetaData.get(FKTableName).getTableMetaData();
          if (tableMetaData.containsKey(FKSourceColumn)) {
            TableMetaDataModel tableMetaDataModel =
                tableMetaData.get(FKSourceColumn);
            if (tableMetaDataModel.getPk() && tableMetaDataModel.isNotNull()) {
              return true;
            } else
              System.out.println("Reference Column " + FKSourceColumn + " Must be Primary Key and Not Null !");

          } else
            System.out.println("Reference Column " + FKSourceColumn + " Not Exits !");
        } else
          System.out.println("Reference Foreign Table " + FKTableName + " Not " +
              "Exits !");
      } else
        System.out.println("Current Table Column " + FKDestinationColumn + " Is Not Valid  Or Already Assigned !");

      return false;
    }
    return false;
  }

  public int countOptionalToken(String columnData) {

    Pattern optionalTokens = Pattern.compile(RegexConstants.OPTIONAL_TOKEN);
    Matcher matcher = optionalTokens.matcher(columnData);
    int operationCount = 0;
    while (matcher.find()) {
      matcher.group(GlobalConstants.OPTIONAL_TOKEN);
      operationCount = operationCount + 1;
    }
    return operationCount;
  }

  public boolean getNotNull(String column) {

    return Arrays.stream(getAllTokens(column)).anyMatch(x -> x.equals(GlobalConstants.NOT_NULL));
  }

  public boolean getPrimaryKey(String column) {

    return Arrays.stream(getAllTokens(column)).anyMatch(x -> x.equals(GlobalConstants.PRIMARY_KEY));
  }

  public boolean getAutoIncrement(String column) {

    return Arrays.stream(getAllTokens(column)).anyMatch(x -> x.equals(GlobalConstants.AUTO_INCREMENT));
  }

  public String getColumnName(String column) {

    String[] fields = column.trim().split("(\s|\n)+");
    return fields[0];
  }

  public String getColumnNewName(String column) {

    String[] fields = column.trim().split("(\s|\n)+");
    return fields[1];
  }

  public String getDataType(String column) {

    String[] fields = column.trim().split("(\s|\n)+");
    return fields[1];
  }
}
