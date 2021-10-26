package com.silenteight.warehouse.backup.indexing.listener;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;

public interface ProductionIndexRequestCommandHandler {

  void handle(ProductionDataIndexRequest request);
}
