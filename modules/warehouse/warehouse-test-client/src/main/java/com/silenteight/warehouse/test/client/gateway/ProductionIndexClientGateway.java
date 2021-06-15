package com.silenteight.warehouse.test.client.gateway;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;

public interface ProductionIndexClientGateway {

  void indexRequest(ProductionDataIndexRequest dataIndexRequest);
}
