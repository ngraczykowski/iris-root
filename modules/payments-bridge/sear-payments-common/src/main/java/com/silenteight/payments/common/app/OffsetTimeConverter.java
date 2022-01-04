package com.silenteight.payments.common.app;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.annotation.Nonnull;

public class OffsetTimeConverter {

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern(
          "[dd/MM/yyyy HH:mm:ss][dd/MM/yyyy H:mm:ss][yyyy-MM-dd HH:mm:ss][yyyy-MM-dd H:mm:ss]");

  @Nonnull
  public static OffsetDateTime getOffsetDateTime(String timeZone, String time) {
    return LocalDateTime
        .parse(time, DATE_FORMAT)
        .atZone(ZoneId.of(timeZone))
        .toOffsetDateTime();
  }

  public static OffsetDateTime getOffsetDateTime(String timeZone, Timestamp timestamp) {
    return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.of(timeZone));
  }
}
