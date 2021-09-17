package com.silenteight.warehouse.indexer.query.single;

public class AlertNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -4207507520395834860L;

  AlertNotFoundException(String message) {
    super(message);
  }
}
