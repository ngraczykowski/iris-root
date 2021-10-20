package com.silenteight.warehouse.report.reporting;

class InvalidDateFromParameterException extends RuntimeException {

  private static final long serialVersionUID = 8409695278732624133L;

  InvalidDateFromParameterException() {
    super("Date 'from' cannot be future.");
  }
}
