# Simple Auditing to Database Library

This bullshit auditing library writes audit events to single table in the 
database.

## How to install

1. Add a dependency to this library
1. In your db.changelog-master.xml add the following include:
```
<include file="db/changelog/db.changelog-auditing.xml"/>
```

## How to use

1. Inject the bean of type `AuditingLogger`
2. Call the method `log(AuditDataDto auditDataDto)`

The `AuditDataDto` class has a related builder

## TODO

+ liquibase migration to create single table in the database (table named
  `audit`)
- API for inserting audit events to the database
- JDBC Template-based so the txs are supported correctly

Must be done by Friday.

Table as in https://youtrack.silenteight.com/issue/Idea-29

API must allow to add all the fields as in table in Idea-29.
