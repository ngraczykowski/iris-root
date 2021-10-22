package com.silenteight.warehouse.report.reporting.exception;

public class InvalidDateRangeParametersOrderException extends RuntimeException {

  private static final long serialVersionUID = -4469925343147238597L;

  public InvalidDateRangeParametersOrderException() {
    super("Date 'from' must be before date 'to'.");
  }
}
