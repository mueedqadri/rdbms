package com.dbms.sql.parser;

import com.dbms.sql.handler.CreateQueryHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class CreateTest {
    private CreateQueryParser parser = new CreateQueryParser();
    private CreateQueryHandler handler = new CreateQueryHandler();

    @Test
    public void multipleColumns() {
        String query = "CREATE TABLE Persons ( PersonID int, LastName varchar(255), FirstName varchar(255), Address varchar(255), City varchar(255) );";
        assertTrue(parser.isValid(query));
    }

    @Test
    public void tableNameNotGiven() {
        String query = "CREATE TABLE ( PersonID int, LastName varchar(255), FirstName varchar(255), Address varchar(255), City varchar(255) );";
        assertFalse(parser.isValid(query));
    }

    @Test
    public void wrongSyntax() {
        String query = "CREATE TABLE PersonID int, LastName varchar(255), FirstName varchar(255), Address varchar(255), City varchar(255) );";
        assertFalse(parser.isValid(query));
    }

    @Test
    public void testHandlerNoOptionalToken() {
        String query = "CREATE TABLE NoOptionalTokenTest ( id int, LastName text, FirstName varchar(255), Address varchar(255), salary float);";
        handler.setSqlQuery(query);
        handler.handle();
    }

    @Test
    public void testHandlerWithAllOptionalToken() {
        String query = "CREATE TABLE WithAllOptionalToken ( id int nn pk ai, LastName text nn," +
                " FirstName varchar(255) nn, Address varchar(255) nn, salary float nn);";
        CreateQueryParser parser = new CreateQueryParser();
        assertTrue(parser.isValid(query));
    }

    @Test
    public void testHandlerWrongOptionalToken() {
        String query = "CREATE TABLE WithAllOptionalToken ( id int wrong, LastName text nn, FirstName varchar(255) nn, " +
                "Address varchar(255) nn, salary float nn);";
        CreateQueryParser parser = new CreateQueryParser();
        assertFalse(parser.isValid(query));
    }

    @Test
    public void createDatabaseTest() {
        String query = "CREATE DATABASE newDatabase;";
        assertTrue(parser.isValid(query));
    }
}
