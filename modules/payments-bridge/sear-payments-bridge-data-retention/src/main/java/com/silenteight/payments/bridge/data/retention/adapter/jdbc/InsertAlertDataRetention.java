package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
class InsertAlertDataRetention {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_alert_data_retention(alert_name, alert_time, created_at)\n"
          + " VALUES (?, ?, ?)\n"
          + " ON CONFLICT (alert_name) DO UPDATE SET alert_time = excluded.alert_time";

  private final JdbcTemplate jdbcTemplate;

  void update(Iterable<AlertDataRetention> alerts) {
    var batchSqlUpdate = new BatchSqlUpdate();
    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter("alert_name", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("alert_time", Types.TIMESTAMP));
    batchSqlUpdate.declareParameter(new SqlParameter("created_at", Types.TIMESTAMP));
    batchSqlUpdate.compile();

    alerts.forEach(alert -> batchSqlUpdate.update(
        alert.getAlertName(),
        Timestamp.valueOf(alert.getAlertTime()
            .atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()),
        Timestamp.valueOf(Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime())));
    batchSqlUpdate.flush();
  }
}
