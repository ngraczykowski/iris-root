package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

@Component
@RequiredArgsConstructor
class InsertRegisteredAlertQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_registered_alert(alert_message_id, alert_name) VALUES (?, ?)";

  private final JdbcTemplate jdbcTemplate;

  void execute(List<SaveRegisteredAlertRequest> alerts) {
    var sql = createQuery();
    alerts.forEach(a -> sql.update(a.getAlertId(), a.getAlertName()));
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
