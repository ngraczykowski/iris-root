package com.silenteight.sep.base.common.time;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static com.silenteight.sep.base.common.time.ApplicationTimeZone.TIME_ZONE;

public class LocalWithTimeZoneDateFormatter implements DateFormatter {

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z").withZone(TIME_ZONE.toZoneId());

  public static final LocalWithTimeZoneDateFormatter INSTANCE =
      new LocalWithTimeZoneDateFormatter();

  @Override
  public String format(TemporalAccessor value) {
    return FORMATTER.format(value);
  }
}
