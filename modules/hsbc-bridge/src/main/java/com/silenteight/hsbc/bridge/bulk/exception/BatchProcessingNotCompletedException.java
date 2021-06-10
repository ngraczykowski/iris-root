package com.silenteight.hsbc.bridge.bulk.exception;

public class BatchProcessingNotCompletedException extends RuntimeException {

  private static final long serialVersionUID = 4129273769419953289L;

  public BatchProcessingNotCompletedException(String id) {
    super("Batch processing is not completed, id=" + id);
  }
}
