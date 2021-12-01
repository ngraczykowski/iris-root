package com.silenteight.warehouse.retention.production.alert.erasing;

class DocumentNotErasedException extends RuntimeException {

  private static final long serialVersionUID = 6983706645543799924L;

  DocumentNotErasedException(String message, Throwable cause) {
    super(message, cause);
  }
}
