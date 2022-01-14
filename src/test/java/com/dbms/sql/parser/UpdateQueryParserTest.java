package com.dbms.sql.parser;

import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.dbms.model.TableMetaDataModel;


class UpdateQueryParserTest {

    private static final UpdateQueryParser updateQueryParser = new UpdateQueryParser();

    @Test
    void isValid_returnsTrue_setOneNoWhereClause() {
        String sql = "UPDATE TABLE_NAME SET FIELD_NAME=5;";
        assertTrue(updateQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsTrue_SetMultipleNoWhereClause() {
        String sql = "UPDATE TABLE_NAME SET FIELD=5,ANOTHER_FIELD=6;";
        assertTrue(updateQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsTrue_setOneWithWhereClause() {
        String sql = "UPDATE TABLE_NAME SET FIELD_NAME=5 where id = 5;";
        assertTrue(updateQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsTrue_SetMultipleWithWhereClause() {
        String sql = "UPDATE TABLE_NAME SET FIELD=5,ANOTHER_FIELD=6 where id = 5;";
        assertTrue(updateQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_SetMultipleWithWhereClauseNoComma() {
        String sql = "UPDATE TABLE_NAME SET FIELD=5 ANOTHER_FIELD=6 where id = 5;";
        assertFalse(updateQueryParser.isValid(sql));
    }

    @Test
    void getGroup() {
        String sql = "UPDATE TABLE_NAME SET column1=5,column2=6 where column3 = 5;";
        final Map<Integer, String> columnsWithValues = updateQueryParser.getColumnsWithValues(sql, getTableMetaDataModels());
        updateQueryParser.getWhereClause(sql, getTableMetaDataModels());
    }

    private HashMap<String, TableMetaDataModel> getTableMetaDataModels() {
        HashMap<String, TableMetaDataModel> hashMap = new HashMap<String, TableMetaDataModel>();
        final TableMetaDataModel tableMetaDataModel1 = new TableMetaDataModel();
        tableMetaDataModel1.setColName("column1");
        hashMap.put("column1", tableMetaDataModel1);
        final TableMetaDataModel tableMetaDataModel2 = new TableMetaDataModel();
        tableMetaDataModel2.setColName("column2");
        hashMap.put("column2", tableMetaDataModel2);
        final TableMetaDataModel tableMetaDataModel3 = new TableMetaDataModel();
        tableMetaDataModel3.setColName("column3");
        hashMap.put("column3", tableMetaDataModel3);
        final TableMetaDataModel tableMetaDataModel4 = new TableMetaDataModel();
        tableMetaDataModel4.setColName("column4");
        tableMetaDataModel4.setAi(true);
        hashMap.put("column4", tableMetaDataModel4);
        return hashMap;
    }

}