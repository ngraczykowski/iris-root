package com.silenteight.hsbc.bridge.bulk.exception;

public class BatchProcessingNotCompletedException extends RuntimeException {

  public BatchProcessingNotCompletedException(String id) {
    super("Batch processing is not completed, id=" + id);
  }
}
