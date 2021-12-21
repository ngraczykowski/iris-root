package com.silenteight.warehouse.production.handler.v1;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;

public interface ProductionRequestV1CommandHandler {

  DataIndexResponse handle(ProductionDataIndexRequest request);
}
