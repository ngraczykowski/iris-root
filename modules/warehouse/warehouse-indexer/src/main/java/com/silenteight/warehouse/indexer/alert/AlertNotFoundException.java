package com.silenteight.warehouse.indexer.alert;

public class AlertNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -4207507520395834860L;

  AlertNotFoundException(String message) {
    super(message);
  }
}
