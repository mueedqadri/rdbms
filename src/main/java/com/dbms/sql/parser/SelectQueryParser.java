package com.dbms.sql.parser;

import com.dbms.constants.GlobalConstants;
import com.dbms.constants.RegexConstants;
import com.dbms.model.TableMetaDataModel;
import com.dbms.sql.parser.interfaces.IParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dbms.constants.GlobalConstants.COLUMN;
import static com.dbms.constants.GlobalConstants.TABLE_NAME;
import static com.dbms.constants.RegexConstants.CASE_INSENSITIVE;
import static com.dbms.constants.RegexConstants.SPACE;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class SelectQueryParser implements IParser {
    private final Pattern patterns = Pattern.compile(getRegex());


    @Override
    public Boolean isValid(String string) {
        String regex = getRegex();
        return string.matches(regex);
    }

    public String getTableName(String sqlQuery) {
        return getGroup(sqlQuery, TABLE_NAME);
    }

    public List<String> getColumns(String sqlQuery, List<TableMetaDataModel> columns) {
        final List<String> columnNames = getStrings(sqlQuery, GlobalConstants.FIELDS);
        if (columnNames.isEmpty() || (columnNames.contains("*") && columnNames.size() != 1))
            throw new IllegalArgumentException();
        if (columnNames.contains("*")) {
            return columns.stream().map(TableMetaDataModel::getColName).collect(toList());
        }
        return columnNames;
    }

    public List<String> getWhereClause(String sqlQuery, Map<String, TableMetaDataModel> columns) {
        final String column = getGroup(sqlQuery, COLUMN);
        if(isNull(column)) return Collections.emptyList();
        if (!columns.containsKey(column)) throw new IllegalArgumentException();
        final String arguments = getGroup(sqlQuery, GlobalConstants.ARGUMENTS);
        return Arrays.asList(column, arguments);
    }

    private List<String> getStrings(String sqlQuery, String group) {
        final String groupValues = getGroup(sqlQuery, group);
        if (isNull(groupValues)) return Collections.emptyList();
        return Arrays.stream(groupValues.replaceAll("\\(|\\)|,", " ").replaceAll("\\s+|\n+", " ")
                .split(SPACE)).filter(s -> !s.isEmpty()).map(String::trim).collect(toList());
    }

    private String getGroup(String string, String group) {
        Matcher matcher = patterns.matcher(string);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return "";
    }

    private String getRegex() {
        return CASE_INSENSITIVE + RegexConstants.SELECT + RegexConstants.SPACE + RegexConstants.FIELDS + RegexConstants.SPACE +
                RegexConstants.FROM + RegexConstants.SPACE + RegexConstants.TABLE_GROUP + RegexConstants.SPACE_OPT
                + getWhereClause()
                + RegexConstants.END;
    }

    private String getWhereClause() {
        return "(" + RegexConstants.WHERE + RegexConstants.SPACE + RegexConstants.COLUMNS + RegexConstants.SPACE_OPT +
                RegexConstants.SYMBOLS + RegexConstants.SPACE_OPT + RegexConstants.ARGUMENTS + ")*";
    }

}