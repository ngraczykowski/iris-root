package com.silenteight.payments.bridge.svb.learning.step.composite;

import com.silenteight.payments.bridge.svb.learning.domain.AlertDetails;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.silenteight.payments.bridge.common.app.OffsetTimeConverter.getOffsetDateTime;

class AlertDetailsRowMapper {

  static AlertDetails mapRow(ResultSet resultSet, String timeZone) throws SQLException {
    return AlertDetails.builder()
        .alertId(resultSet.getLong("learning_alert_id"))
        .fkcoId(resultSet.getLong("fkco_id"))
        .fkcoVFormat(resultSet.getString("fkco_v_format"))
        .fkcoVApplication(resultSet.getString("fkco_v_application"))
        .messageId(resultSet.getString("fkco_v_messageid"))
        .systemId(resultSet.getString("fkco_v_system_id"))
        .fkcoDFilteredDateTime(
            getOffsetDateTime(timeZone, resultSet.getTimestamp("fkco_d_filtered_datetime")))
        .fkcoVContent(resultSet.getString("fkco_v_content"))
        .fkcoVFormat(resultSet.getString("fkco_v_format"))
        .fkcoVApplication(resultSet.getString("fkco_v_application"))
        .build();
  }
}
