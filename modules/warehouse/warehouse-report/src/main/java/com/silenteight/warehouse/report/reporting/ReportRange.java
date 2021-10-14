package com.silenteight.warehouse.report.reporting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static java.time.ZoneId.systemDefault;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportRange {

  @NonNull
  OffsetDateTime from;
  @NonNull
  OffsetDateTime to;

  public static ReportRange of(@NonNull LocalDate from, @NonNull LocalDate to) {
    ZoneId zoneId = systemDefault();
    OffsetDateTime parsedFrom = from.atStartOfDay(zoneId).toOffsetDateTime();
    OffsetDateTime parsedTo = to.atStartOfDay(zoneId).toOffsetDateTime();
    return new ReportRange(parsedFrom, parsedTo);
  }
}
