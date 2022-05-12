package com.silenteight.warehouse.test.client.gateway;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;

public interface SimulationV1IndexClientGateway {

  void indexRequest(SimulationDataIndexRequest dataIndexRequest);
}
