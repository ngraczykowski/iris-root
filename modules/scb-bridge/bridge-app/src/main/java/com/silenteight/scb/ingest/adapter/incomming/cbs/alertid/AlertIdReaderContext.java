package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.Builder;

public record AlertIdReaderContext(
    AlertIdContext alertIdContext,
    int chunkSize,
    int totalRecordsToRead) {

  @Builder
  public AlertIdReaderContext {}

}
