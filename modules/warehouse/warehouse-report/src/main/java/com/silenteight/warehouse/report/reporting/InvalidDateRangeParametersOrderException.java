package com.silenteight.warehouse.report.reporting;

class InvalidDateRangeParametersOrderException extends RuntimeException {

  private static final long serialVersionUID = -4469925343147238597L;

  InvalidDateRangeParametersOrderException() {
    super("Date 'from' must be before date 'to'.");
  }
}
