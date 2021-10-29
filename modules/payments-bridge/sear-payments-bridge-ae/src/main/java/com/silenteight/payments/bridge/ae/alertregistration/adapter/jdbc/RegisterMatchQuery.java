package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

class RegisterMatchQuery {

  private final BatchSqlUpdate sql;

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_registered_match(alert_message_id, match_name) VALUES (?, ?)";

  RegisterMatchQuery(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(SQL);
    sql.declareParameter(new SqlParameter("alert_message_id", Types.OTHER));
    sql.declareParameter(new SqlParameter("match_name", Types.VARCHAR));

    sql.compile();
  }

  void execute(UUID alertId, List<String> matchNames) {
    matchNames.forEach(m -> update(alertId, m));
    sql.flush();
  }

  @SuppressWarnings("FeatureEnvy")
  private void update(UUID alertId, String matchName) {
    sql.update(alertId, matchName);
  }
}
