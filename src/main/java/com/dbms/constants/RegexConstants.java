package com.dbms.constants;

public class RegexConstants {
    public static final String UPDATE = "(update)";
    public static final String DELETE = "(delete)";
    public static final String CREATE = "(create)";
    public static final String SET = "(set)";
    public static final String EQUAL = "=";
    public static final String SELECT = "(select)";
    public static final String SPACE = "(\\s|\\n)+";
    public static final String SPACE_OPT = "(\\s|\\n)*";
    public static final String FROM = "(from)";
    public static final String TABLE = "([A-z]+)";
    public static final String WHERE = "(where)";
    public static final String VALUES = "([A-z0-9]+)";
    public static final String PARAMS = "([A-z]+)";
    public static final String END = "\\;";
    public static final String ROUND_START = "(";
    public static final String ROUND_END_STAR = ")*";
    public static final String COMMA = "\\,";
    public static final String PIPE = "\\|";
    public static final String CLOSE_ROUND = ")";
    public static final String CASE_INSENSITIVE = "(?i)";
    public static final String ALTER_OPERATIONS = "(?<OperationType>drop|modify|add|rename)";
    public static final String TABLE_GROUP = "(?<TableName>[A-z]+?)";
    public static final String VARCHAR = "VARCHAR\\([0-9]+\\)";

    public static final String OPTIONAL_TOKEN = "(?i)^(?<optionalToken>(\\s)*(PK|AI|NN)([;]{1})*$)";
    public static final String AI_MATCHING = "(?i)(s|\\n)*ai(s|\\n)*";
    public static final String PK_MATCHING = "(?i)(s|\\n)*pk(s|\\n)*";
    public static final String FK_MATCHING = "(?i)(?<foreignKey>FOREIGN" +
        "(\\s|\\n)+KEY(\\s|\\n)+\\((?<FKDestinationColumn>.*)\\)(\\s|\\n)+" +
        "REFERENCES(\\s|\\n)+(?<FKTableName>[A-Za-z]+)\\((?<FKSourceColumn>" +
        ".*)\\))";
    public static final String MATCH_ALL = ".*";

    public static final String NN_MATCHING = "(?i)(s|\\n)*nn(s|\\n)*";
    public static final String CREATE_MATCH_ALL_COLUMNS = "([(](?<createOperation>.*)(\\s\\n)*\\))";

    public static final String ENTITY_NAME_GROUP = "(?<EntityName>[A-z1-9]+?)";
    public static final String ALTER = "(alter)";
    public static final String TABLE_KEYWORD = "(table)";
    public static final String DROP_OPERATION = "(?<OperationType>table|database)";
    public static final String CREATE_OPERATION = "(?<OperationType>table|database)";
    public static final String DROP_KEYWORD = "(drop)";
    public static final String USE_KEYWORD = "(use)";
    public static final String CREATE_KEYWORD = "(create)";
    public static final String ANY_GROUP_MATCH = "(?<AnyGroup>.+)";
    public static final String INSERT = "(insert)";
    public static final String INTO = "(into)";
    public static final String COLUMN_NAMES = "(?<ColumnName>\\((\\s|\\n)*([A-z0-9]+(\\s|\\n)*)(\\,(\\s|\\n)*[A-z0-9]+(\\s|\\n)" +
            "*)*\\))*";
    public static final String VALUES_KEY = "(values)";
    public static final String COLUMN_VALUES = "(?<values>\\(((\\s|\\n)*[A-z0-9]+(\\s|\\n)*)(\\,(\\s|\\n)*[A-z0-9]+(\\s|\\n)*)" +
            "*\\))";
    public static final String FIELDS = "(?<Fields>\\*|[A-z]+(\\,[A-z]+)*)";
    public static final String COLUMNS = "(?<columns>([A-z1-9]+))";
    public static final String SYMBOLS = "(?<symbols>=)";
    public static final String ARGUMENTS = "(?<arguments>([A-z1-9]+))";
    public static final String SET_VALUES = "(?<setValues>([A-z0-9]+)(\\s|\\n)*=(\\s|\\n)*([A-z0-9]+)(\\,([A-z0-9]+)(\\s|\\n)*=(\\s|\\n)*([A-z0-9]+))*)";

    private RegexConstants() {
    }
}