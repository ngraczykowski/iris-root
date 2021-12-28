package com.silenteight.warehouse.backup.indexing.listener;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;

public interface ProductionIndexRequestV1BackupCommandHandler {

  void handle(ProductionDataIndexRequest request);
}
