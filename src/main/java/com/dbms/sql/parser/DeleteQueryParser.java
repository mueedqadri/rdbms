package com.dbms.sql.parser;

import com.dbms.constants.GlobalConstants;
import com.dbms.log.DBLogger;
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
import static com.dbms.constants.RegexConstants.*;
import static java.util.Objects.isNull;

public class DeleteQueryParser implements IParser {
    private final Pattern patterns = Pattern.compile(getRegex());

    @Override
    public Boolean isValid(String string) {
        String regex = getRegex();
        return string.matches(regex);
    }

    public String getTableName(String sqlQuery) {
        return getGroup(sqlQuery, TABLE_NAME);
    }

    public List<String> getWhereClause(String sqlQuery, Map<String, TableMetaDataModel> columns) {
        final String column = getGroup(sqlQuery, COLUMN);
        if(isNull(column)) return Collections.emptyList();
        if (!columns.containsKey(column)) DBLogger.logError("Invalid column name specified.");
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
        return CASE_INSENSITIVE + DELETE + SPACE + FROM + SPACE + TABLE_GROUP + SPACE_OPT +
                getWhereClause() + END;
    }

    private String getWhereClause() {
        return "(" + WHERE + SPACE + COLUMNS + SPACE_OPT +
                SYMBOLS + SPACE_OPT + ARGUMENTS + ")*";
    }

}
