package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.domain.model.BatchSource;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchInfoService {

  private final BatchInfoRepository repository;

  public void store(String internalBatchId, BatchSource batchSource) {
    repository.save(BatchInfoMapper.toBatchInfoEntity(internalBatchId, batchSource));
    log.info(
        "Batch info has been stored with internalBatchId: {}", internalBatchId);
  }
}
