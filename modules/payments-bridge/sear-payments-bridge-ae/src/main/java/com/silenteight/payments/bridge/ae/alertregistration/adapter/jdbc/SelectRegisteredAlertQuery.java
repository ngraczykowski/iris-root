package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class SelectRegisteredAlertQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT alert_message_id FROM pb_registered_alert\n"
          + " WHERE alert_name = ?\n"
          + " AND alert_message_id IS NOT NULL";

  private static final RegisteredAlertRowMapper ROW_MAPPER = new RegisteredAlertRowMapper();
  private final JdbcTemplate jdbcTemplate;

  UUID execute(String alertName) {
    return jdbcTemplate.queryForObject(SQL, ROW_MAPPER, alertName);
  }

  private static final class RegisteredAlertRowMapper implements RowMapper<UUID> {

    @Override
    public UUID mapRow(ResultSet rs, int rowNum) throws SQLException {
      return UUID.fromString(rs.getString("alert_message_id"));
    }
  }
}
