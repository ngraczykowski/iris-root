package com.silenteight.hsbc.bridge.bulk.exception;

public class BulkProcessingNotCompletedException extends RuntimeException {

  public BulkProcessingNotCompletedException(String id) {
    super("Bulk processing is not completed, id=" + id);
  }
}
