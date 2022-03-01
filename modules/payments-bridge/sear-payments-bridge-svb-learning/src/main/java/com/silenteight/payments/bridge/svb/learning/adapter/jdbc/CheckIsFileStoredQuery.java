package com.silenteight.payments.bridge.svb.learning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CheckIsFileStoredQuery {

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT count(*) FROM pb_learning_csv_row WHERE file_name = ?";
  private final JdbcTemplate jdbcTemplate;

  boolean execute(String fileName) {
    var count = jdbcTemplate.queryForObject(QUERY, Integer.class, fileName);
    return count > 0;
  }
}
