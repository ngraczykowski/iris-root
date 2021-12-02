package com.silenteight.payments.bridge.svb.newlearning.batch.step.message;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
final class LearningAlertedMessageRowMapper
    implements RowMapper<LearningAlertedMessageEntity> {

  private final String timeZone;

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("[dd/MM/yyyy HH:mm:ss][dd/MM/yyyy H:mm:ss]");

  @Override
  public LearningAlertedMessageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    var fkcoMessages = rs.getLong(1);
    var fkcoDFilteredFatetime = getOffsetDateTime(rs.getString(2));
    var fkcoIBlockinghits = rs.getString(3);

    return LearningAlertedMessageEntity
        .builder()
        .fkcoMessages(fkcoMessages)
        .fkcoDfilteredDateTime(fkcoDFilteredFatetime)
        .fkcoIBlockinghits(fkcoIBlockinghits)
        .build();
  }

  @Nonnull
  private OffsetDateTime getOffsetDateTime(String time) {
    return LocalDateTime
        .parse(time, DATE_FORMAT)
        .atZone(ZoneId.of(timeZone))
        .toOffsetDateTime();
  }
}
