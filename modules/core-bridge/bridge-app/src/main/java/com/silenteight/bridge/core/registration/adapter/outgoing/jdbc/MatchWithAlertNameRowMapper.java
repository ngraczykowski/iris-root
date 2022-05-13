package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class MatchWithAlertNameRowMapper implements RowMapper<MatchWithAlertId> {

  @Override
  public MatchWithAlertId mapRow(ResultSet rs, int rowNum) throws SQLException {
    return MatchWithAlertId.builder()
        .alertId(rs.getString("alert_id"))
        .id(rs.getString("match_id"))
        .name(rs.getString("match_name"))
        .build();
  }
}
