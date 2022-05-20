package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;


import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class FindTodayAnalysisQuery {

  @Language("PostgreSQL")
  private static final String SQL = "SELECT analysis_name\n"
      + "FROM pb_analysis\n"
      + "WHERE created_at > now() - interval '%s min'\n"
      + "ORDER BY created_at DESC\n"
      + "LIMIT 1";

  private final JdbcTemplate jdbcTemplate;
  private final CurrentAnalysisQueryProperties currentAnalysisQueryProperties;

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  Optional<String> execute() {
    final String format =
        String.format(SQL, currentAnalysisQueryProperties.getNewAnalysisInterval().toMinutes());
    var result = DataAccessUtils.singleResult(jdbcTemplate.queryForList(format));
    return result == null ? Optional.empty() : Optional.of((String) result.get("analysis_name"));
  }
}
