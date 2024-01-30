
# Custom Relational Database Management System (RDBMS)

## Introduction
This development of a custom Relational Database Management System (RDBMS). The system supports SQL-like querying, entity-relationship diagram (ERD) generation, transaction and multi-user management, two-factor authentication, and password encryption.

## Features
- **Query System**: Allows SQL-like querying.
- **ERD Generation**: Generates entity-relationship diagrams.
- **Transaction Management**: Supports transactions with concurrent users.
- **Security**: Implements two-factor authentication and password encryption.
- **SQL Dump**: Capability to take a SQL dump of the database.
- **User Management**: Supports multiple user accounts and permissions.

## System Architecture
- **Database Structure**: Utilizes folders for database and table storage, with meta and data separation.
- **Buffer Management**: Manages read/write operations and plays a key role in transaction management.
- **Query Parsing**: Uses regex for syntactic and semantic checks.

## Use Cases
- Creating users, handling authentication, managing databases and tables, data manipulation (select, insert, update, delete), transactions, generating ERDs and dumps.

## Future Scope
- Support for nested queries, joins, ordering results, graphical user interface, remote access.
