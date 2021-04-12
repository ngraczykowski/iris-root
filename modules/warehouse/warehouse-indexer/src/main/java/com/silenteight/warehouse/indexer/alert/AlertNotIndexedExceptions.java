package com.silenteight.warehouse.indexer.alert;

class AlertNotIndexedExceptions extends RuntimeException {

  AlertNotIndexedExceptions(String message, Throwable cause) {
    super(message, cause);
  }
}
