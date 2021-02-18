package com.silenteight.hsbc.bridge.bulk.exception;

public class BulkProcessingNotCompletedException extends RuntimeException {

  public BulkProcessingNotCompletedException(String message) {
    super(message);
  }
}
