package com.silenteight.warehouse.report.storage.temporary;

class TemporaryReportNotSavedException extends RuntimeException {

  private static final long serialVersionUID = 4461207348361205216L;

  TemporaryReportNotSavedException(String message, Throwable cause) {
    super(message, cause);
  }
}
