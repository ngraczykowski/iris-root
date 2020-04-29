package com.silenteight.sens.webapp.backend.report;

import com.silenteight.sens.webapp.common.time.DateFormatter;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static com.silenteight.sens.webapp.common.time.ApplicationTimeZone.TIME_ZONE;

public class IsoOffsetDateFormatter implements DateFormatter {

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX").withZone(TIME_ZONE.toZoneId());
  public static final IsoOffsetDateFormatter INSTANCE = new IsoOffsetDateFormatter();

  @Override
  public String format(TemporalAccessor value) {
    return FORMATTER.format(value);
  }
}
