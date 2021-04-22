package com.silenteight.warehouse.report.storage;

class AlertNotSavedException extends RuntimeException {

  private static final long serialVersionUID = 7087594624585948465L;

  AlertNotSavedException(String message, Throwable cause) {
    super(message, cause);
  }
}
