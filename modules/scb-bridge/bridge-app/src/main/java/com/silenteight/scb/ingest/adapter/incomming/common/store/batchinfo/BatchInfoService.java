package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.domain.model.BatchSource;
import com.silenteight.scb.ingest.domain.model.BatchStatus;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BatchInfoService {

  private final BatchInfoRepository repository;

  public void store(String internalBatchId, BatchSource batchSource, int alertCount) {
    repository.save(BatchInfoMapper.toBatchInfoEntity(internalBatchId, batchSource, alertCount));
    log.info(
        "Batch info has been stored with internalBatchId: {}", internalBatchId);
  }

  public void changeStatus(String internalBatchId, BatchStatus status) {
    repository.update(internalBatchId, status);
    log.info(
        "Status of batch info has been changed to {} for internalBatchId: {}",
        status.name(), internalBatchId);
  }
}
