package com.silenteight.payments.bridge.svb.newlearning.step.composite;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertDetails;

import java.sql.ResultSet;
import java.sql.SQLException;

class AlertDetailsRowMapper {

  static AlertDetails mapRow(ResultSet resultSet) throws SQLException {
    return AlertDetails.builder()
        .alertId(resultSet.getLong("learning_alert_id"))
        .fkcoId(resultSet.getLong("fkco_id"))
        .build();
  }
}
