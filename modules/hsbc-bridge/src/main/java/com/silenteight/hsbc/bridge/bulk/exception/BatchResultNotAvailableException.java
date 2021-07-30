package com.silenteight.hsbc.bridge.bulk.exception;

import lombok.AllArgsConstructor;

public class BatchResultNotAvailableException extends RuntimeException {

  private static final long serialVersionUID = -1626986776133812018L;

  public BatchResultNotAvailableException(String id, Reason reason) {
    super(reason.message + ", id=" + id);
  }

  @AllArgsConstructor
  public enum Reason {
    NOT_COMPLETED("Batch processing is not completed"),
    LEARNING_BATCH("Learning batch results are not available");

    private final String message;
  }
}
