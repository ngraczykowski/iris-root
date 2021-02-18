package com.silenteight.hsbc.bridge.bulk.exception;

public class BulkAlreadyCompletedException extends RuntimeException {
  public BulkAlreadyCompletedException(String message) {
    super(message);
  }
}
