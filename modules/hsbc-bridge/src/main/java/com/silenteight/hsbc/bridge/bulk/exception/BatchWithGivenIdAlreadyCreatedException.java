package com.silenteight.hsbc.bridge.bulk.exception;

public class BatchWithGivenIdAlreadyCreatedException extends RuntimeException {

  public BatchWithGivenIdAlreadyCreatedException(String id) {
    super("Batch was already created with id=" + id);
  }
}
