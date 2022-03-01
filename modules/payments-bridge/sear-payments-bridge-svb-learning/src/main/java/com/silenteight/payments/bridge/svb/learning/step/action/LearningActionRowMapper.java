package com.silenteight.payments.bridge.svb.learning.step.action;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.silenteight.payments.bridge.common.app.OffsetTimeConverter.getOffsetDateTime;


@RequiredArgsConstructor
final class LearningActionRowMapper implements RowMapper<LearningActionEntity> {

  private final String timeZone;

  @Override
  public LearningActionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    return LearningActionEntity
        .builder()
        .fkcoMessages(rs.getLong("fkco_messages"))
        .fkcoVActionComment(rs.getString("fkco_v_action_comment"))
        .fkcoActionDate(rs.getString("fkco_action_date"))
        .fkcoDActionDatetime(getOffsetDateTime(timeZone, rs.getString("fkco_d_action_datetime")))
        .fkcoOperator(rs.getString("fkco_operator"))
        .fkcoStatus(rs.getString("fkco_status"))
        .fkcoITotalAction(rs.getString("fkco_i_total_action"))
        .fkcoVStatusName(rs.getString("fkco_v_status_name"))
        .fkcoVStatusBehavior(rs.getString("fkco_v_status_behavior"))
        .build();
  }
}
