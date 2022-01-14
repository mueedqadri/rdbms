package com.dbms.sql.parser;

import com.dbms.constants.GlobalConstants;
import com.dbms.model.TableMetaDataModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InsertQueryParserTest {

    private InsertQueryParser insertQueryParser = new InsertQueryParser();

    @Test
    void isValid_returnsTrue_InsertQuery() {
        String sql = "insert into table_name (column1, column2, column3) values (value1, value2, value3);";
        assertTrue(insertQueryParser.isValid(sql));
        assertTrue(true);
    }

    @Test
    void isValid_returnsTrue_InsertQuerySingleValue() {
        String sql = "insert into table_name (column1) values (value1);";
        assertTrue(insertQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsTrue_InsertQueryAllValues() {
        String sql = "insert into table_name values (value1, value2);";
        assertTrue(insertQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_InsertQueryAllValuesWithBracket() {
        String sql = "insert into table_name() values (value1, value2);";
        assertFalse(insertQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_InsertQueryNoValues() {
        String sql = "insert into table_name values ();";
        assertFalse(insertQueryParser.isValid(sql));
    }

    @Test
    void isValid_returnsFalse_InsertQueryNoValuesWithBracket() {
        String sql = "insert into table_name() values value1, value2;";
        assertFalse(insertQueryParser.isValid(sql));
    }

    @Test
    void getColumnsWithValues_returnMap_ForColumnsAndValues() {
        String sql = "insert into table_name (column1, column2) values (value1, value2);";

        final List<TableMetaDataModel> metaDataModels = getTableMetaDataModels();
        final Map<String, String> columnsWithValues = insertQueryParser.getColumnsWithValues(sql, metaDataModels, 0);
        final Map<String, String> expected = getExpected(null, "1");
        assertEquals(expected, columnsWithValues);
    }

    @Test
    void getColumnsWithValues_returnMap_ForNoColumnsAndGivenValues() {
        String sql = "insert into table_name values (value1, value2, value3, 2);";

        final List<TableMetaDataModel> metaDataModels = getTableMetaDataModels();
        final Map<String, String> columnsWithValues = insertQueryParser.getColumnsWithValues(sql, metaDataModels, 0);
        final Map<String, String> expected = getExpected("value3", "2");
        assertEquals(expected, columnsWithValues);
    }

    private Map<String, String> getExpected(String thirdValue, String fourthValue) {
        final Map<String, String> expected = new HashMap<>();
        expected.put("column1", "value1");
        expected.put("column2", "value2");
        expected.put("column3", thirdValue);
        expected.put("column4", fourthValue);
        return expected;
    }

    private List<TableMetaDataModel> getTableMetaDataModels() {
        final TableMetaDataModel tableMetaDataModel1 = new TableMetaDataModel();
        tableMetaDataModel1.setColName("column1");
        final TableMetaDataModel tableMetaDataModel2 = new TableMetaDataModel();
        tableMetaDataModel2.setColName("column2");
        final TableMetaDataModel tableMetaDataModel3 = new TableMetaDataModel();
        tableMetaDataModel3.setColName("column3");
        final TableMetaDataModel tableMetaDataModel4 = new TableMetaDataModel();
        tableMetaDataModel4.setColName("column4");
        tableMetaDataModel4.setAi(true);
        return Arrays.asList(tableMetaDataModel1, tableMetaDataModel2, tableMetaDataModel3, tableMetaDataModel4);
    }

}