package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class AlertWithMatchNamesProjectionRowMapper implements RowMapper<AlertWithMatchNamesProjection> {

  private static final String ALERT_ID = "alert_alert_id";
  private static final String ALERT_NAME = "alert_name";
  private static final String ALERT_STATUS = "alert_status";
  private static final String ALERT_METADATA = "alert_metadata";
  private static final String ALERT_ERROR_DESCRIPTION = "alert_error_description";

  private static final String MATCH_ID = "match_match_id";
  private static final String MATCH_NAME = "match_name";

  @Override
  public AlertWithMatchNamesProjection mapRow(ResultSet rs, int rowNum) throws SQLException {
    return AlertWithMatchNamesProjection.builder()
        .alertId(rs.getString(ALERT_ID))
        .alertName(rs.getString(ALERT_NAME))
        .alertStatus(rs.getString(ALERT_STATUS))
        .alertMetadata(rs.getString(ALERT_METADATA))
        .alertErrorDescription(rs.getString(ALERT_ERROR_DESCRIPTION))
        .matchId(rs.getString(MATCH_ID))
        .matchName(rs.getString(MATCH_NAME))
        .build();
  }
}
