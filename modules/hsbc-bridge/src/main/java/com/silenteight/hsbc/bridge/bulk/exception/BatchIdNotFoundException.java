package com.silenteight.hsbc.bridge.bulk.exception;

public class BatchIdNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -3613850259150064650L;

  public BatchIdNotFoundException(String id) {
    super("Batch id: " + id + " does not exist!");
  }
}
