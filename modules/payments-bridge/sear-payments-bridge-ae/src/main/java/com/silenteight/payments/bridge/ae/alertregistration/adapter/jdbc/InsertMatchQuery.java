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
class InsertMatchQuery {

  private final JdbcTemplate jdbcTemplate;

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_registered_match(alert_message_id, match_name, match_id) VALUES (?, ?, ?)";

  void execute(List<SaveRegisteredAlertRequest> alerts) {
    var sql = createQuery();
    alerts.forEach(alert -> alert
        .getMatches()
        .forEach(m -> sql.update(alert.getAlertId(), m.getMatchName(), m.getMatchId())));
    sql.flush();
  }

  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(SQL);
    sql.declareParameter(new SqlParameter("alert_message_id", Types.OTHER));
    sql.declareParameter(new SqlParameter("match_name", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("match_id", Types.VARCHAR));

    sql.compile();
    return sql;
  }
}
