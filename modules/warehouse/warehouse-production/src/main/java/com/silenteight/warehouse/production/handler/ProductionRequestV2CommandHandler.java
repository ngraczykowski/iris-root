package com.silenteight.warehouse.production.handler;

import com.silenteight.data.api.v2.DataIndexResponse;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;

public interface ProductionRequestV2CommandHandler {

  DataIndexResponse handle(ProductionDataIndexRequest request);
}
