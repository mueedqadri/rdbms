package com.dbms.sql.parser;

import com.dbms.constants.GlobalConstants;
import com.dbms.constants.RegexConstants;
import com.dbms.log.DBLogger;
import com.dbms.sql.parser.interfaces.IParser;
import com.dbms.model.TableMetaDataModel;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dbms.constants.GlobalConstants.COLUMN;
import static com.dbms.constants.GlobalConstants.TABLE_NAME;
import static java.util.Objects.isNull;

public class UpdateQueryParser implements IParser {
    private final Pattern patterns = Pattern.compile(getRegex());

    @Override
    public Boolean isValid(String string) {
        String regex = getRegex();
        return string.matches(regex);
    }

    public String getTableName(String sqlQuery) {
        return getGroup(sqlQuery, TABLE_NAME);
    }

    public Map<Integer, String> getColumnsWithValues(String sqlQuery, Map<String, TableMetaDataModel> columns) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        List<String> columnNames = new ArrayList<>();
        final String group = getGroup(sqlQuery, GlobalConstants.SET_VALUES);
        Arrays.stream(group.split(",")).filter(s->!s.isEmpty()).forEach(assignment -> {
            final String[] split = assignment.split("=");
            hashMap.put(columns.get(split[0]).getOrder(), split[1]);
            columnNames.add(split[0]);
        });
        if(!columns.keySet().containsAll(columnNames)) {
            DBLogger.logError("Invalid column/s specified.");
        }
        return hashMap;

    }

    public List<String> getWhereClause(String sqlQuery, Map<String, TableMetaDataModel> columns) {
        final String column = getGroup(sqlQuery, COLUMN);
        if(isNull(column)) return Collections.emptyList();
        if (!columns.containsKey(column)) DBLogger.logError("Invalid column name specified");
        final String arguments = getGroup(sqlQuery, GlobalConstants.ARGUMENTS);
        return Arrays.asList(column, arguments);
    }


    private String getGroup(String string, String group) {
        Matcher matcher = patterns.matcher(string);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return "";
    }

    private String getRegex() {
        return RegexConstants.CASE_INSENSITIVE
                + RegexConstants.UPDATE + RegexConstants.SPACE
                + RegexConstants.TABLE_GROUP + RegexConstants.SPACE +
                RegexConstants.SET + RegexConstants.SPACE
                + RegexConstants.SET_VALUES + RegexConstants.SPACE_OPT +
                getWhereClause() + RegexConstants.END;
    }

    private String getAssignment() {
        return RegexConstants.PARAMS + RegexConstants.SPACE_OPT + RegexConstants.EQUAL +
                RegexConstants.SPACE_OPT + RegexConstants.VALUES;
    }

    private String getWhereClause() {
        return "(" + RegexConstants.WHERE
                + RegexConstants.SPACE
                + RegexConstants.COLUMNS
                + RegexConstants.SPACE_OPT +
                RegexConstants.SYMBOLS
                + RegexConstants.SPACE_OPT
                + RegexConstants.ARGUMENTS + ")*";
    }

}
