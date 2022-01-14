package com.dbms.sql.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SelectQueryParserTest {

    private static final SelectQueryParser selectQueryParser = new SelectQueryParser();

    @Test
    void isValid_returnsTrue_selectAllNoWhereClause() {
        String sql = "SELECT * FROM TABLE_NAME;";
        assertTrue(selectQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsTrue_selectFieldsNoWhereClause() {
        String sql = "SELECT name,address FROM TABLE_NAME;";
        assertTrue(selectQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsTrue_selectFieldsWithWhereClauseSymbols() {
        String sql = "SELECT name,address FROM TABLE_NAME WHERE id = 5;";
        assertTrue(selectQueryParser.isValid(sql));
    }


    @Test
    void isValid_returnsFalse_selectAllNoSemiColon() {
        String sql = "SELECT * FROM TABLE_NAME";
        assertFalse(selectQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_selectFieldsWithNoComma() {
        String sql = "SELECT name address FROM TABLE_NAME;";
        assertFalse(selectQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_selectFieldsWithNoWordAfterComma() {
        String sql = "SELECT name, FROM TABLE_NAME;";
        assertFalse(selectQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_selectFieldsWithNoWordBeforeComma() {
        String sql = "SELECT ,name FROM TABLE_NAME;";
        assertFalse(selectQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_selectFieldsWhereDoubleEquals() {
        String sql = "SELECT name,address FROM TABLE_NAME WHERE id == 5;";
        assertFalse(selectQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_selectFieldsWhereInvalidMatcher() {
        String sql = "SELECT name,address FROM TABLE_NAME WHERE id iss 5;";
        assertFalse(selectQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_selectFieldsWithWhereClauseWordsAndNotComesBeforeIs() {
        String sql = "SELECT name,address FROM TABLE_NAME WHERE id not is 5;";
        assertFalse(selectQueryParser.isValid(sql));
    }

}