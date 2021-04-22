package com.silenteight.warehouse.indexer.alert;

class AlertNotIndexedExceptions extends RuntimeException {

  private static final long serialVersionUID = -2507567520895834660L;

  AlertNotIndexedExceptions(String message, Throwable cause) {
    super(message, cause);
  }
}
