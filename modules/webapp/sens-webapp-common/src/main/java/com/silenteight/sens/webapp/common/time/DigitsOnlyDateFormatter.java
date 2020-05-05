package com.silenteight.sens.webapp.common.time;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static com.silenteight.sens.webapp.common.time.ApplicationTimeZone.TIME_ZONE;


public class DigitsOnlyDateFormatter implements DateFormatter {

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(TIME_ZONE.toZoneId());
  public static final DigitsOnlyDateFormatter INSTANCE = new DigitsOnlyDateFormatter();

  @Override
  public String format(TemporalAccessor value) {
    return FORMATTER.format(value);
  }
}
