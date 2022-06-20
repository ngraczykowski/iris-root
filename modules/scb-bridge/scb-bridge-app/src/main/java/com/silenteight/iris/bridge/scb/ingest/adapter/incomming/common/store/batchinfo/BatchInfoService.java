/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.batchinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants;
import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchSource;
import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
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
