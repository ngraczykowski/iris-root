package com.silenteight.sens.webapp.backend.report.scb;

import com.silenteight.sens.webapp.common.time.DateFormatter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.util.TimeZone.getDefault;

class ScbReportDateFormatter implements DateFormatter {

  private static final ZoneId ZONE_ID = getDefault().toZoneId();
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZONE_ID);

  @Override
  public String format(Instant value) {
    return FORMATTER.format(value);
  }
}
