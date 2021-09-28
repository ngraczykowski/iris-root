package com.silenteight.warehouse.report.storage;

class ReportNotSavedException extends RuntimeException {

  private static final long serialVersionUID = 7087594624585948465L;

  ReportNotSavedException(String message, Throwable cause) {
    super(message, cause);
  }
}
