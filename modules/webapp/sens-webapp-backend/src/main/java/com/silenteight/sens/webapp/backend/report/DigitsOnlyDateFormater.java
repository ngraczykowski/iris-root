package com.silenteight.sens.webapp.backend.report;

import com.silenteight.sens.webapp.common.time.DateFormatter;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static com.silenteight.sens.webapp.common.time.ApplicationTimeZone.TIME_ZONE;

public class DigitsOnlyDateFormater implements DateFormatter {

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(TIME_ZONE.toZoneId());
  public static final DigitsOnlyDateFormater INSTANCE = new DigitsOnlyDateFormater();

  @Override
  public String format(TemporalAccessor value) {
    return FORMATTER.format(value);
  }
}
