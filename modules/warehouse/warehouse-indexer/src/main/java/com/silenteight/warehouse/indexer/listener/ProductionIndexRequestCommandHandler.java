package com.silenteight.warehouse.indexer.listener;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;

public interface ProductionIndexRequestCommandHandler {

  DataIndexResponse handle(ProductionDataIndexRequest request);
}
