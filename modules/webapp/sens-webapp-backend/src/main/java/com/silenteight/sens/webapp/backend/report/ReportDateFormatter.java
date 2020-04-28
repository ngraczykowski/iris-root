package com.silenteight.sens.webapp.backend.report;

import com.silenteight.sens.webapp.common.time.DateFormatter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static java.util.TimeZone.getDefault;

class ReportDateFormatter implements DateFormatter {

  private static final ZoneId ZONE_ID = getDefault().toZoneId();
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZONE_ID);

  @Override
  public String format(TemporalAccessor value) {
    return FORMATTER.format(value);
  }
}
