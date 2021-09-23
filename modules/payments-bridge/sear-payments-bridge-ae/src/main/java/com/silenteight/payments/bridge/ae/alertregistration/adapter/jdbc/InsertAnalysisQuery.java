package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class InsertAnalysisQuery {

  @Language("PostgreSQL")
  private static final String SQL = "INSERT INTO pb_analysis VALUES (?, now())";

  private final JdbcTemplate jdbcTemplate;

  void update(long analysisId) {
    jdbcTemplate.update(SQL, analysisId);
  }
}
