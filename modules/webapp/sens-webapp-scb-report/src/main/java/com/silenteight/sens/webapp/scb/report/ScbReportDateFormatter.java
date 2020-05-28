package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sep.base.common.time.DateFormatter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static com.silenteight.sep.base.common.time.ApplicationTimeZone.TIME_ZONE;

class ScbReportDateFormatter implements DateFormatter {

  private static final ZoneId ZONE_ID = TIME_ZONE.toZoneId();
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZONE_ID);

  @Override
  public String format(TemporalAccessor value) {
    return FORMATTER.format(value);
  }
}
