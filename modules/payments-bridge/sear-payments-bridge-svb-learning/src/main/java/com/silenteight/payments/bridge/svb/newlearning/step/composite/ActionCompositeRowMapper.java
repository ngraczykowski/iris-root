package com.silenteight.payments.bridge.svb.newlearning.step.composite;

import com.silenteight.payments.bridge.svb.newlearning.domain.ActionComposite;

import java.sql.ResultSet;
import java.sql.SQLException;

class ActionCompositeRowMapper {

  static ActionComposite mapRow(ResultSet resultSet) throws SQLException {
    return ActionComposite.builder().actionId(resultSet.getLong("learning_action_id")).build();
  }
}
