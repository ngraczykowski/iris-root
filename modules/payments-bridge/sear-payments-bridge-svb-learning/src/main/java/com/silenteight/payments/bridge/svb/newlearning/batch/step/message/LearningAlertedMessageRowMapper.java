package com.silenteight.payments.bridge.svb.newlearning.batch.step.message;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.silenteight.payments.common.app.OffsetTimeConverter.getOffsetDateTime;

@RequiredArgsConstructor
final class LearningAlertedMessageRowMapper
    implements RowMapper<LearningAlertedMessageEntity> {

  private final String timeZone;

  @Override
  public LearningAlertedMessageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    var fkcoMessages = rs.getLong(1);
    var fkcoDFilteredDatetime = getOffsetDateTime(timeZone, rs.getString(2));
    var fkcoIBlockinghits = rs.getString(3);

    return LearningAlertedMessageEntity
        .builder()
        .fkcoMessages(fkcoMessages)
        .fkcoDfilteredDateTime(fkcoDFilteredDatetime)
        .fkcoIBlockinghits(fkcoIBlockinghits)
        .build();
  }
}
