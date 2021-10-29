package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
class InsertRegisteredAlertQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_registered_alert(alert_id, alert_name) VALUES (?, ?)";

  private final JdbcTemplate jdbcTemplate;

  void execute(UUID alertId, String alertName) {
    jdbcTemplate.update(SQL, alertId, alertName);
  }
}
