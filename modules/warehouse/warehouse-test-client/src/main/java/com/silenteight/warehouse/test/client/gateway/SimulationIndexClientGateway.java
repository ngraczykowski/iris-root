package com.silenteight.warehouse.test.client.gateway;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;

public interface SimulationIndexClientGateway {

  void indexRequest(SimulationDataIndexRequest dataIndexRequest);
}
