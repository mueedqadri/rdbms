package com.dbms.sql.handler;

import com.dbms.Context;
import com.dbms.data.dictionary.Database;
import com.dbms.userManagement.UserAuthModel;
import org.junit.jupiter.api.Test;

public class CreateHandlerTest {
    private CreateQueryHandler handler = new CreateQueryHandler();

    @Test
    public void testCreateTableSingleColumn() {
        DatabaseLoader loader = new DatabaseLoader();
        Database.setName("university");
        loader.load();
        String query = "CREATE TABLE new ( id int nn pk ai, LastName text nn," +
                " FirstName varchar(255) nn, Address varchar(255) nn, salary float nn);";
        handler.setSqlQuery(query);
        handler.handle();
    }

    @Test
    public void testCreateDataBase() {
        String query = "CREATE DATABASE testing3;";
        Context.currentUser = new UserAuthModel();
        Context.currentUser.setUserName("mueed@gmail.com");
        handler.setSqlQuery(query);
        handler.handle();
    }
    @Test
    public void testCreatePerson() {
        DatabaseLoader loader = new DatabaseLoader();
        Database.setName("university");
        loader.load();
        String query = "CREATE TABLE Persons (\n" +
            "    PersonID int pk nn,\n" +
            "    LastName varchar(255),\n" +
            "    FirstName varchar(255),\n" +
            "    Address varchar(255),\n" +
            "    City varchar(255)\n" +
            ");";
        handler.setSqlQuery(query);
        handler.handle();
    }
    @Test
    public void testCreateOrder()
    {
        DatabaseLoader loader = new DatabaseLoader();
        Database.setName("university");
        loader.load();
        String query = "CREATE TABLE Orders (\n" +
            "    OrderID int nn pk,\n" +
            "    OrderNumber int nn,\n" +
            "    PersonID int,\n" +
            "    FOREIGN KEY (PersonID) REFERENCES Persons(PersonID),FOREIGN " +
            "KEY (OrderID) REFERENCES new(id));";
        handler.setSqlQuery(query);
        handler.handle();

    }

    @Test
    public void testERDiagram() {
        ERDiagramHandler er = new ERDiagramHandler();
        DatabaseLoader loader = new DatabaseLoader();
        Database.setName("university");
        loader.load();
        er.generate();
    }

}
