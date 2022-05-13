package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches.AlertStatus;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class AlertWithoutMatchesRowMapper implements RowMapper<AlertWithoutMatches> {

  @Override
  public AlertWithoutMatches mapRow(ResultSet rs, int rowNum) throws SQLException {
    return AlertWithoutMatches.builder()
        .id(rs.getString("id"))
        .alertId(rs.getString("alert_id"))
        .alertName(rs.getString("alert_name"))
        .alertStatus(AlertStatus.valueOf(rs.getString("alert_status")))
        .metadata(rs.getString("alert_metadata"))
        .errorDescription(rs.getString("alert_error_description"))
        .build();
  }
}
