package com.silenteight.warehouse.common.testing.e2e;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

/*
  This annotation forces Spring to clean database and rebuild the context.
  It is particularly useful for cases where transaction rollback on the test class
  does not rollback transaction in the app class - e.g. when we have two separate transactions
  bounded to different threads (test and rabbit).

  Use with caution as this significantly increases execution time.
 */
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(
    statements = "drop schema public cascade; create schema public;",
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD,
    config = @SqlConfig(transactionMode = ISOLATED))
@Retention(RetentionPolicy.RUNTIME)
public @interface CleanDatabase {
}
