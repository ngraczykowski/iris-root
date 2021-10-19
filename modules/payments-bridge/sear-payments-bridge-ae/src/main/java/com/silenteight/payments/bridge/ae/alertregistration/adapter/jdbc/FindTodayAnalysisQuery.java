package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

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
      + "WHERE date(created_at) = current_date\n"
      + "ORDER BY created_at DESC\n"
      + "LIMIT 1";

  private final JdbcTemplate jdbcTemplate;

  Optional<String> execute() {
    var result = DataAccessUtils.singleResult(jdbcTemplate.queryForList(SQL));
    return result == null ? Optional.empty() : Optional.of((String) result.get("analysis_name"));
  }
}
