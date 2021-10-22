package com.silenteight.warehouse.report.reporting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.report.reporting.exception.InvalidDateFromParameterException;
import com.silenteight.warehouse.report.reporting.exception.InvalidDateRangeParametersOrderException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;
import static java.time.LocalTime.MAX;
import static java.time.LocalTime.MIDNIGHT;
import static java.time.ZoneOffset.UTC;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportRange {

  @NonNull
  OffsetDateTime from;
  @NonNull
  OffsetDateTime to;

  public static ReportRange of(@NonNull LocalDate from, @NonNull LocalDate to) {
    validateRange(from, to);
    OffsetDateTime parsedFrom = parseToOffsetDateTime(from, MIDNIGHT);
    OffsetDateTime parsedTo = parseToOffsetDateTime(to, MAX);
    return new ReportRange(parsedFrom, parsedTo);
  }

  public static ReportRange of(OffsetDateTime from, OffsetDateTime to) {
    return new ReportRange(from, to);
  }

  public LocalDate getFromAsLocalDate() {
    return getFrom().toLocalDate();
  }

  public LocalDate getToAsLocalDate() {
    return getTo().toLocalDate();
  }

  private static OffsetDateTime parseToOffsetDateTime(LocalDate localDate, LocalTime localTime) {
    return OffsetDateTime.of(localDate, localTime, UTC);
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
