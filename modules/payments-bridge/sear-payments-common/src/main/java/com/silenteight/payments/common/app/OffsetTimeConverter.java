package com.silenteight.payments.common.app;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.annotation.Nonnull;

public class OffsetTimeConverter {

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("[dd/MM/yyyy HH:mm:ss][dd/MM/yyyy H:mm:ss]");

  @Nonnull
  public static OffsetDateTime getOffsetDateTime(String timeZone, String time) {
    return LocalDateTime
        .parse(time, DATE_FORMAT)
        .atZone(ZoneId.of(timeZone))
        .toOffsetDateTime();
  }
}
