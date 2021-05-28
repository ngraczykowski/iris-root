package com.silenteight.hsbc.bridge.bulk.exception;

public class BatchIdNotFoundException extends RuntimeException {

  public BatchIdNotFoundException(String id) {
    super("Batch id: " + id + " does not exist!");
  }
}
