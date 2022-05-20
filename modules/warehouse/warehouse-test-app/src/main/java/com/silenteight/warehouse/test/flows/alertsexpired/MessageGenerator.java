package com.silenteight.warehouse.test.flows.alertsexpired;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
class MessageGenerator {

  public static final String SQL_SELECT_RANDOM_ALERT_NAMES =
      "SELECT name FROM warehouse_alert order by random() limit %d";

  @NonNull
  private final JdbcTemplate jdbcTemplate;

  AlertsExpired generateAlertsExpired(int alertCount) {
    List<String> alertNames = fetchRandomAlerts(alertCount);
    return AlertsExpired
        .newBuilder()
        .addAllAlerts(alertNames)
        .build();
  }

  private List<String> fetchRandomAlerts(int count) {
    List<String> alertNames = jdbcTemplate.queryForList(
        format(SQL_SELECT_RANDOM_ALERT_NAMES, count),
        String.class);

    if (alertNames.size() < count) {
      log.warn("Insufficient amount of production alerts in the database. "
          + "Generate more production alerts first. "
          + "requestCount={}, actualCount={}", count, alertNames.size());
    }

    if (alertNames.isEmpty()) {
      throw new IllegalStateException("No production alerts in the database");
    }

    return alertNames;
  }
}
