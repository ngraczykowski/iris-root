package com.silenteight.warehouse.backup.indexing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.warehouse.backup.indexing.listener.ProductionIndexRequestV1BackupCommandHandler;
import com.silenteight.warehouse.backup.storage.StorageService;

@Slf4j
@RequiredArgsConstructor
class BackupUseCaseV1 implements ProductionIndexRequestV1BackupCommandHandler {

  @NonNull
  private final StorageService storageService;

  @Override
  public void handle(ProductionDataIndexRequest request) {
    log.info("{}: storing backup, requestId={}, serializedSize={}",
        BackupUseCaseV1.class, request.getRequestId(), request.getSerializedSize());

    storageService.save(request, request.getRequestId(), request.getAnalysisName());

    log.debug("{}: backup processed, requestId={}", BackupUseCaseV1.class, request.getRequestId());
  }
}
