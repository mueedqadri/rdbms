package com.dbms.sql.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DropTableTest {
    private DropQueryParser parser = new DropQueryParser();

    @Test
    public void dropTest() {
        String query = "DROP TABLE  table_name;";
        System.out.println(parser.getRegex());
        assertTrue(parser.isValid(query));
    }

    @Test
    public void dropDbTest() {
        String query = "DROP DATabase myDb;";
        assertTrue(parser.isValid(query));
    }

    @Test
    public void dropInvalidTest() {
        String query = "DROP DATabase;";
        assertFalse(parser.isValid(query));
    }

    @Test
    public void dropInvalidTestTwo() {
        String query = "DROP DATbase myDb;;";
        assertFalse(parser.isValid(query));
    }
}
