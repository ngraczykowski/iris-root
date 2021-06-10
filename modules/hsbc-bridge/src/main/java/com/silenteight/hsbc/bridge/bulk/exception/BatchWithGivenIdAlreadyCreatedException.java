package com.silenteight.hsbc.bridge.bulk.exception;

public class BatchWithGivenIdAlreadyCreatedException extends RuntimeException {

  private static final long serialVersionUID = 1172295115647111754L;

  public BatchWithGivenIdAlreadyCreatedException(String id) {
    super("Batch was already created with id=" + id);
  }
}
