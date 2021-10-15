package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
class SelectRegisteredAlertQuery {

  @Language("PostgreSQL")
  private static final String SQL = "SELECT alert_id, alert_name FROM pb_registered_alert\n"
      + " WHERE alert_name = ?";

  private static final RegisteredAlertRowMapper ROW_MAPPER = new RegisteredAlertRowMapper();
  private final JdbcTemplate jdbcTemplate;

  String execute(String alertName) {
    return jdbcTemplate.queryForObject(SQL, ROW_MAPPER, alertName);
  }

  private static final class RegisteredAlertRowMapper implements RowMapper<String> {

    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString(1);
    }
  }
}
