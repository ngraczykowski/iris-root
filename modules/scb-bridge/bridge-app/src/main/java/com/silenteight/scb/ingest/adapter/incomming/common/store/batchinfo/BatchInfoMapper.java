package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo;

import lombok.experimental.UtilityClass;

import com.silenteight.scb.ingest.domain.model.BatchSource;

import static com.silenteight.scb.ingest.domain.model.BatchStatus.REGISTERED;

@UtilityClass
public class BatchInfoMapper {

  public BatchInfo toBatchInfoEntity(
      String internalBatchId, BatchSource batchSource, int alertCount) {

    return BatchInfo.builder()
        .internalBatchId(internalBatchId)
        .batchSource(batchSource)
        .batchStatus(REGISTERED)
        .alertCount(alertCount)
        .build();
  }
}
