package com.silenteight.warehouse.report.reporting.exception;

public class InvalidDateFromParameterException extends RuntimeException {

  private static final long serialVersionUID = 8409695278732624133L;

  public InvalidDateFromParameterException() {
    super("Date 'from' cannot be future.");
  }
}
