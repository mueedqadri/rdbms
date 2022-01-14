package com.dbms.sql.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteQueryParserTest {

    DeleteQueryParser deleteQueryParser = new DeleteQueryParser();

    @Test
    void shouldBeValid() {
        String sql = "delete from table_name where col = 5;";
        assertTrue(deleteQueryParser.isValid(sql));
    }

    @Test
    void shouldBeValidWithoutWhere() {
        String sql = "delete from table_name;";
        assertTrue(deleteQueryParser.isValid(sql));
    }

}