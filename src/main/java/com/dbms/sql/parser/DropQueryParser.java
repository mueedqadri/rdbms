package com.dbms.sql.parser;

import com.dbms.constants.GlobalConstants;
import com.dbms.constants.RegexConstants;
import com.dbms.sql.parser.interfaces.IParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropQueryParser implements IParser {

    private Pattern pattern = Pattern.compile(getRegex());

    @Override
    public Boolean isValid(String string) {
        return string.matches(getRegex());
    }

    public String getRegex() {
        return RegexConstants.CASE_INSENSITIVE
                + RegexConstants.DROP_KEYWORD
                + RegexConstants.SPACE
                + RegexConstants.DROP_OPERATION
                + RegexConstants.SPACE
                + RegexConstants.ENTITY_NAME_GROUP
                + RegexConstants.END;
    }

    public String getEntityName(String string) {
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(GlobalConstants.ENTITY_NAME);
        }
        return null;
    }

    public String getDropOperationType(String string) {
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(GlobalConstants.OPERATION_TYPE);
        }
        return null;
    }
}
