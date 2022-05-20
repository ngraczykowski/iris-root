package com.silenteight.warehouse.report.storage;

class ReportNotSavedException extends RuntimeException {

  private static final long serialVersionUID = 7087594624585948465L;

  ReportNotSavedException(Throwable cause) {
    super("Report has not been successfully saved.", cause);
  }
}
