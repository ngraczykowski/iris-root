package com.silenteight.hsbc.bridge.bulk;

public enum BulkStatus {

  STORED,
  PRE_PROCESSED,
  PRE_PROCESSING,
  PROCESSING,
  ERROR,
  COMPLETED,
  DELIVERED,
  CANCELLED;

  public boolean isInternal() {
    return this == PRE_PROCESSED || this == PRE_PROCESSING;
  }
}
