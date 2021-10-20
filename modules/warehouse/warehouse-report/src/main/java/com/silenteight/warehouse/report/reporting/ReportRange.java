package com.silenteight.warehouse.report.reporting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;
import static java.time.ZoneId.systemDefault;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportRange {

  @NonNull
  OffsetDateTime from;
  @NonNull
  OffsetDateTime to;

  public static ReportRange of(@NonNull LocalDate from, @NonNull LocalDate to) {
    validateRange(from, to);
    ZoneId zoneId = systemDefault();
    OffsetDateTime parsedFrom = from.atStartOfDay(zoneId).toOffsetDateTime();
    OffsetDateTime parsedTo = to.atStartOfDay(zoneId).toOffsetDateTime();
    return new ReportRange(parsedFrom, parsedTo);
  }

  private static void validateRange(LocalDate from, LocalDate to) {
    validateRangeParametersOrder(from, to);
    validateFromParameterIsNotFuture(from);
  }

  private static void validateRangeParametersOrder(LocalDate from, LocalDate to) {
    if (from.isAfter(to))
      throw new InvalidDateRangeParametersOrderException();
  }

  private static void validateFromParameterIsNotFuture(LocalDate from) {
    LocalDate todayDate = getTodayDate();
    if (from.isAfter(todayDate))
      throw new InvalidDateFromParameterException();
  }

  private static LocalDate getTodayDate() {
    return INSTANCE.localDateTime().toLocalDate();
  }
}
