package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
@RequiredArgsConstructor
class InsertMatchQuery {

  private final JdbcTemplate jdbcTemplate;

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_registered_match(registered_alert_id, match_name, match_id) VALUES (?, ?, ?)";

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  void execute(SaveRegisteredAlertRequest alert, long registeredAlertId) {
    var sql = createQuery();
    alert
        .getMatches()
        .forEach(m -> sql.update(registeredAlertId, m.getMatchName(), m.getMatchId()));
    sql.flush();
  }

  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(SQL);
    sql.declareParameter(new SqlParameter("registered_alert_id", Types.BIGINT));
    sql.declareParameter(new SqlParameter("match_name", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("match_id", Types.VARCHAR));

    sql.compile();
    return sql;
  }
}
