package com.silenteight.warehouse.indexer.production.v1;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;

public interface ProductionIndexRequestV1CommandHandler {

  DataIndexResponse handle(ProductionDataIndexRequest request);
}
