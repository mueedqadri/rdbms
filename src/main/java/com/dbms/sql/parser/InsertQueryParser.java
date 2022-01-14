package com.dbms.sql.parser;


import com.dbms.constants.GlobalConstants;
import com.dbms.model.TableMetaDataModel;
import com.dbms.sql.parser.interfaces.IParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dbms.constants.GlobalConstants.TABLE_NAME;
import static com.dbms.constants.RegexConstants.*;
import static java.util.Objects.isNull;

public class InsertQueryParser implements IParser {
    private final Pattern patterns = Pattern.compile(getRegex());


    @Override
    public Boolean isValid(String string) {
        return string.matches(getRegex());
    }

    private String getRegex() {
        return CASE_INSENSITIVE + INSERT + SPACE + INTO + SPACE + TABLE_GROUP + SPACE_OPT + COLUMN_NAMES + SPACE + VALUES_KEY +
                SPACE_OPT + COLUMN_VALUES + SPACE_OPT + END;
    }

    private String getGroup(String string, String group) {
        Matcher matcher = patterns.matcher(string);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return "";
    }


    public String getTableName(String sqlQuery) {
        return getGroup(sqlQuery, TABLE_NAME);
    }

    public Map<String, String> getColumnsWithValues(String sqlQuery, List<TableMetaDataModel> columns, int size) {

        final List<String> columnNames = getStrings(sqlQuery, GlobalConstants.COLUMN_NAMES);
        final List<String> values = getStrings(sqlQuery, GlobalConstants.COLUMN_VALUES);
        if(!columnNames.isEmpty() && columnNames.size() != values.size()) throw new IllegalArgumentException();
        return updateMap(columnNames, values, columns, size);
    }

    private Map<String, String> updateMap(List<String> columnNames, List<String> values,
                                          List<TableMetaDataModel> columns, int size) {
        Map<String, String> map = new HashMap<>();
        if(columnNames.isEmpty()) {
            return getMapWhenColumnNamesNotPresent(values, columns, map);
        }
        final Map<String, String> currentColumnMap = getCurrentColumnMap(columnNames, values);

        for (int i = 0; i < columns.size(); i++) {
            map.put(columns.get(i).getColName(), currentColumnMap.get(columns.get(i).getColName()));
        }

        setAutoIncrementValues(map, columns, size);
        return map;
    }

    private void setAutoIncrementValues(Map<String, String> map, List<TableMetaDataModel> columns, int size) {
        columns.stream().filter(TableMetaDataModel::isAi).forEach(column -> {
            map.replace(column.getColName(), String.valueOf(size + 1));
        });
    }

    private Map<String, String> getCurrentColumnMap(List<String> columnNames, List<String> values) {
        Map<String, String> colMap = new HashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            colMap.put(columnNames.get(i), values.get(i));
        }

        return colMap;
    }

    private Map<String, String> getMapWhenColumnNamesNotPresent(List<String> values, List<TableMetaDataModel> columns, Map<String, String> map) {
        if(values.size()!= columns.size()) throw new IllegalArgumentException();
        for (int i = 0; i < columns.size(); i++) {
            map.put(columns.get(i).getColName(), values.get(i));
        }
        return map;
    }

    private List<String> getStrings(String sqlQuery, String group) {
        final String groupValues = getGroup(sqlQuery, group);
        if (isNull(groupValues)) return Collections.emptyList();
        return Arrays.stream(groupValues.replaceAll("\\(|\\)|,", " ").replaceAll("\\s+|\n+", " ")
                .split(SPACE)).filter(s -> !s.isEmpty()).map(String::trim).collect(Collectors.toList());
    }
}
