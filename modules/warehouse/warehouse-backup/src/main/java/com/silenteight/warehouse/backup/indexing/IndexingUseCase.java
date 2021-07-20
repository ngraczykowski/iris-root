package com.silenteight.warehouse.backup.indexing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.backup.indexing.listener.ProductionIndexRequestCommandHandler;
import com.silenteight.warehouse.backup.storage.StorageService;

@Slf4j
@RequiredArgsConstructor
class IndexingUseCase implements ProductionIndexRequestCommandHandler {

  @NonNull
  private final StorageService storageService;

  @NonNull
  private final TimeSource timeSource;

  @Override
  public void handle(ProductionDataIndexRequest request) {
    log.debug("{}: ProductionDataIndexRequest received, requestId={}",
        IndexingUseCase.class, request.getRequestId());

    storageService.save(request);

    log.trace("ProductionDataIndexRequest processed, requestId={}",
        request.getRequestId());
  }
}
