package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class InsertMatchQuery {

  private final JdbcTemplate jdbcTemplate;

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_registered_match(alert_message_id, match_name) VALUES (?, ?)";

  void execute(UUID alertId, List<String> matchNames) {
    var sql = createQuery();
    matchNames.forEach(m -> sql.update(alertId, m));
    sql.flush();
  }

  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(SQL);
    sql.declareParameter(new SqlParameter("alert_message_id", Types.OTHER));
    sql.declareParameter(new SqlParameter("match_name", Types.VARCHAR));

    sql.compile();
    return sql;
  }
}
