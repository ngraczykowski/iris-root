package com.silenteight.warehouse.indexer.production.v2;

import com.silenteight.data.api.v2.DataIndexResponse;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;

public interface ProductionIndexRequestV2CommandHandler {

  DataIndexResponse handle(ProductionDataIndexRequest request);
}
