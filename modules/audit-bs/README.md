# Simple Auditing to Database Library

This bullshit auditing library writes audit events to single table in the 
database.

## TODO

- liquibase migration to create single table in the database (table named
  `audit`)
- API for inserting audit events to the database
- JDBC Template-based so the txs are supported correctly

Must be done by Friday.

Table as in https://youtrack.silenteight.com/issue/Idea-29

API must allow to add all the fields as in table in Idea-29.