package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo;

import lombok.experimental.UtilityClass;

import com.silenteight.scb.ingest.domain.model.BatchSource;

@UtilityClass
public class BatchInfoMapper {

  public BatchInfo toBatchInfoEntity(String internalBatchId, BatchSource batchSource) {
    return new BatchInfo(internalBatchId, batchSource);
  }
}
