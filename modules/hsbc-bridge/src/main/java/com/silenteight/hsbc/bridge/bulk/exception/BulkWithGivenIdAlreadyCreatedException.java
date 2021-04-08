package com.silenteight.hsbc.bridge.bulk.exception;

public class BulkWithGivenIdAlreadyCreatedException extends RuntimeException {

  public BulkWithGivenIdAlreadyCreatedException(String id) {
    super("Bulk was already created with id=" + id);
  }
}
