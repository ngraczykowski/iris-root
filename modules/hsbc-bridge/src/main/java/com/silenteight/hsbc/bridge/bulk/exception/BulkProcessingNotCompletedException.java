package com.silenteight.hsbc.bridge.bulk.exception;

import java.util.UUID;

public class BulkProcessingNotCompletedException extends RuntimeException {

  public BulkProcessingNotCompletedException(UUID uuid) {
    super("Bulk processing is not completed, id=" + uuid.toString());
  }
}
