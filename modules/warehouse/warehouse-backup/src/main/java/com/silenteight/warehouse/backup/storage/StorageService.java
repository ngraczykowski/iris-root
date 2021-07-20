package com.silenteight.warehouse.backup.storage;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.warehouse.backup.indexing.ProductionDataIndexStorable;

@Slf4j
public class StorageService implements ProductionDataIndexStorable {

  @Override
  public void save(ProductionDataIndexRequest request) {
    log.debug("saving productionDataIndexRequest with request={}",
        request.getRequestId());
  }
}