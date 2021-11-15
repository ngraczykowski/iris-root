package com.silenteight.warehouse.report.reporting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.report.reporting.exception.InvalidDateFromParameterException;
import com.silenteight.warehouse.report.reporting.exception.InvalidDateRangeParametersOrderException;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;
import static java.time.ZoneOffset.UTC;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportRange {

  @NonNull
  OffsetDateTime from;
  @NonNull
  OffsetDateTime to;

  public static ReportRange of(@NonNull OffsetDateTime from, @NonNull OffsetDateTime to) {
    validateRange(from, to);
    return new ReportRange(from, to);
  }

  public LocalDate getFromAsLocalDate() {
    return getFrom().atZoneSameInstant(UTC).toLocalDate();
  }

  public LocalDate getToAsLocalDate() {
    return getTo().atZoneSameInstant(UTC).toLocalDate();
  }

  private static void validateRange(OffsetDateTime from, OffsetDateTime to) {
    validateRangeParametersOrder(from, to);
    validateFromParameterIsNotFuture(from);
  }

  private static void validateRangeParametersOrder(OffsetDateTime from, OffsetDateTime to) {
    if (from.isAfter(to))
      throw new InvalidDateRangeParametersOrderException();
  }

  private static void validateFromParameterIsNotFuture(OffsetDateTime from) {
    OffsetDateTime currentDateTime = getCurrentDateTime();
    if (from.isAfter(currentDateTime))
      throw new InvalidDateFromParameterException();
  }

  private static OffsetDateTime getCurrentDateTime() {
    return INSTANCE.now().atOffset(UTC);
  }
}
