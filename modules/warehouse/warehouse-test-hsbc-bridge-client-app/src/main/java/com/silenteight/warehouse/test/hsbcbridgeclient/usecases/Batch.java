package com.silenteight.warehouse.test.hsbcbridgeclient.usecases;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Builder(toBuilder = true)
@Slf4j
public class Batch {

  String batchId;

  String status;

  byte[] payload;

  byte[] ingest;

  boolean isCompleted() {
    log.info("Checking batch status, batchId={}, currentStatus={}", batchId, status);
    return "COMPLETED".equals(this.getStatus());
  }
}
