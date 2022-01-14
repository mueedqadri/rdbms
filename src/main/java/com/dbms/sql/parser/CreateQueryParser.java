package com.dbms.sql.parser;

import com.dbms.constants.GlobalConstants;
import com.dbms.constants.RegexConstants;
import com.dbms.sql.parser.interfaces.IParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateQueryParser extends OperationParser implements IParser {

  private final Pattern createTablePattern = Pattern.compile(getCreateTableRegex());
  private final Pattern createDatabasePattern = Pattern.compile(getCreateDatabaseRegex());
  private final Pattern createGeneralPattern = Pattern.compile(getRegex());
  public String operationType;
  public String database;
  public static ArrayList<String> validColumns = new ArrayList<>();
  public static ArrayList<String> validForeignKey = new ArrayList<>();

  @Override
  public Boolean isValid(String string) {

    operationType = getOperationType(string);
    if (operationType != null) {
      if (operationType.equalsIgnoreCase(GlobalConstants.TABLE)) {
        return string.matches(getCreateTableRegex()) && areIndividualColumnValid(string);
      } else if (operationType.equalsIgnoreCase(GlobalConstants.DATABASE)) {

        return string.matches(getCreateDatabaseRegex());
      }
    }
    return false;
  }

  public String getDatabaseGroup(String string) {

    Matcher matcher = createDatabasePattern.matcher(string);
    if (matcher.find()) {
      return matcher.group(GlobalConstants.ENTITY_NAME);
    }
    return null;
  }

  public String getRegex() {
    return RegexConstants.CASE_INSENSITIVE + RegexConstants.CREATE_KEYWORD + RegexConstants.SPACE
        + RegexConstants.CREATE_OPERATION + RegexConstants.SPACE + RegexConstants.MATCH_ALL + RegexConstants.END;
  }

  public String getCreateDatabaseRegex() {
    return RegexConstants.CASE_INSENSITIVE + RegexConstants.CREATE_KEYWORD + RegexConstants.SPACE
        + RegexConstants.CREATE_OPERATION + RegexConstants.SPACE + RegexConstants.ENTITY_NAME_GROUP
        + RegexConstants.END;
  }

  public String getOperationType(String string) {
    Matcher matcher = createGeneralPattern.matcher(string);
    if (matcher.find()) {
      return matcher.group(GlobalConstants.OPERATION_TYPE);
    }
    return null;
  }

  public String getCreateTableRegex() {
    return RegexConstants.CASE_INSENSITIVE + RegexConstants.CREATE + RegexConstants.SPACE
        + RegexConstants.TABLE_KEYWORD + RegexConstants.SPACE + RegexConstants.TABLE_GROUP
        + RegexConstants.SPACE_OPT + RegexConstants.CREATE_MATCH_ALL_COLUMNS + RegexConstants.SPACE_OPT
        + RegexConstants.END;
  }

  public String getTableGroup(String string) {
    Matcher matcher = createTablePattern.matcher(string);
    if (matcher.find()) {
      return matcher.group(GlobalConstants.TABLE_NAME);
    }
    return "";
  }

  public boolean areIndividualColumnValid(String string) {
    String allColumns = this.getColumnOperation(string);
    for (String column : allColumns.split(",")) {
      if (!super.isValid(column.trim())) {
        return false;
      } else {
        if (column.trim().split("(\s|\n)+")[0].toLowerCase(Locale.ROOT).equals("foreign"))
          validForeignKey.add(column.trim().split("(\s|\n)+")[2].replaceAll("[//(,//)]", ""));
        else
          validColumns.add(column.trim().split("(\s|\n)+")[0]);
      }
    }
    return true;
  }

  public String getColumnOperation(String string) {
    Matcher matcher = createTablePattern.matcher(string);
    if (matcher.find()) {
      return matcher.group(GlobalConstants.CREATE_OPERATION);
    }
    return null;
  }

  public List<String> getAllColumnName(String string) {
    List<String> names = new ArrayList<>();
    String allColumns = this.getColumnOperation(string);
    for (String column : allColumns.split(",")) {
      if (!getColumnName(column).toLowerCase(Locale.ROOT).equals("foreign"))
        names.add(getColumnName(column));
    }
    return names;
  }

  public List<Boolean> getAllPrimary(String string) {
    List<Boolean> pk = new ArrayList<>();
    String allColumns = this.getColumnOperation(string);
    for (String column : allColumns.split(",")) {
      pk.add(getPrimaryKey(column));
    }
    return pk;
  }

  public Map<String, String> getAllForeignKey(String string) {
    final Pattern createForeignKeyPattern = Pattern.compile(RegexConstants.FK_MATCHING);
    Map<String, String> FK = new HashMap<>();
    String allColumns = this.getColumnOperation(string);
    for (String column : allColumns.split(",")) {
      Matcher matcher = createForeignKeyPattern.matcher(column);
      if (matcher.find()) {
        String FKDestinationColumn = matcher.group(GlobalConstants.FK_DESTINATION_COLUMN);
        String FKTableName = matcher.group(GlobalConstants.FK_TABLE_NAME);
        String FKSourceColumn = matcher.group(GlobalConstants.FK_SOURCE_COLUMN);
        FK.put(FKDestinationColumn, FKTableName + "|" + FKSourceColumn);
      }
    }
    return FK;
  }

  public List<Boolean> getAllNotNULL(String string) {
    List<Boolean> nn = new ArrayList<>();
    String allColumns = this.getColumnOperation(string);
    for (String column : allColumns.split(",")) {
      nn.add(getNotNull(column));
    }
    return nn;
  }

  public List<Boolean> getAllAutoIncrement(String string) {
    List<Boolean> ai = new ArrayList<>();
    String allColumns = this.getColumnOperation(string);
    for (String column : allColumns.split(",")) {
      ai.add(getAutoIncrement(column));
    }
    return ai;
  }

  public List<String> getAllDatatype(String string) {
    List<String> datatype = new ArrayList<>();
    String allColumns = this.getColumnOperation(string);
    for (String column : allColumns.split(",")) {
      datatype.add(getDataType(column));
    }
    return datatype;
  }
}