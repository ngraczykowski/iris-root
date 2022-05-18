package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.model.DataType;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
class FindAlertDataRetention {

  @Language("PostgreSQL")
  private static final String FIND_ALERT_DATA_EXPIRED_SQL =
      "SELECT alert_name FROM pb_alert_data_retention ar\n"
          + "WHERE ar.alert_time < ? AND\n"
          + " ar.alert_data_removed_at is null";

  @Language("PostgreSQL")
  private static final String FIND_PERSONAL_INFORMATION_EXPIRED_SQL =
      "SELECT alert_name FROM pb_alert_data_retention ar\n"
          + "WHERE ar.alert_time < ? AND\n"
          + " ar.pii_removed_at is null";

  private final JdbcTemplate jdbcTemplate;

  List<String> findAlertTimeBefore(OffsetDateTime dateTime, DataType dataType) {
    return jdbcTemplate.query(getSqlStatement(dataType),
        (rs, rowNum) -> rs.getString(1), dateTime);
  }

  private String getSqlStatement(DataType dataType) {
    switch (dataType) {
      case ALERT_DATA:
        return FIND_ALERT_DATA_EXPIRED_SQL;
      case PERSONAL_INFORMATION:
        return FIND_PERSONAL_INFORMATION_EXPIRED_SQL;
      default:
        throw new IllegalArgumentException("Unknown message type " + dataType);
    }
  }

}
