package com.silenteight.warehouse.report.generation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.List.of;
import static java.util.UUID.randomUUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenerateReportFixtures {

  static final String TEST_BUCKET = "report";
  static final String SELECT_SQL = "SELECT test_col FROM test_temp";
  static final String REPORT_STORAGE_NAME = randomUUID().toString();
  static final String DROP_TABLE_SQL = "DROP TABLE test";
  static final String CREATE_TABLE_WITH_RANDOM_DATA_SQL = "CREATE TABLE test"
      + " AS SELECT test_col, MD5(RANDOM()::text)"
      + " FROM GENERATE_SERIES(1,50) test_col";

  static final String PREPARE_DATA_SQL_1 = "SELECT test_col"
      + " INTO TEMPORARY TABLE test_temp"
      + " FROM test";

  static final String PREPARE_DATA_SQL_2 = "DELETE FROM test_temp"
      + " WHERE test_col > '5'";

  static final List<String> PREPARE_DATA_SQL_STATEMENTS =
      of(PREPARE_DATA_SQL_1, PREPARE_DATA_SQL_2);
}
