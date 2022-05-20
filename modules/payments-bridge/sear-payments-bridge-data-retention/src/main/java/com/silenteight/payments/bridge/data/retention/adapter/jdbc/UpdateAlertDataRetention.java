package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.model.DataType;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class UpdateAlertDataRetention {

  @Language("PostgreSQL")
  private static final String UPDATE_ALERT_DATA_EXPIRED_SQL =
      "UPDATE pb_alert_data_retention SET alert_data_removed_at = :dateTime\n"
          + " WHERE alert_name in (:alerts)";

  @Language("PostgreSQL")
  private static final String UPDATE_PERSONAL_INFORMATION_EXPIRED_SQL =
      "UPDATE pb_alert_data_retention SET pii_removed_at = :dateTime\n"
          + " WHERE alert_name in (:alerts)";


  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  void update(List<String> alertNames, OffsetDateTime dateTime, DataType dataType) {
    namedParameterJdbcTemplate.update(getSqlStatement(dataType),
        Map.of("dateTime", dateTime, "alerts", alertNames));
  }

  private String getSqlStatement(DataType dataType) {
    switch (dataType) {
      case ALERT_DATA:
        return UPDATE_ALERT_DATA_EXPIRED_SQL;
      case PERSONAL_INFORMATION:
        return UPDATE_PERSONAL_INFORMATION_EXPIRED_SQL;
      default:
        throw new IllegalArgumentException("Unknown message type " + dataType);
    }
  }
}
