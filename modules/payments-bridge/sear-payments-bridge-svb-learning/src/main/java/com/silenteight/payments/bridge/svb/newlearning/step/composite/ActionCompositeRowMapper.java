package com.silenteight.payments.bridge.svb.newlearning.step.composite;

import com.silenteight.payments.bridge.svb.newlearning.domain.ActionComposite;
import com.silenteight.payments.common.app.OffsetTimeConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

class ActionCompositeRowMapper {


  static ActionComposite mapRow(ResultSet resultSet, String timeZone) throws SQLException {
    return ActionComposite.builder()
        .actionId(resultSet.getLong("learning_action_id"))
        .actionDatetime(OffsetTimeConverter.getOffsetDateTime(
            timeZone,
            resultSet.getTimestamp("fkco_d_action_datetime")))
        .statusName(resultSet.getString("fkco_v_status_name"))
        .statusBehaviour(resultSet.getString("fkco_v_status_behavior"))
        .build();
  }
}
