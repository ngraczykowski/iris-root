package com.silenteight.hsbc.bridge.bulk.exception;

public class BulkIdAlreadyUsedException extends RuntimeException {
  public BulkIdAlreadyUsedException(String message) {
    super(message);
  }
}
