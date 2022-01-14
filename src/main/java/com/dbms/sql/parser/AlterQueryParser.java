package com.dbms.sql.parser;

import com.dbms.constants.GlobalConstants;
import com.dbms.constants.RegexConstants;
import com.dbms.sql.parser.interfaces.IParser;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlterQueryParser implements IParser {

    private final Pattern alterPattern = Pattern.compile(getRegex());

    @Override
    public Boolean isValid(String string) {
        OperationParser operationParser = new OperationParser();
        if(string.matches(getRegex())){
            String operationType = getAlterOperationType(string).toLowerCase(Locale.ROOT);
            if(operationType.equals(GlobalConstants.ADD) || operationType.equals(GlobalConstants.MODIFY)){
                return operationParser.isValid(getAlterOperation(string));
            } else if(operationType.equals(GlobalConstants.RENAME) || operationType.equals(GlobalConstants.DROP)){
                //TODO implement this logic
                return true;
            }
        }
        return false;
    }

    public String getRegex() {
        return RegexConstants.CASE_INSENSITIVE
                + RegexConstants.ALTER
                + RegexConstants.SPACE
                + RegexConstants.TABLE_KEYWORD
                + RegexConstants.SPACE
                + RegexConstants.ENTITY_NAME_GROUP
                + RegexConstants.SPACE
                + RegexConstants.ALTER_OPERATIONS
                + RegexConstants.SPACE
                + RegexConstants.ANY_GROUP_MATCH
                + RegexConstants.END;
    }

    public String getTableGroup(String string) {
        Matcher matcher = alterPattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(GlobalConstants.ENTITY_NAME);
        }
        return null;
    }

    public String getAlterOperationType(String string) {
        Matcher matcher = alterPattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(GlobalConstants.OPERATION_TYPE);
        }
        return null;
    }

    public String getAlterOperation(String string) {
        Matcher matcher = alterPattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(GlobalConstants.ANY_GROUP);
        }
        return null;
    }
}
