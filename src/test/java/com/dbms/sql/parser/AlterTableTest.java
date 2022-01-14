package com.dbms.sql.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlterTableTest {
    private AlterQueryParser parser = new AlterQueryParser();

    @Test
    public void dropTest() {
        String query = "ALTER TABLE testalter_tbl DROP i;";
        assertTrue(parser.isValid(query));
    }

    @Test
    public void addTest() {
        String query = "ALTER TABLE Customers\n" +
                "ADD Email varchar(255);";
        assertTrue(parser.isValid(query));
    }

    @Test
    public void addTestTwo() {
        String query = "alter TABLE cus_tbl  \n" +
                "ADD cus_age varchar(40) NN PK;";
        assertTrue(parser.isValid(query));
    }


    @Test
    public void addTestTwoInvalidOperation() {
        String query = "alter TABLE cus_tbl  \n" +
                "ADD cus_age varchar(40) NN PKO;";
        assertFalse(parser.isValid(query));
    }

    @Test
    public void addTestTwoInvalid() {
        String query = "ALTER  cus_tbl  \n" +
                "ADD cus_age varchar(40) NN PK;";
        assertFalse(parser.isValid(query));
    }

    @Test
    public void dropInvalidTest() {
        String query = "ALTER table testalter_tbl DROP ;";
        assertFalse(parser.isValid(query));
    }

    @Test
    public void dropInvalidTestTwo() {
        String query = "ALTR table testalter_tbl DROP i;";
        assertFalse(parser.isValid(query));
    }
}
