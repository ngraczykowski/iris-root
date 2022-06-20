/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.batchinfo;

import lombok.experimental.UtilityClass;

import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchSource;

import static com.silenteight.iris.bridge.scb.ingest.domain.model.BatchStatus.QUEUED;

@UtilityClass
public class BatchInfoMapper {

  public BatchInfo toBatchInfoEntity(
      String internalBatchId, BatchSource batchSource, int alertCount) {

    return BatchInfo.builder()
        .internalBatchId(internalBatchId)
        .batchSource(batchSource)
        .batchStatus(QUEUED)
        .alertCount(alertCount)
        .build();
  }
}
