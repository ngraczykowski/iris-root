package com.silenteight.payments.bridge.svb.learning.step.composite;

import com.silenteight.payments.bridge.common.app.OffsetTimeConverter;
import com.silenteight.payments.bridge.svb.learning.domain.ActionComposite;

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
        .actionComment(resultSet.getString("fkco_v_action_comment"))
        .statusBehaviour(resultSet.getString("fkco_v_status_behavior"))
        .build();
  }
}
