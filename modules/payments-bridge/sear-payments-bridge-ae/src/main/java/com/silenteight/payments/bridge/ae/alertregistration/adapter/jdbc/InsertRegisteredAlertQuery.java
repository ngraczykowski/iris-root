package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
class InsertRegisteredAlertQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO "
          + "pb_registered_alert(alert_name, fkco_system_id, alert_message_id) "
          + "VALUES (:alert_name, :fkco_system_id, :alert_message_id)\n"
          + "RETURNING registered_alert_id";

  private final JdbcTemplate jdbcTemplate;

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  long execute(SaveRegisteredAlertRequest alert) {
    var sql = createQuery();
    var keyHolder = new GeneratedKeyHolder();
    var paramMap =
        Map.of("alert_name", alert.getAlertName(),
            "fkco_system_id", alert.getFkcoSystemId(),
            "alert_message_id", alert.getAlertMessageId());
    sql.updateByNamedParam(paramMap, keyHolder);
    sql.flush();
    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(SQL);
    sql.setReturnGeneratedKeys(true);
    sql.declareParameter(new SqlParameter("alert_name", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("fkco_system_id", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("alert_message_id", Types.OTHER));

    sql.compile();
    return sql;
  }
}
