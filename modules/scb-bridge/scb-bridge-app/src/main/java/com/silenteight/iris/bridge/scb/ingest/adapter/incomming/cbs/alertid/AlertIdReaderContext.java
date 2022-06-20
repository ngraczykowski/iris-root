/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.Builder;

public record AlertIdReaderContext(
    AlertIdContext alertIdContext,
    int chunkSize,
    int totalRecordsToRead) {

  @Builder
  public AlertIdReaderContext {}

}
