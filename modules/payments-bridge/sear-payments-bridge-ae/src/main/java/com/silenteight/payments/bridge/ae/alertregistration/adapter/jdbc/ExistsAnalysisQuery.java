package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ExistsAnalysisQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT EXISTS(SELECT 1 FROM pb_analysis where analysis_name = ?)";

  private final JdbcTemplate jdbcTemplate;

  Boolean execute(String analysisName) {
    return jdbcTemplate.queryForObject(SQL, Boolean.class, analysisName);
  }


}
